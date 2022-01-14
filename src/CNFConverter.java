
public class CNFConverter {
	
	/*
	 * The varCode function is used to encode our variable 
	 * x_{r,c,v}
	 */
	private int varCode(int row, int col, int val, int n) {
		return (row-1)*n*n + (col-1)*n + val;
	}
	
    public String convertToCNF(int[][] Matrix, int n) {
        StringBuilder clauses = new StringBuilder();
		int sqrt_n = (int) Math.sqrt(n);
        
        /*
		 * Rule 1 : Each entry has at least one value
		 */
		StringBuilder cellDefinednessRule = new StringBuilder();
		for (int r = 1; r <= n; r++) {
			for (int c = 1; c <= n; c++) {		
				for (int v = 1; v <= n; v++) {
					cellDefinednessRule.append(varCode(r,c,v,n)+" ");
				}
				cellDefinednessRule.append(" 0\n");
			}
		}

        
        /*
		 * Rule 2 : Each number appears at most once in each column
		 */
		StringBuilder colUniquenessRule = new StringBuilder();
		for (int r = 1; r <= n; r++) {
			for (int v = 1; v <= n; v++) {		
				for (int ci = 1; ci <= n-1; ci++) {
                    for (int cj = ci + 1; cj <= n; cj++) {
                        colUniquenessRule.append("-"+varCode(r,ci,v,n)+" -"+varCode(r,cj,v,n)+" 0\n");
                    }
				}
			}
		}

        /*
		 * Rule 3 : Each number appears at most once in each row
		 */
		StringBuilder rowUniquenessRule = new StringBuilder();
		for (int c = 1; c <= n; c++) {
			for (int v = 1; v <= n; v++) {		
				for (int ri = 1; ri <= n-1; ri++) {
                    for (int rj = ri+1; rj <= n; rj++) {
                        rowUniquenessRule.append("-"+varCode(ri,c,v,n)+" -"+varCode(rj,c,v,n)+" 0\n");
                    }
				}
			}
		}

        /*
		 * Rule 4 :  Each number appears at most once in each sqrt_n*sqrt_n sub-grid
		 */
		StringBuilder blockDefinednessAndUniquenessRule = new StringBuilder();
		for (int v = 1; v <= n; v++) {
			for (int i = 0; i <= sqrt_n-1; i++) {		
				for (int j = 0; j <= sqrt_n-1; j++) {
					for (int r = 1; r <= sqrt_n; r++) {
                        for (int c = 1; c <= sqrt_n; c++) {
                            for (int k = c+1; k <= sqrt_n; k++) {
                                blockDefinednessAndUniquenessRule.append("-"+varCode((sqrt_n*i+r),(sqrt_n*j+c),v,n)+" -"+varCode((sqrt_n*i+r),(sqrt_n*j+k),v,n)+" 0\n");
                            }
                            for (int k = r+1; k <= sqrt_n; k++) {
                                for (int l = 1; l <= sqrt_n; l++) {
                                    blockDefinednessAndUniquenessRule.append("-"+varCode((sqrt_n*i+r),(sqrt_n*j+c),v,n)+" -"+varCode((sqrt_n*i+k),(sqrt_n*j+l),v,n)+" 0\n");
                                }
                            }
                        }
					}
				}
			}
		}
        
        /*
		 * Rule 5 : The clues must be respected
		 */
		StringBuilder assignedElementsRule = new StringBuilder();
		for (int r = 1; r <= n; r++) {
			for (int c = 1; c <= n; c++) {
				if (Matrix[r-1][c-1] > 0 && Matrix[r-1][c-1] <= n)
                assignedElementsRule.append(varCode(r,c,Matrix[r-1][c-1],n)+" 0\n");
			}
		}
        
        /*
		 * Append rules to clauses List
		 */
		clauses.append(cellDefinednessRule);
		clauses.append(rowUniquenessRule);
        clauses.append(colUniquenessRule);
        clauses.append(blockDefinednessAndUniquenessRule);
        clauses.append(assignedElementsRule);
		
		return clauses.toString();
        
    }
}
