import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class countLinesAndFiles {

	public static void main(String[] args) throws IOException {
		String directoryPath = "Course data Thesis\\PROCESSED\\Coursera Downloads Processed";
		File root = new File(directoryPath);
		File[] filelist = root.listFiles();

		try {
			countLines(filelist);
			Logger.log("\nTOTAL lines: " + lines + " | TOTAL files: " + files);
			
			System.out.println("TOTAL lines: " + lines + " | TOTAL files: " + files);
		} catch (FileNotFoundException e) {
			System.out.println("No such file " + e.getMessage());
		}

	}
	
	public static int lines = 0;
	public static int files = 0;
	
	private static void countLines(File[] filelist) throws IOException {
		List<File> txtfiles = new ArrayList<File>(); 
		
		for(File curFile : filelist) {
			if(curFile.isDirectory()) {
				boolean isEmpty = curFile.listFiles().equals(null);
				if(isEmpty == false) {
					countLines(curFile.listFiles());
				}
			} else if(curFile.isFile()) {
				if(!(curFile.getName().endsWith(".txt"))) {
					Logger.log("\n\n\n--- SKIP ---: " + curFile.getName());
				} else {
					files++;
					int count = 0;
					Logger.log("\n\n\n--- PROCESSING FILE --- " + curFile.getName());
					Scanner readtxt = new Scanner(curFile);
					while(readtxt.hasNextLine()) {
						Logger.log("\n"+readtxt.nextLine());
						//System.out.println("count lines.. "); 
						count++;
					}
					lines += count;
					readtxt.close();
				}
			}
		}
	}

}

