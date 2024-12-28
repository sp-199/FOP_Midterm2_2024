import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String rubyCode="ruby.rb";
    }
    public static String[]textFileToStringArray(String fileName){
        ArrayList<String> rubyLineArrayList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                rubyLineArrayList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] rubyLinesArray = new String[rubyLineArrayList.size()];
        rubyLineArrayList.toArray(rubyLinesArray);
        return rubyLinesArray;
    }
}
