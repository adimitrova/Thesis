import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
		TextPreprocessor processor = new TextPreprocessor("C:\\Users\\ani\\Github\\Thesis\\Code\\DATA\\data-management");
		System.out.println(processor.filelist);
	}

	// ---------------- CLASS FIELDS ----------------
	String directoryPath;
	private File root;
	private File[] filelist;
	
	// ------ CONSTRUCTOR ----------
	public TextPreprocessor(String dirPathIn) {
		directoryPath = dirPathIn;
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
	
	// get the curr directory path
	public String getDirPath() { return directoryPath; }
	
	// clear the list of files
	public boolean clearFileList() {
		// TODO: implement
		return false;
	}
	
	// ----------- CLASS SPECIFIC METHODS ------------

	
	
	
	
	// overriding the toString() method for the resulting file list ONLY, not for path or anything else
	@Override
	public String toString() {
		String start = "FileList<";
		String finaloutp = "";
		
		if(filelist.length == 0) {
			finaloutp = " NO FILES! Update the file list with .updateFileList()";
		} else {
			for (File file : filelist) {
				if (file.isDirectory()) {
					finaloutp += "\nDir: " + file.getName();
				} else if (file.isFile()) {
					finaloutp += "\nFile: " + file.getName();
				}
			}
		}
		return start + finaloutp + " >";
	}

}
