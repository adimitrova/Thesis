import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to preprocess data: 
 * Data is .txt and .srt files
 * Get all the data and separate it into sentences. Each sentence on a new line
 * Keep commas as some specific patterns will be looking for comma separated values
 * Recursive solution to go over all files in a folder and sub-folders
 */

public class TextPreprocessor {
	
	public static void main(String[] args) {
		String directoryPath = "C:\\Users\\ani\\Desktop\\Thesis\\PROCESSED\\01_understanding-research-data";
		File root = new File(directoryPath);
		File oneFile =  new File("C:\\Users\\ani\\Desktop\\Thesis\\01_demonstrating-trustworthiness.en.txt_ONELINE.txt");
		File[] filelist = root.listFiles();
		
		// Please enter (0), (1) or (2) for NO, INITIAL or FULL processing with method showFiles(FILES, OPTION);
		// showFiles(filelist,2); // also calls the procRawFile() method to make the raw data into one whole text
		// readFile(oneFile);

		System.out.println(getDirPath(oneFile,0));
		System.out.println(getDirPath(oneFile,1));
		
		SplitSentences(oneFile);
		System.out.println("\n");
	}



	
	// ---------------- CLASS FIELDS ----------------
	String directoryPath;
	private File root;
	private File[] filelist;	
	
	// ------ CONSTRUCTOR ----------
	public TextPreprocessor(String rootPath) {
		directoryPath = rootPath;
		root = new File(directoryPath);
		filelist = root.listFiles();
	}
	
	// ---------- GETTERS AND SETTERS ------------
	

	/**
	 * set new directory path
	 * Deletes all the files that were previously loaded into the list
	 * DOES NOT DELETE THEM FROM THE SYSTEM, only from the local list
	 * @param newDirPathIn
	 */
	public void changeDir(String newDirPathIn) {
		// assume the user won't figure out to add an initial slash before the path, so we add it
		directoryPath = newDirPathIn;
		root = new File(directoryPath);
		filelist = root.listFiles();
	}
	
	/**
	 * get the curr directory path
	 * Option 0 = return path INCL file name, but WITHOUT Extension
	 * Option 1 = return path WITHOUT file name and extension, but with the last SHASH
	 * @param fileIn
	 * @param fullOrExt0or1
	 * @return
	 */
	public static String getDirPath(File fileIn, int fullOrExt0or1) { 
		int choice = fullOrExt0or1;
		File inpFile = fileIn;
		String fullPath = inpFile.getPath();
		
		String result = "";
		
		String pathNOExt = fullPath.substring(0, fullPath.lastIndexOf('.'));
		String pathNOFileName = fullPath.substring(0, fullPath.lastIndexOf('\\'));
		
		if(choice == 0) {
			result = pathNOExt;
		} else if(choice == 1) {
			result = pathNOFileName + "\\";
		} else {
			System.out.println("ERROR: Invalid path trim choice for method getDirPath()");
		}
		
		return result; 
	}
	
	// get the curr root file
	public File getRootFile() { return root; }
	
	// get the curr root file
	public File[] getFileList() { return filelist; }
	
	// clear the list of files
	public boolean clearFileList() {
		// TODO: implement
		return false;
	}
	
	// ----------- CLASS SPECIFIC METHODS ------------
	
	/**
	 * Recursive traversal of files and folder and sub-folders
	 * print the files in the console
	 * @param array of files
	 * @param processing option: 
	 * 0 for no processing, just file listing
	 * 1 for initial processing
	 * 2 for full processing
	 * Calls the initial pocessing method procRawFile(File fileToRead) for every TXT file
	 * 
	 */
	private static void showFiles(File[] files, int optionIn) {
		int option = optionIn;
		
		switch (option) {
		case 0:
			// ----- OPTION 0: NO processing, only file listing -----
			
			for(File curFile : files) {
				if(curFile.isDirectory()) {
					System.out.println("DIR: \t" + curFile.getName());
					boolean isEmpty = curFile.listFiles().equals(null);
					if(!(isEmpty == true)) {
						showFiles(curFile.listFiles(), 0);
					}
				} else if(curFile.isFile()) {
					System.out.println("\t\t" + curFile.getName());
				}
			}
			
			break;
		
		case 1:
			// ----- OPTION 1: Only initial processing -----
			
			for(File curFile : files) {
				if(curFile.isDirectory()) {
					System.out.println("DIR: \t" + curFile.getName());
					boolean isEmpty = curFile.listFiles().equals(null);
					if(!(isEmpty == true)) {
						showFiles(curFile.listFiles(), 1);
					}
				} else if(curFile.isFile()) {
					System.out.println("\t\t" + curFile.getName());
					if(curFile.getName().endsWith("txt")) {
						procRawFile(curFile);
					}
				}
			}
			
			break;
			
		case 2:
			// ----- OPTION 2: Perform all processing that's possible -----
			
			for(File curFile : files) {
				if(curFile.isDirectory()) {
					System.out.println("DIR: \t" + curFile.getName());
					boolean isEmpty = curFile.listFiles().equals(null);
					if(!(isEmpty == true)) {
						showFiles(curFile.listFiles(), 2);
					}
				} else if(curFile.isFile()) {
					System.out.println("\t\t" + curFile.getName());
					if(curFile.getName().endsWith("txt")) {
						// File processing methods here
						procRawFile(curFile);
						SplitSentences(curFile);
					}
				}
			}
			
			break;

		default:
			System.out.println("ERROR: Invalid choice. Please enter (0), (1) or (2) for NO, INITIAL or FULL processing");
			break;
		}
	}

	
	
	private static void procRawFile(File fileToRead) {
		//BufferedReader reader = new BufferedReader(new FileReader(curFile));
		Scanner fileReaded;
		String[] SENTENCE = new String[50]; 
		ArrayList<String> sentBySent = new ArrayList<String>();
		ArrayList<String> sentenceList = new ArrayList<String>();
		
		// @ WORKS
		try {
			fileReaded = new Scanner(fileToRead);
			
			// check the unordered messy raw file and scan line by line until EoF and add each line as a value in the ArrayList
			while(fileReaded.hasNextLine()) {
				sentenceList.add(fileReaded.nextLine());
			}
			
			String oneBigSentence = "";
		
			//PrintWriter writer = new PrintWriter("C:\\Users\\ani\\Desktop\\Thesis\\ONEsentence.txt", "UTF-8");
			
			/** grab an input file and parse all its content to a String, then scan the string, grab each sentence and add a new line after it
			*  then save to a new file 
			*/
			for (String item : sentenceList) {
				oneBigSentence = oneBigSentence.concat(item);	
			}
			String BigSentenceNoLines = oneBigSentence.trim().replaceAll("\n ", " ");
			
			/**
			 *  @ WORKS
			 *  process file into one single big sentence and save to a file
			 *  SAVE TO FILE (Uncomment when ready)
			 */
			PrintWriter writerNoLines = new PrintWriter("C:\\Users\\ani\\Desktop\\Thesis\\PROCESSED\\"+fileToRead.getName()+"_ONELINE.txt", "UTF-8");
			writerNoLines.println(BigSentenceNoLines);
			writerNoLines.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
	}
	
	
	/**
	 * @WORKS
	 * Splits the whole text into sentences and saves it in the original folder with _SENTbySENT.txt extension
	 * @param fileToRead
	 */
	private static void SplitSentences(File fileToRead) {
		/** grab a file
		 * parse content to a String variable
		 * look with a regex into each sentence and parse it into a new ArrayList
		 * write each element of the ArrayList into a the file on a new line
		 * Save and close the file
		 */
		List<String> sentenceList = new ArrayList<String>();
		File inpFile = fileToRead;
		String text = "";		// used to save first the whole single String from the file and then sentence by sentence
		String pathNoExt = getDirPath(inpFile,0).concat("_SENTbySENT.txt");
		
		try {
			Scanner readFile = new Scanner(inpFile);
			// create an output file
			//System.out.println(inpFile.getPath());
			PrintWriter OFSentBySent = new PrintWriter(pathNoExt, "UTF-8");
			
			while(readFile.hasNext()) {
				text = readFile.nextLine();
			}
			/** replace the content of the text variable with sentences each on a new line by looking at the 
			 * text content and looking for a pattern and when it finds the end of sentence, replaces it with new line
			 * that is saved back to the text String
			 * CON: removed the final punctuation of the sentences, but is not crucial for my work
			 */
			text = text.replaceAll("(?>[.?!])(\\s)", "\n");
			
			/**
			 * Once text is split to sentences on new lines, scans each line
			 * and saves each line by line into the output file
			 */
			Scanner readText = new Scanner(text);
			while(readText.hasNextLine()) {
				sentenceList.add(readText.nextLine());
			}
			
			for (String sentence : sentenceList) {
				OFSentBySent.println(sentence);
			} 
			OFSentBySent.close();
			
			// END OF SENTENCE PUNCTUATION PPATTERN 	(?>[.?!])(\\s)
			// WHOLE SENTENCE		[\"']?[A-Z][^.?!]+((?![.?!]['\"]?\\s[\"']?[A-Z][^.?!]).)+[.?!'\"]+
			
			// System.out.println(sentenceList.size());
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}


	private void curFile() {
		for(File curFile : filelist) {
			if(curFile.isDirectory()) {
				System.out.println("Dir: \t" + curFile.getName());
			} else if(curFile.isFile()) {
				System.out.println("File: \t" + curFile.getName());
			}
		}
	}
	
	
	// overriding the toString() method for the resulting file list ONLY, not for path or anything else
	@Override
	public String toString() {
		String start = "FileList<";
		String finaloutp = "";
		
		if(filelist.length == 0) {
			finaloutp = " NO FILES! Update the file list with .updateFileList()";
		} else {
			for (int i = 0; i < filelist.length; i++) {
				if (filelist[i].isDirectory()) {
					finaloutp += "\nDir: " + filelist[i].getName();
				} else if (filelist[i].isFile()) {
					finaloutp += "\nFile: " + filelist[i].getName();
				}
			} 
		}
		return start + finaloutp + " >";
	}

}
