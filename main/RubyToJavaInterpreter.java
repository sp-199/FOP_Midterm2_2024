package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.util.HashMap;

public class RubyToJavaInterpreter {

    public static HashMap<String, Object> variableMap = new HashMap<>();

    public static String[]textFileToStringArray(String fileName){
        ArrayList<String> rubyLineArrayList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                rubyLineArrayList.add(line);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        String[] rubyLinesArray = new String[rubyLineArrayList.size()];
        rubyLineArrayList.toArray(rubyLinesArray);

        return rubyLinesArray;
    }

    public static void findVariableDeclaration (String line) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '=' && line.charAt(i + 1) != '=') {
                String variableName = "";
                String variableValue = "";

                // Ignoring optional space
                int j = i - 1;
                if (line.charAt(i - 1) == ' ') j--;

                // Getting variable name
                while (j >= 0 && line.charAt(j) != ' ') {
                    variableName += line.charAt(j);
                    j --;
                }

                variableName = new StringBuilder(variableName).reverse().toString();

                int k = i + 1;
                if (line.charAt(k) == ' ') k++;

                while (k < line.length() && line.charAt(k) != ' ') {
                    variableValue += line.charAt(k);
                    k++;
                }

                if (variableValue.charAt(0) == '"' || variableValue.charAt(0) == '\'') {
                    variableMap.put(variableName, variableValue.substring(1, variableValue.length() - 1));
                } else if (variableValue.charAt(0) == 't') {
                    variableMap.put(variableName, true);
                } else if (variableValue.charAt(0) == 'f') {
                    variableMap.put(variableName, false);
                } else if (variableValue.contains(".")) {
                    variableMap.put(variableName, Double.parseDouble(variableValue));
                } else {
                    variableMap.put(variableName, Integer.parseInt(variableValue));
                }
            }
        }
    }
}
