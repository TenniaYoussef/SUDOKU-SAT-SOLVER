
public class CNFConverter {
	
	/*
	 * The varCode function is used to encode our variable 
	 * x_{r,c,v}
	 */
	private int varCode(int row, int col, int val, int dim) {
		return (row-1)*dim*dim + (col-1)*dim + (val-1) + 1;
	}
	
    public String convertToCNF(int[][] Matrix, int dim) {
        StringBuilder clauses = new StringBuilder();
		int N = dim*dim;
        
        /*
		 * Rule 1 : Each entry has at least one value
		 */
		String cellDefinednessRule = "";
		for (int r = 1; r <= N; r++) {
			for (int c = 1; c <= N; c++) {		
				for (int v = 1; v <= N; v++) {
					cellDefinednessRule+=varCode(r,c,v,N)+" ";
				}
				cellDefinednessRule+=" 0\n";
			}
		}

        /*
		 * Rule 2 : Each entry has at most one value
		 */
		String cellUniquenessRule = "";
		for (int r = 1; r <= N; r++) {
			for (int c = 1; c <= N; c++) {		
				for (int vi = 1; vi < N; vi++) {
                    for (int vj = vi+1; vj < N; vj++) {
                        cellUniquenessRule+="-"+varCode(r,c,vi,N)+" -"+varCode(r,c,vj,N)+" 0\n";
                    }
				}
			}
		}

        /*
		 * Rule 3 : Each Row has at most one value
		 */
		String rowUniquenessRule = "";
		for (int r = 1; r <= N; r++) {
			for (int v = 1; v <= N; v++) {		
				for (int ci = 1; ci <= N-1; ci++) {
                    //rowUniquenessRule+=;
                    for (int cj = ci + 1; cj <= N; cj++) {
                        rowUniquenessRule+="-"+varCode(r,ci,v,N)+" -"+varCode(r,cj,v,N)+" 0\n";
                    }
                    //rowUniquenessRule+=" 0\n";
				}
			}
		}

        /*
		 * Rule 4 : Each Column has at most one value
		 */
		String colUniquenessRule = "";
		for (int c = 1; c <= N; c++) {
			for (int v = 1; v <= N; v++) {		
				for (int ri = 1; ri <= N-1; ri++) {
                    //colUniquenessRule+=;
                    for (int rj = ri+1; rj <= N; rj++) {
                        colUniquenessRule+="-"+varCode(ri,c,v,N)+" -"+varCode(rj,c,v,N)+" 0\n";
                    }
                    //colUniquenessRule+=" 0\n";
				}
			}
		}

        /*
		 * Rule 5 :  Each number appears at most once in each n*n sub-grid
		 */
		String blockDefinednessAndUniquenessRule = "";
		for (int v = 1; v <= N; v++) {
			for (int i = 0; i <= dim-1; i++) {		
				for (int j = 0; j <= dim-1; j++) {
					for (int r = 1; r <= dim; r++) {
                        for (int c = 1; c <= dim; c++) {
                            for (int k = c+1; k <= dim; k++) {
                                blockDefinednessAndUniquenessRule+="-"+varCode((dim*i+r),(dim*j+c),v,N)+" -"+varCode((dim*i+r),(dim*j+k),v,N)+" 0\n";
                            }
                            for (int k = r+1; k <= dim; k++) {
                                for (int l = 1; l <= dim; l++) {
                                    blockDefinednessAndUniquenessRule+="-"+varCode((dim*i+r),(dim*j+c),v,N)+" -"+varCode((dim*i+k),(dim*j+l),v,N)+" 0\n";
                                }
                            }
                        }
					}
				}
			}
		}
        
        /*
		 * Rule 6 : The clues must be respected
		 */
		String assignedElementsRule = "";
		for (int r = 1; r <= N; r++) {
			for (int c = 1; c <= N; c++) {
				if (Matrix[r-1][c-1] > 0 && Matrix[r-1][c-1] <= N)
                assignedElementsRule+=varCode(r,c,Matrix[r-1][c-1],N)+" 0\n";
			}
		}

        /*
		 * Append rules to clauses 
		 */
		clauses.append(cellDefinednessRule);
		clauses.append(cellUniquenessRule);
		clauses.append(rowUniquenessRule);
        clauses.append(colUniquenessRule);
        clauses.append(blockDefinednessAndUniquenessRule);
        clauses.append(assignedElementsRule);
		
		return clauses.toString();
        
    }
}
