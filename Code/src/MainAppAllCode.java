import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MainAppAllCode {

	public static void main(String[] args) {
		Scanner inp = new Scanner(System.in);
		System.out.println("Enter your choice: \n1. File extension converter \n2. Text preprocessing");
		int choice = inp.nextInt();
		
		System.out.println("Please provide root path: ");
		String path = inp.nextLine();
		
		switch (choice) {
		case 1:
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
			
			break;
			
		case 2:
			TextPreprocessor processor = new TextPreprocessor(path);
			System.out.println(processor.getFileList());
			System.out.println(processor.getDirPath());
			
		default:
			break;
		}
	}
}
