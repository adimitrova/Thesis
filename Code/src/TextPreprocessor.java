import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Class to pre-process data: 
 * Data is .txt and .srt files
 * Get all the data and separate it into sentences. Each sentence on a new line
 * Keep commas as some specific patterns will be looking for comma separated values
 * Recursive solution to go over all files in a folder and sub-folders
 */
public class TextPreprocessor extends Thread {
	public static void main(String[] args) throws IOException, InterruptedException {
		
		//String directoryPath = "C:\\Users\\ani\\Desktop\\Thesis\\PROCESSED\\";
		String directoryPath = "C:\\Users\\ani\\Desktop\\Course data Thesis\\PROCESSED\\Coursera Downloads new";
		File root = new File(directoryPath);
		File ONELineFile =  new File("C:\\Users\\ani\\Desktop\\Thesis\\01_demonstrating-trustworthiness.en_ONELINE.txt");
		File RawFile = new File("C:\\Users\\ani\\Desktop\\Thesis\\01_demonstrating-trustworthiness.en.txt");
		File[] filelist = root.listFiles();
		
		// ------------- RUN FIRST ----------------
		/*showFiles(filelist,1); // first process raw file into one line text and override original file | IMPORTANT: Make data backup first!!!!
		showFiles(filelist,2); // Then grab each file and process it and make it sentence by sent. on a new line | Appends SENTbySENT.txt at the end
		
		filelist = root.listFiles();
		ConvTXTtoWORD(filelist);*/
		
		// ------------- RUN SECOND ---------------
		//delUselessFiles(filelist);
		
		/*TextPreprocessor thread1 = new TextPreprocessor(directoryPath);
		TextPreprocessor thread2;
        thread1.start();
        boolean thread1IsAlive = true;
        boolean thread2IsAlive = false;
                
        if(thread1IsAlive) {
        	// ------------- RUN FIRST ----------------

        	System.out.println("--------- Executing Thread 1 ---------");
    		// Please enter (0), (1) or (2) for NO, INITIAL or FULL processing with method showFiles(FILES, OPTION);
    		showFiles(filelist,1); // first process raw file into one line text and override original file | IMPORTANT: Make data backup first!!!!
    		showFiles(filelist,2); // Then grab each file and process it and make it sentence by sent. on a new line | Appends SENTbySENT.txt at the end
    		
    		filelist = root.listFiles();
    		ConvTXTtoWORD(filelist);
    		
    		filelist = root.listFiles();
    		thread1IsAlive = false;		// kill thread 1
            System.out.println("Thread 1 is alive: " + thread1IsAlive);
    		
            thread2IsAlive = true;
        } 
        
        if(thread2IsAlive) {
        	// ------------- RUN SECOND ---------------
        	System.out.println("\n--------- Executing Thread 2 ---------");
        	thread2 = new TextPreprocessor(directoryPath);
        	thread2.start();
        	
        	delUselessFiles(filelist);
    		thread2IsAlive = false;
    		System.out.println("Thread 2 is alive: " + thread2IsAlive);
        }*/
		
		/*procRawFile(RawFile);
		SplitSentences(ONELineFile);*/
	}


	// ---------------- CLASS FIELDS ----------------
	String directoryPath;
	private File root;
	private static File[] filelist;	
	static int lines;
	
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
	public static File[] getFileList() { return filelist; }
	
	
	// ----------- CLASS SPECIFIC METHODS ------------
	
	/**
	 * Recursive traversal of files and folder and sub-folders
	 * print the files in the console
	 * @param array of files
	 * @param processing option: 
	 * 0 for no processing, just file listing
	 * 1 for initial processing
	 * 2 for full processing
	 * Calls the initial processing method procRawFile(File fileToRead) for every TXT file
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
						// File processing methods here;
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

	
	/**
	 * Gets the file and removes new lines at random places
	 * makes all the text into one line text for the next step of the pre-processing
	 * @WORKS
	 * @IMPORTANT Overrides the original file. MAKE A BACK-UP of the data!!!
	 * @param fileToRead
	 */
	private static void procRawFile(File fileToRead) {
		//BufferedReader reader = new BufferedReader(new FileReader(curFile));
		Scanner fileReaded;
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
			String pathWithNameAndExt = fileToRead.getPath();
			//String pathNoExt = getDirPath(fileToRead,0).concat("_ONELINE.txt");
			
			PrintWriter writerNoLines = new PrintWriter(pathWithNameAndExt, "UTF-8");
			writerNoLines.println(BigSentenceNoLines);
			writerNoLines.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
	}
	
	
	/**
	 * Splits the whole text into sentences and saves it in the original folder with _SENTbySENT.txt extension
	 * @WORKS
	 * @param fileToRead
	 */
	private static void SplitSentences(File fileToRead) {
		/** grab a file
		 * parse content to a String variable
		 * look with a regex into each sentence and parse it into a new ArrayList
		 * write each element of the ArrayList into a the file on a new line
		 * Save and close the file
		 */
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
			readFile.close();
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
				OFSentBySent.println("* " + readText.nextLine());
			}
			
			OFSentBySent.close();
			readText.close();
			// END OF SENTENCE PUNCTUATION PPATTERN 	(?>[.?!])(\\s)
			// WHOLE SENTENCE		[\"']?[A-Z][^.?!]+((?![.?!]['\"]?\\s[\"']?[A-Z][^.?!]).)+[.?!'\"]+
			
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		
	}


	private static void ConvTXTtoWORD(File[] filelistIn) throws IOException {
		for(File curFile : filelistIn) {
			if(curFile.isDirectory()) {
				boolean isEmpty = curFile.listFiles().equals(null);
				if(!(isEmpty == true)) {
					ConvTXTtoWORD(curFile.listFiles());
				}
			} else if(curFile.isFile()) {
				boolean isTXT = curFile.getName().endsWith(".txt");
				boolean isSentBySent = curFile.getName().endsWith("_SENTbySENT.txt");
				System.out.println("Is TXT: " + isTXT + " Is SENTbySENT: " + isSentBySent + " | " + curFile.getName());
				if(isTXT && isSentBySent) {
					String OrigPathNOExt = curFile.getPath().substring(0, curFile.getPath().lastIndexOf('.'));
					String convFileExt = OrigPathNOExt.concat(".rtf");
					PrintWriter converted = new PrintWriter(convFileExt, "utf-8");
					Scanner fileReader = new Scanner(curFile);
					while(fileReader.hasNextLine()) {
						System.out.println("CONVERTING: " + curFile.getName());
						converted.println(fileReader.nextLine());
					}
					fileReader.close();
					converted.close();
				}
			}
		}
	}
	
	private static void delUselessFiles(File[] filelistIn) {
		File[] listOfFiles = filelistIn;
		//float OneKB = 1024.0f;
		
		for(File curFile : listOfFiles) {
			if(curFile.isDirectory()) {
				boolean isEmpty = curFile.listFiles().equals(null);
				if(!(isEmpty == true)) {
					delUselessFiles(curFile.listFiles());
				}
			} else if(curFile.isFile()) {
				// if file is 1KB or smaller, it is probably empty => delete it
				double fileSizeBytes = (double)curFile.length();
				double fileSizeKB = (double)curFile.length()/1024;
				
				// ------ delete all files except the processed to sentences and the SRT files -------
				String endOnSENTbySENT = "_SENTbySENT.txt";
				String endOnSRT = ".srt";
				if(!(curFile.getName().endsWith(endOnSENTbySENT))) {
					if(!curFile.getName().endsWith(endOnSRT)) {
						if(!curFile.getName().endsWith(".rtf")) {
							System.out.println("DELETING USELESS FILE:\t " + curFile.getName() + "\t | SIZE (KB) = " + fileSizeKB);	
							curFile.delete();
						}
					}					
				}
				
				// ------ check if file smaller than 1KB, if yes, check if files has more than 1 line of text, if not, delete --------
				// 0.01 (KB)
				if(curFile.exists()) {
					if(fileSizeKB <= 0.01) { 
						System.out.println("DELETING EMPTY FILE:\t " + curFile.getName() + "\t | SIZE = " + fileSizeKB + " KB | " + fileSizeBytes + " Bytes");
						curFile.delete();
					}
				}
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
			for(File curFile : filelist) {
				if(curFile.isDirectory()) {
					boolean isEmpty = curFile.listFiles().equals(null);
					finaloutp += "\nDir: " + curFile.getName();
					if(!(isEmpty == true)) {
						showFiles(curFile.listFiles(), 0);
					}
				} else if(curFile.isFile()) {
					finaloutp += "\nFile: " + curFile.getName();
				}
			}
		}
		return start + finaloutp + " >";
	}

}
