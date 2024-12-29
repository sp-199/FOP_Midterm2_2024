package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.util.HashMap;

public class RubyToJavaInterpreter {

    public static HashMap<String, Object> variableMap = new HashMap<>();
    public static HashMap<String, Class<?>> typeMap = new HashMap<>();

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
        for (int i = 1; i < line.length(); i++) {
            if (line.charAt(i) == '=' && line.charAt(i + 1) != '=' && line.charAt(i - 1) != '=') {
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

                // Getting variable value
                while (k < line.length() && line.charAt(k) != ' ') {
                    variableValue += line.charAt(k);
                    k++;
                }

                // Checking for variable type and adding to map
                if (variableValue.charAt(0) == '"' || variableValue.charAt(0) == '\'') {
                    variableMap.put(variableName, variableValue.substring(1, variableValue.length() - 1));
                    typeMap.put(variableName, String.class);
                } else if (variableValue.charAt(0) == 't') {
                    variableMap.put(variableName, true);
                    typeMap.put(variableName, Boolean.class);
                } else if (variableValue.charAt(0) == 'f') {
                    variableMap.put(variableName, false);
                    typeMap.put(variableName, Boolean.class);
                } else if (variableValue.contains(".")) {
                    variableMap.put(variableName, Double.parseDouble(variableValue));
                    typeMap.put(variableName, Double.class);
                } else {
                    variableMap.put(variableName, Integer.parseInt(variableValue));
                    typeMap.put(variableName, Integer.class);
                }

                break;
            }
        }
    }

    // Checking for if/else statements
    public static void ifElseStatements (String line) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == 'i' && line.charAt(i + 1) == 'f' && line.charAt(i + 2) == ' ') {
                String condition = "";
                int j = i + 2;
                while (j < line.length() - 1) {
                    j++;
                    if (line.charAt(j) == ' ') {
                        continue;
                    }
                    condition += line.charAt(j);
                }
                break;
            }
        }
    }
}
