import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class NoiseRemover {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		String oneFile = "C:\\Users\\ani\\Desktop\\Course data Thesis\\PROCESSED\\Coursera Downloads Processed";
		File root = new File(oneFile);
		File[] filelist = root.listFiles();

		/*System.out.println("--- Run the commands one after another: "
				+ "\n1. Extract raw data from SRT files (optional)"
				+ "\n2. Punctuation removal; "
				+ "\n3. Delete empty lines from the files;"
				+ "\n4. Remove Stop words" 
				+ "\n5. Delete unncessary files; (IRREVERSIBLE!!!);"
				+ "\n9. DELETE ALL TXT FILES (BEFORE STRASTING - IRREVERSIBLE!!!);"
				+ "\n\nWe recommend running dl before sw as files sometimes contain empty lines and sw won't work \n---");
		Scanner inp = new Scanner(System.in);
		int choice = inp.nextInt();
		
		traverseFiles(filelist, choice);*/

		traverseFiles(filelist, 9);
		
		for (int choice = 1; choice < 6; choice++) {
			traverseFiles(filelist, choice);
		}
	}

	/**
	 * Traverse all files one by one and call the respective method depending on the
	 * choice of the user
	 * 
	 * @param filelist
	 * @param choiceIn
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void traverseFiles(File[] filelist, int choiceIn) throws FileNotFoundException, IOException {
		int choice = choiceIn;

		for (File curFile : filelist) {
			if (curFile.isDirectory()) {
				boolean isEmpty = curFile.listFiles().equals(null);

				if (!isEmpty) {
					traverseFiles(curFile.listFiles(), choice);
				}
			} else {
				switch (choice) {
				case 1: 
					// process raw file
					if (curFile.getName().endsWith(".srt")) {
						Logger.log("[Extracting data from SRT...] FILE: " + curFile.getName() + "\n");
						System.out.println("[Extracting data from SRT...] FILE: " + curFile.getName());
						extractTXTfromSRT(curFile);
					}
					continue;
				
				case 2:
					// remove punctuation and numbers
					if (curFile.getName().endsWith(".txt")) {
						Logger.log("[Removing Punctuation and numbers...] FILE: " + curFile.getName() + "\n");
						System.out.println("[Removing Punctuation and numbers...] FILE: " + curFile.getName());
						// call removeNumbers() and once it'd done it will call removePunctuation() on
						// its own
						removeNumbers(curFile);
					}
					continue;
					
				case 3:
					// Remove blank lines
					if (curFile.getName().endsWith(".txt")) {
						if (curFile.getName().endsWith("_NoPunctuationNum.txt")) {
							Logger.log("[Checking for blank lines...] FILE: " + curFile.getName() + "\n");
							System.out.println("[Checking for blank lines...] FILE: " + curFile.getName());
							delEmptyLines(curFile);
						}
					}
					continue;
					
				case 4:
					// Remove StopWords
					if (curFile.getName().endsWith(".txt")) {
						if (curFile.getName().endsWith("_noBlankLines.txt")) {
							Logger.log("[Removing StopWords...] FILE: " + curFile.getName() + "\n");
							System.out.println("[Removing StopWords...] FILE: " + curFile.getName());
							removeStopWords(curFile);
						}
					}
					continue;
					
				case 5:
					// delete unnecessary files
					if ((curFile.getName().endsWith(".srt")) 
							|| (curFile.getName().endsWith("_Raw.txt")) 
							|| (curFile.getName().endsWith("_Processed.txt"))) {
						Logger.log("[--- SKIPPING.. ] FILE: \t" + curFile.getName() + "\n");
						System.out.println("[--- SKIPPING.. ] FILE: \t" + curFile.getName());
					} else {
						Logger.log("[DELETING.. ] FILE: \t" + curFile.getName() + "\n");
						System.out.println("[DELETING.. ] FILE: \t" + curFile.getName());
						curFile.delete();
					}
					continue;
					
				case 9:
					// delete everything except for the srt file (used INITIALLY before processing)
					if (curFile.getName().endsWith(".txt")) {
						Logger.log("[DELETING.. ] FILE: \t" + curFile.getName() + "\n");
						System.out.println("[DELETING.. ] FILE: \t" + curFile.getName());
						curFile.delete();
					}			
					continue;
					
				default:
					System.out.println("Invalid choice.");
					break;
				}
			}
		}
	}

	/**
	 * Process SRT file and extract only text
	 * @param fileIn
	 * @throws IOException
	 */
	private static void extractTXTfromSRT(File fileIn) throws IOException {
		BufferedReader fReader = new BufferedReader(new FileReader(fileIn));
		// REGEX: matches the time of the subtitles:	[0-9]+[:,-> ]+
		// REGEX: matches line made entirely of digits:	[\d]?
		// MATCH EMPTY LINE:	line.matches("\\s+")  &&  line.length() == 0
		String fileLine;
		
		String outpPath = fileIn.getPath().substring(0, fileIn.getPath().lastIndexOf('.')) + "_Raw.txt";
		File outp = new File(outpPath);
		FileWriter fWriter = new FileWriter(outp);
		
		PrintWriter outputFile = new PrintWriter(new FileWriter(outpPath));
		
		while ((fileLine = fReader.readLine()) != null) {
			if(fileLine.matches("[0-9]+[:,-> ]+") || fileLine.matches("[0-9]+")) {
				//System.out.println("TIME or NR LINE: " + fileLine);
				continue;
			} else if(fileLine.matches("\\s+") || fileLine.length() == 0) {
				//System.out.println("EMPTY LINE: " + fileLine);
				continue;
			} else {
				// write to output file in LOWER CASE
				//System.out.println("<<< TEXT LINE: " + fileLine);
				outputFile.write(fileLine.toLowerCase() + "\n");
			}
		}
		fReader.close();		
		outputFile.close();
	}
	
	/**
	 * Remove all numbers from file
	 * @param fileIn
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static void removeNumbers(File fileIn) throws IOException, FileNotFoundException {
		// this method will call the RemoveNumbers method here as it's part of the
		// punctuation removal
		try {
			Scanner fReader = new Scanner(fileIn);
			File outp = new File(
					fileIn.getPath().substring(0, fileIn.getPath().lastIndexOf('.')) + "_NoPunctuation.txt");
			FileWriter fWriter = new FileWriter(outp);

			while (fReader.hasNextLine()) {
				String curLine = fReader.nextLine();
				// Match numbers and math symbols for addition,p division etc.
				fWriter.write(curLine.replaceAll("[0-9+.\\-\\+\\/\\^\\*0-9+]", ""));
				fWriter.write("\n");
			}

			fReader.close();
			fWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String outpFileAbsPath = fileIn.getPath().substring(0, fileIn.getPath().lastIndexOf('.'))
				+ "_NoPunctuation.txt";
		File temp = new File(outpFileAbsPath);

		if (temp.exists()) {
			// System.out.println("Sending file for punctuation removal.. ");
			removePunctuation(temp);
		}
	}

	/**
	 * Remove all punctuation from file, except for commas
	 * @param fileIn
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static void removePunctuation(File fileIn) throws IOException, FileNotFoundException {
		try {
			Scanner fReader = new Scanner(fileIn);
			File outp = new File(
					fileIn.getPath().substring(0, fileIn.getPath().lastIndexOf('_')) + "_NoPunctuationNum.txt");
			FileWriter fWriter = new FileWriter(outp);

			while (fReader.hasNextLine()) {
				String curLine = fReader.nextLine();

				// Match everything that is NOT in the pattern
				fWriter.write(curLine.replaceAll("[^a-z,.\\s]", ""));
				fWriter.write("\n");
			}

			fReader.close();
			fWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Remove stop words from the file
	 * @param fileIn
	 * @throws IOException
	 */
	private static void removeStopWords(File fileIn) throws IOException {

		File stopWordsTXT = new File("C:\\Users\\ani\\Desktop\\Course data Thesis\\stopwords.txt");
		
		// create file reader and go over it to save the stopwords into the Set data
		// structure
		BufferedReader readerSW = new BufferedReader(
				new FileReader("C:\\Users\\ani\\Desktop\\Course data Thesis\\stopwords.txt"));
		Set<String> stopWords = new LinkedHashSet<String>();
		String stopWordsLine = readerSW.readLine();
		while (stopWordsLine != null) {
			// trim() eliminates leading and trailing spaces
			Scanner words = new Scanner(stopWordsLine);
			String word = words.next();
			while (word != null) {
				stopWords.add(word.trim()); // Add the stop words to the set

				if (words.hasNext()) {
					word = words.next(); // If theres another line, read it
				} else {
					break; // else break the inner while loop
				}

			}
			stopWordsLine = readerSW.readLine();
		}

		BufferedReader inp = new BufferedReader(new FileReader(fileIn));
		String line = inp.readLine();
		String basename = fileIn.getPath().substring(0, fileIn.getPath().lastIndexOf('_')) + "_Processed.txt";
		//System.out.println(basename);
		File outp = new File(basename);
		FileWriter out = new FileWriter(outp, true);
		/*
		 * BufferedWriter bw = new BufferedWriter(out); PrintWriter fOut = new
		 * PrintWriter(bw);
		 */

		while (line != null) {
			//System.out.println(">>> LINE: " + line);
			Scanner lineReader = new Scanner(line);
			String word = lineReader.next();
			while (word != null) {
				//System.out.println("\nWord: " + word);
				if (stopWords.contains(word)) {
					//System.out.println(" --- REMOVING: " + word);
					out.write("");
				} else {
					//System.out.println(" --- WRITING: " + word);
					out.write(word + " ");
				}

				if (lineReader.hasNext()) { // If theres another line, read it
					word = lineReader.next();
				} else {
					out.write("\n");
					break; // else break the first while loop
				}
			}
			lineReader.close();
			line = inp.readLine();
		}
		out.close();
	}

	/**
	 * Delete empty lines from files
	 * @param fileIn
	 * @throws IOException
	 */
	private static void delEmptyLines(File fileIn) throws IOException {
		
		BufferedReader inputFileReader = new BufferedReader(new FileReader(fileIn));
		String inputFileLine;
		
		// setup output file with the same name and add _noBlankLines at the end
		String outpPath = fileIn.getPath().substring(0, fileIn.getPath().lastIndexOf('_')) + "_noBlankLines.txt";
		File outp = new File(outpPath);
		PrintWriter outputFile = new PrintWriter(new FileWriter(outpPath));
		
		// check every line and if it has a line with only white spaces of any type 
		// or a line the size of which is = 0, i.e. just a new line without any white space, skip it and don't write in output file
		while ((inputFileLine = inputFileReader.readLine()) != null) {
			if (inputFileLine.matches("\\s+") || inputFileLine.length() == 0) {
				//System.out.println(" ---- Found a blank line ----");
				continue;
			} else {
				outputFile.write(inputFileLine+"\n");
			}
		}
		inputFileReader.close();		
		outputFile.close();
	}
}
