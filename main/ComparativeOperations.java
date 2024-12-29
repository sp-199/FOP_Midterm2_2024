package main;

import java.util.HashMap;

public class ComparativeOperations {
    public static boolean EqualityResult(String condition, HashMap variableMap) {
        int index = condition.indexOf("==");
        String variable1Name = "";
        String variableName2 = "";

        // Ignoring optional space
        int j = index - 1;
        while (condition.charAt(index - 1) == ' ') j--;

        // Getting variable name
        while (j >= 0 && condition.charAt(j) != ' ') {
            variable1Name += condition.charAt(j);
            j--;
        }
        variable1Name = new StringBuilder(variable1Name).reverse().toString();


        int k = index + 2;
        while (condition.charAt(k) == ' ') k++;

        // Getting second variable name
        while (k < condition.length() && condition.charAt(k) != ' ') {
            variableName2 += condition.charAt(k);
            k++;
        }
        return variableMap.get(variable1Name) == variableMap.get(variableName2);
    }

    private static boolean NotEqualityResult(String condition, HashMap variableMap) {
        int index = condition.indexOf("!=");
        String variable1Name = "";
        String variableName2 = "";

        // Ignoring optional space
        int j = index - 1;
        while (condition.charAt(index - 1) == ' ') j--;

        // Getting variable name
        while (j >= 0 && condition.charAt(j) != ' ') {
            variable1Name += condition.charAt(j);
            j--;
        }
        variable1Name = new StringBuilder(variable1Name).reverse().toString();


        int k = index + 2;
        while (condition.charAt(k) == ' ') k++;

        // Getting second variable name
        while (k < condition.length() && condition.charAt(k) != ' ') {
            variableName2 += condition.charAt(k);
            k++;
        }
        return variableMap.get(variable1Name) != variableMap.get(variableName2);
    }

    private static boolean LessThanResult(String condition, HashMap<String, Integer> variableMap) {

        int index = condition.indexOf("<");
        String variable1Name = "";
        String variableName2 = "";

        // Ignoring optional space
        int j = index - 1;
        while (condition.charAt(index - 1) == ' ') j--;

        // Getting variable name
        while (j >= 0 && condition.charAt(j) != ' ') {
            variable1Name += condition.charAt(j);
            j--;
        }
        variable1Name = new StringBuilder(variable1Name).reverse().toString();


        int k = index + 1;
        while (condition.charAt(k) == ' ') k++;

        // Getting second variable name
        while (k < condition.length() && condition.charAt(k) != ' ') {
            variableName2 += condition.charAt(k);
            k++;
        }
        return variableMap.get(variable1Name) < variableMap.get(variableName2);
    }

    private static boolean GreaterThanResult(String condition, HashMap<String, Integer> variableMap) {
        int index = condition.indexOf(">");
        String variable1Name = "";
        String variableName2 = "";

        // Ignoring optional space
        int j = index - 1;
        while (condition.charAt(index - 1) == ' ') j--;

        // Getting variable name
        while (j >= 0 && condition.charAt(j) != ' ') {
            variable1Name += condition.charAt(j);
            j--;
        }
        variable1Name = new StringBuilder(variable1Name).reverse().toString();


        int k = index + 1;
        while (condition.charAt(k) == ' ') k++;

        // Getting second variable name
        while (k < condition.length() && condition.charAt(k) != ' ') {
            variableName2 += condition.charAt(k);
            k++;
        }
        return variableMap.get(variable1Name) < variableMap.get(variableName2);
    }

    private static boolean LessOrEqualResult(String condition, HashMap<String, Integer> variableMap) {
        int index = condition.indexOf("<=");
        String variable1Name = "";
        String variableName2 = "";

        // Ignoring optional space
        int j = index - 1;
        while (condition.charAt(index - 1) == ' ') j--;

        // Getting variable name
        while (j >= 0 && condition.charAt(j) != ' ') {
            variable1Name += condition.charAt(j);
            j--;
        }
        variable1Name = new StringBuilder(variable1Name).reverse().toString();


        int k = index + 2;
        while (condition.charAt(k) == ' ') k++;

        // Getting second variable name
        while (k < condition.length() && condition.charAt(k) != ' ') {
            variableName2 += condition.charAt(k);
            k++;
        }
        return variableMap.get(variable1Name) <= variableMap.get(variableName2);
    }

    private static boolean GreaterOrEqualResult(String condition, HashMap<String, Integer> variableMap) {
        int index = condition.indexOf(">=");
        String variable1Name = "";
        String variableName2 = "";

        // Ignoring optional space
        int j = index - 1;
        while (condition.charAt(index - 1) == ' ') j--;

        // Getting variable name
        while (j >= 0 && condition.charAt(j) != ' ') {
            variable1Name += condition.charAt(j);
            j--;
        }
        variable1Name = new StringBuilder(variable1Name).reverse().toString();


        int k = index + 2;
        while (condition.charAt(k) == ' ') k++;

        // Getting second variable name
        while (k < condition.length() && condition.charAt(k) != ' ') {
            variableName2 += condition.charAt(k);
            k++;
        }
        return variableMap.get(variable1Name) >= variableMap.get(variableName2);
    }

    public static boolean answer(String condition, HashMap variableMap) {
        //check if line contains a comparative operation
        if (condition.contains("==")) return EqualityResult(condition, variableMap);
        else if (condition.contains("!=")) return NotEqualityResult(condition, variableMap);
        else if (condition.contains("<")) return LessThanResult(condition, variableMap);
        else if (condition.contains(">")) return GreaterThanResult(condition, variableMap);
        else if (condition.contains("<=")) return LessOrEqualResult(condition, variableMap);
        else if (condition.contains(">=")) return GreaterOrEqualResult(condition, variableMap);
        return false;
    }
}