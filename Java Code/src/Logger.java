import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
    public static void log(String message) throws IOException { 
      PrintWriter out = new PrintWriter(new FileWriter("C:\\Users\\ani\\Desktop\\Course data Thesis\\logPreprocessing.txt", true), true);
      out.write(message);
      out.close();
    }
}