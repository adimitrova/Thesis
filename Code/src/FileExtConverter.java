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
public class FileExtConverter {
	// ---------------- MAIN METHOD ----------------
	public static void main(String[] args) {
		String path = "C:\\Users\\ani\\Desktop\\topParent";
		FileExtConverter typeChanger = new FileExtConverter(path, "txt");
		File dir = new File(typeChanger.getDirPath());	//new File("path/to/file")
		try {
			String newLine = "\n";
			typeChanger.updateFileList(dir);
			System.out.println(typeChanger.getFileList());
			if (typeChanger.changeType("doc", "srt", dir) == true) {
				typeChanger.updateFileList(dir);
				System.out.println(newLine + typeChanger.getFileList());
			} else {
				System.out.println("Could not update the files");
			}
		} catch (IOException err) {
			err.getMessage();
		}
		
	}
	
	// ---------------- CLASS FIELDS ----------------
	String directoryPath;
	static File dirPath;
	private String absPathofDir = "";
	private String extensionFROM = "";	// convert from this extension
	private String extensionTO = "";	// convert to this extension
	private List<File> resultList = new ArrayList<File>();
	
	// ------ CONSTRUCTOR ----------
	public FileExtConverter(String dirPathIn, String SearchExtensionIn) {
		directoryPath = "/" + dirPathIn;
		dirPath = new File(directoryPath);
		extensionFROM = SearchExtensionIn;
		absPathofDir = dirPath.getAbsolutePath();
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
		directoryPath = "/" + newDirPathIn;
		for (int i = 0; i < resultList.size(); i++) {
			resultList.removeAll(resultList);
		}
		dirPath = new File(directoryPath);
	}
	
	// get the curr directory path
	public String getDirPath() { return directoryPath; }
	
	// clear the list of files
	public boolean clearFileList() {
		boolean isEmpty = false;
		
		for (int i = 0; i < resultList.size(); i++) {
			resultList.removeAll(resultList);
		}
		if(resultList.isEmpty()) {
			isEmpty = true;
		} else if(!resultList.isEmpty()) {
			isEmpty = false;
		}
		
		return isEmpty;
	}
	
	// ----------- CLASS SPECIFIC METHODS ------------
	
	// goes over all files in all folders
	public void updateFileList(File dirPathIn) throws IOException{		
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
				}
			}
		} catch (Exception e) {
			e.getLocalizedMessage();
		}
	}
	
	/**
	 * Change extension of files in a certain directory and its sub-directories
	 * @param extFrom	extension to look for
	 * @param extTo		files to be updated with this extension
	 * @param path		path of the directory where the files will be looked for
	 * @return			returns true if successfully changed or false if not
	 */
	public boolean changeType(String extFrom, String extTo, File path) {
		boolean success = false;
		extensionFROM = extFrom;
		extensionTO = extTo;
		String message = " ";
		
		for (File file : resultList) {
			try {
				String fileName = file.getName();
		        String curFileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
		        if(curFileExt.equals(extFrom)) {
		        	String fileOldType = file.getName();
		        	String fileBaseName = FilenameUtils.getBaseName(fileName + extensionTO);	// get file name w/o extension
		        	file.renameTo(new File(file.getAbsolutePath() + "." + extensionTO));
		        	String fileNewType = file.getName();
		        	message = "------- Changed file(s) from type \"" + curFileExt + "\" to: \"" + extensionTO + "\" --------";
		        	
		        	success = true;
		        } 
		    } catch (Exception e) {
		       System.out.println("changeType() MESSAGE: Could not change type.");
		    }
		}
		System.out.println(message);
		return success;
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
