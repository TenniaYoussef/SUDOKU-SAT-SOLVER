import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		CNFConverter cnfConverter = new CNFConverter();
		
        String fileName = askForFile();
		List<String> fileStream = Files.readAllLines(Paths.get(fileName));
	    int noOfLines = fileStream.size()-1;
	    int n = Integer.parseInt(fileStream.get(0));
	    
	    if (noOfLines != n || n < 0)
	    	throw new IllegalArgumentException("Size does not match with number of lines");
	    
	    int[][] Matrix = new int[n][n];
	    Matrix = generateMatrixFromFilesData(fileStream, n);
	    
	    String formula = cnfConverter.convertToCNF(Matrix, n);
	    int nbVariables =  (int) Math.pow(n,3);
	    int nbClauses = formula.split("\n").length;
	    String DimacsContent = "p cnf " + nbVariables + " " + nbClauses + "\n" + formula;
	    
	    fileWriter(DimacsContent);

        executeMinisat();

        printResult(n);
	    
	}

	private static String askForFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the sudoku file name :");

        String fileName = scanner.next();
        scanner.close();
        return fileName;
    }

    public static void fileWriter(String str) throws IOException {
		FileWriter myWriter = new FileWriter("inputMinisat.txt");
		myWriter.write(str);
		myWriter.close();
	}


    public static int executeMinisat() throws IOException, InterruptedException {
		String[] command = new String[] {"minisat", "inputMinisat.txt", "outputMinisat.txt"};
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		Process process = processBuilder.start();
		return process.waitFor();
	}

    public static void printResult(int n) throws IOException {
        List<String> fileStream = Files.readAllLines(Paths.get("outputMinisat.txt"));
		
		if(fileStream.get(0).equals("SAT")) {
			System.out.println("SATISFIABLE\n");
			String[] values = fileStream.get(1).split(" ");
			for(int r = 0; r < n; r++) {
				for(int c = 0; c < n; c++) {
					for(int k = 0; k < n; k++) {
						int value = Integer.parseInt(values[(n * n * r) + (n * c) + k]);
						if(value > 0) {
							System.out.print((value - (n * n * r) - (n * c)) + " ");
							break;
						}
					}
				}
				System.out.println("\n");
			}
		} else {
			System.out.println("UNSATISFIABLE\n");
        }
	}

	private static int[][] generateMatrixFromFilesData(List<String> fileStream, int n) {
		
		int[][] Matrix = new int[n][n];
		
		for (int r=0; r < n; r++) {
	    	
			String line = fileStream.get(r+1);
	    	
	    	String[] e = line.replaceAll("\\s*", "")
	    					 .split("");
	    	
	    	if (e.length != n)
	    		throw new IllegalArgumentException("Number of columns does not match with number of lines at ligne "+(r+2));
	    	
	    	for (int c=0; c < n; c++) {
	    		if (e[c].matches("\\-|\\_|\\.")) {
	    			Matrix[r][c]=0;
	    		} else {
	    			if (!e[c].matches("[1-9]|[a-p]|[A-P]"))
	    				throw new IllegalArgumentException("Unkonwn format "+e[c]);
	    			
	    			int value = generateIntValueFromString(e[c]);
	    			if(value <= n) {
	    				Matrix[r][c] = value;
	    			} else {
	    				throw new IllegalArgumentException("Each value must be between 1 and " + n);
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
