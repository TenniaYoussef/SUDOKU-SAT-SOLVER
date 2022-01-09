
public class CNFConverter {
	
	/*
	 * The varCode function is used to encode our variable 
	 * x_{r,c,v}
	 */
	private int varCode(int row, int col, int val, int dim) {
		return (row-1)*dim*dim + (col-1)*dim + (val-1) + 1;
	}
	
	public String toCNF(int[][] Matrix, int dim) {
		
		StringBuilder clauses = new StringBuilder();
		int N = dim*dim;

		
		/*
		 * Rule 1 : Each entry has at least one value
		 */
		String rule1 = "";
		for (int r = 1; r <= N; r++) {
			for (int c = 1; c <= N; c++) {		
				for (int v = 1; v <= N; v++) {
					rule1+=varCode(r,c,v,N)+" ";
				}
				rule1+=" 0\n";
			}
		}
		
		/*
		 * Rule 2 : Each entry has at most one value
		 */
		String rule2 = "";
		for (int r = 1; r <= N; r++) {
			for (int c = 1; c <= N; c++) {		
				for (int v = 1; v <= N; v++) {
					for (int w = v+1; w <= N; w++) {
						rule2+="-"+varCode(r,c,v,N)+" -"+varCode(r,c,w,N)+" 0\n";
					}
				}
			}
		}
		
		/*
		 * Rule 3 : Each row has all numbers
		 */
		String rule3 = "";
		for (int r = 1; r <= N; r++) {
			for (int v = 1; v <= N; v++) {		
				for (int c = 1; c <= N; c++) {
					rule3+=varCode(r,c,v,N)+" ";
				}
				rule3+=" 0\n";
			}
		}
		
		/*
		 * Rule 4 : Each column has all numbers
		 */
		String rule4 = "";
		for (int c = 1; c <= N; c++) {
			for (int v = 1; v <= N; v++) {		
				for (int r = 1; r <= N; r++) {
					rule4+=varCode(r,c,v,N)+" ";
				}
				rule4+=" 0\n";
			}
		}
		
		/*
		 * Rule 5 : Each block (subgrid) has all numbers
		 */
		String rule5 = "";
		for (int v = 1; v <= N; v++) {	
			for (int subRow = 0; subRow <= dim; subRow++) {
				for (int subCol = 0; subCol <= dim; subCol++) {		
					for (int rd = 1; rd <= dim; rd++) {
						for (int cd = 1; cd <= dim; cd++) {
							rule5+=varCode(subRow*dim+rd,subCol*dim+cd,v, N)+" ";
						}
					}
					rule5+=" 0\n";
				}
			}
		}
		
		/*
		 * Rule 6 : The clues must be respected
		 */
		String rule6 = "";
		for (int r = 1; r <= N; r++) {
			for (int c = 1; c <= N; c++) {
				if (Matrix[r-1][c-1] > 0 && Matrix[r-1][c-1] <= N)
					rule6+=varCode(r,c,Matrix[r-1][c-1],N)+" 0\n";
			}
		}
		
		/*
		 * Append rules to clauses 
		 */
		clauses.append(rule1);
		clauses.append(rule2);
		clauses.append(rule3);
		clauses.append(rule4);
		clauses.append(rule5);
		clauses.append(rule6);
		
		return clauses.toString();
	}

}
