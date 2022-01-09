import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
	
	
	private static String readFromInputStream(InputStream inputStream) throws IOException {
		StringBuilder resultStringBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}
		}
		return resultStringBuilder.toString();
	}
	
	public static void fileWriter(String str) throws IOException {
		FileWriter myWriter = new FileWriter("minisatInput.txt");
		myWriter.write(str);
		myWriter.close();
	}

	public static void main(String[] args) throws IOException {
		
		CNFConverter cnfConverter = new CNFConverter();
		
		List<String> fileStream = Files.readAllLines(Paths.get(args[0]));
	    int noOfLines = fileStream.size()-1;
	    int n = Integer.parseInt(fileStream.get(0));
	    
	    if (noOfLines != n*n || n < 0)
	    	throw new IllegalArgumentException("Size does not match with number of lines");
	    
	    int[][] Matrix = new int[n * n][n * n];
	    Matrix = generateMatrixFromFilesData(fileStream, n);
	    
	    String formula = cnfConverter.toCNF(Matrix, n);
	    int nbVariables =  (int) Math.pow(n,6);
	    int nbClauses = formula.split("\n").length;
	    String DimacsContent = "p cnf " + nbVariables + " " + nbClauses + "\n" + formula;
	    
	    fileWriter(DimacsContent);
	    
	    

	}

	private static int[][] generateMatrixFromFilesData(List<String> fileStream, int n) {
		
		int[][] Matrix = new int[n * n][n * n];
		
		for (int r=0; r < n*n; r++) {
	    	
			String line = fileStream.get(r+1);
	    	
	    	String[] e = line.replaceAll("\\s*", "")
	    					 .split("");
	    	
	    	if (e.length != n*n)
	    		throw new IllegalArgumentException("Number of columns does not match with number of lines at ligne "+(r+2));
	    	
	    	for (int c=0; c < n*n; c++) {
	    		if (e[c].matches("\\-|\\_|\\.")) {
	    			Matrix[r][c]=0;
	    		} else {
	    			if (!e[c].matches("[1-9]|[a-p]|[A-P]"))
	    				throw new IllegalArgumentException("Unkonwn format "+e[c]);
	    			
	    			int value = generateIntValueFromString(e[c]);
	    			if(value <= n*n) {
	    				Matrix[r][c] = value;
	    			} else {
	    				throw new IllegalArgumentException("Each value must be between 1 and " + (n*n));
	    			}	
	    		}   			
	    	}
	    }
		
		return Matrix;
	}
	
	private static int generateIntValueFromString(String s) {
		String value = s.toLowerCase();
		
		if (isAnInteger(value))
			return Integer.parseInt(value);
		
		int v = 10;
		for (char c = 'a'; c <= 'p'; c++) {
			if(c == value.charAt(0))
				return v;
			v++;
		}
		return -1;
	}
	
	private static boolean isAnInteger(String string) {
		try {
			Integer.parseInt(string);
		} catch(NumberFormatException n) {
			return false;
		}
		return true;
	}

}
