import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class playground {

	public static void main(String[] args) {

		//BufferedReader reader = new BufferedReader(new FileReader(curFile));
		ArrayList<String> sentBySent = new ArrayList<String>();
			
		String BigSentenceNoLines = "In this module, you'll be introducedto the concepts of file formats, compression, data normalization,and transformations. By the end of the module,you'll understand why these are important. And you'll be able to make decisionsabout which approach to use when. You'll also be able to usethe content featured in the module to help improve your researchdata management practice. So what is a file format? A file format is a way of encodinginformation within a computer file. A program or an applicationneeds to recognize that file format in order to be able toaccess the content within the file. A web browser, for example,will recognize HTML, things in hypertext markup language, and will be able to openthese files and display them as webpages. If a browser comes across a different typeof file it might call on a special kind of plugin to be able to open anddisplay those files. Otherwise, if it doesn't recognizethe file type, it may offer the option to download the file soyou can open it with the relevant program. The file format is often indicated aspart of the file name in an extension or suffix. Conventionally, the extension";
		//BigSentenceNoLines.replaceAll("[.?!]", "-------");
		//System.out.println(BigSentenceNoLines.replaceAll("(?>[.?!])(\\s)", "\n"));
		
		ArrayList<String> saveTo = new ArrayList<String>();
		saveTo.add(BigSentenceNoLines.replaceAll("(?>[.?!])(\\s)", "\n"));
		
		for (String string : saveTo) {
			System.out.println(string + "\n");
		}
		/*try {
			String[] sentList = BigSentenceNoLines.split("[A-Za-z]*[.?!]");
			PrintWriter sentBySentFile = new PrintWriter("C:\\Users\\ani\\Desktop\\Thesis\\PROCESSED\\regexTest_SENTbySENT.txt", "UTF-8");
			for (String sent : sentList) {
				sentBySentFile.println(sent + "\n");
				System.out.println(sent + "\n");
			}
			sentBySentFile.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		}*/
	}
	
	
}
