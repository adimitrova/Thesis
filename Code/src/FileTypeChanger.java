import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
// download at http://commons.apache.org/io/


/**
 * Class to covnert files from one type to another for easy processing 
 * PROBLEM: keeps the old file extension before the new one, but the file is converted.
 * Will fix this someday
 */
public class FileTypeChanger {
	public static void main(String[] args) {
		String path = "C:\\Users\\ani\\Desktop\\topParent";
		FileTypeChanger typeChanger = new FileTypeChanger(path, "txt");
		File dir = new File(typeChanger.getDirPath());	//new File("path/to/file")
		try {
			updateFileList(dir);
		} catch (IOException err) {
			err.getMessage();
		}
		System.out.println(typeChanger.getFileList());
	}
	
	String directoryPath;
	static File dirPath;
	private String absPathofDir = "";
	private static String extensionFROM = "";	// convert from this extension
	private static String extensionTO = "";	// convert to this extension
	private static List<File> resultList = new ArrayList<File>();
	
	public FileTypeChanger(String dirPathIn, String SearchExtensionIn) {
		directoryPath = "/" + dirPathIn;
		dirPath = new File(directoryPath);
		extensionFROM = SearchExtensionIn;
		absPathofDir = dirPath.getAbsolutePath();
	}
	
	
	
	
	public void changeDir(String newDirPathIn) {
		// assume the user won't figure out to add an initial slash before the path, so we add it
		directoryPath = "/" + newDirPathIn;
		for (int i = 0; i < resultList.size(); i++) {
			resultList.removeAll(resultList);
		}
		dirPath = new File(directoryPath);
	}
	
	
	
	
	public String getDirPath() { return directoryPath; }
	
	
	
	
	// goes over all files in all folders
	public static void updateFileList(File dirPathIn) throws IOException{		
		try {
			// get all the files from a directory
			File[] fileList = dirPathIn.listFiles();
			
			for (File file : fileList) {
				if(file.isDirectory()) {
					resultList.add(file);
					File tempPath = new File(file.getPath());
					//updateFileList(file.getAbsolutePath());
					updateFileList(file);
				} else if (file.isFile()) {
					resultList.add(file);
					System.out.println(changeType(file, "srt", "rtf", dirPathIn));
				}
			}
		} catch (Exception e) {
			e.getLocalizedMessage();
		}
	}
	
	
	public static String changeType(File fileToChange, String extFrom, String extTo, File path) {
		extensionFROM = extFrom;
		extensionTO = extTo;
		
		try {
			String fileName = fileToChange.getName();
	        String curFileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
	        String message = " ";
	        if(curFileExt.equals(extFrom)) {
	        	message = ">>>>> " + extFrom + " FILE found: " + fileToChange.getName();
	        	String fileBaseName = FilenameUtils.getBaseName(fileName + extensionTO); // gets the file name without the extension
	        	
	        	fileToChange.renameTo(new File(fileToChange.getAbsolutePath() + "." + extensionTO));
	        	System.out.println("new file name: " + fileToChange);
	        } 
	        return message;
	    } catch (Exception e) {
	       return "changeType() MESSAGE: Nothing found";
	    }		
	}
	
	
	
	// because I can do toString() only with the FileTypeChanger object, which is not intuitive, 
	// I created this method that sounds more intuitive
	public String getFileList() { return this.toString(); }
	
	
	
	
	// overriding the toString() method for the resulting file list ONLY, not for path or anything else
	@Override
	public String toString() {
		String start = "FileList<";
		String finaloutp = "";
		
		if(resultList.isEmpty()) {
			finaloutp = " NO FILES! Update the file list with .updateFileList()";
		} else {
			for (File file : resultList) {
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
