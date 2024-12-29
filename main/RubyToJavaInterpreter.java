package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.util.HashMap;
import java.util.List;

public class RubyToJavaInterpreter {

    public static HashMap<String, Object> variableMap = new HashMap<>();
    public static HashMap<String, Class<?>> typeMap = new HashMap<>();
    public static HashMap<String, Integer> integerMap = new HashMap<>();

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
            if (line.charAt(i) == '=' && line.charAt(i + 1) != '=' && line.charAt(i - 1) != '=' &&
                    line.charAt(i - 1) != '!' && line.charAt(i - 1) != '>' && line.charAt(i - 1) != '<') {
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
                    integerMap.put(variableName, Integer.parseInt(variableValue));
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
                boolean conditionState = ComparativeOperations.answer(condition, integerMap);

                System.out.println(condition + ": " + conditionState);
                break;
            }
        }
    }

//    public static boolean comparator (String condition) {
//        String var1 = "", var2 = "", operator = "";
//        boolean rightSide = false;
//
//        for (int i = 0; i < condition.length(); i++) {
//            char current = condition.charAt(i);
//            if (current == '=' || current == '!' || current == '>' || current == '<') {
//                operator += current;
//                rightSide = true;
//                continue;
//            } else if (rightSide) {
//                var2 += current;
//            } else var1 += current;
//        }
//
//        System.out.println(var1 + " " + operator + " " + var2);
//
//        if (operator.equals("==")) return false;
//        return false;
//    }

    public static int evaluateArithmeticExpression(String expression) {
        List<String> separatedExpression = separateExpression(expression);

        for (int i = 0; i < separatedExpression.size(); i++) {
            if (separatedExpression.get(i).equals("*") || separatedExpression.get(i).equals("/") || separatedExpression.get(i).equals("%")) {
                int a = Integer.parseInt(separatedExpression.get(i - 1));
                int b = Integer.parseInt(separatedExpression.get(i + 1));
                int result = 0;

                switch (separatedExpression.get(i)) {
                    case "*":
                        result = a * b;
                        break;
                    case "/":
                        try{
                            result = a / b;
                            break;
                        }catch (ArithmeticException e){
                            System.out.println(e.getMessage());
                        }
                    case "%":
                        result = a % b;
                        break;
                }

                separatedExpression.set(i - 1, String.valueOf(result));
                separatedExpression.remove(i + 1);
                separatedExpression.remove(i);
                i--;
            }
        }

        for (int i = 0; i < separatedExpression.size(); i++) {
            if (separatedExpression.get(i).equals("+") || separatedExpression.get(i).equals("-")) {
                int a = Integer.parseInt(separatedExpression.get(i - 1));
                int b = Integer.parseInt(separatedExpression.get(i + 1));
                int result = 0;

                switch (separatedExpression.get(i)) {
                    case "+":
                        result = a + b;
                        break;
                    case "-":
                        result = a - b;
                        break;
                }

                separatedExpression.set(i - 1, String.valueOf(result));
                separatedExpression.remove(i + 1);
                separatedExpression.remove(i);
                i--;
            }
        }

        return Integer.parseInt(separatedExpression.get(0));
    }

    private static List<String> separateExpression(String expression) {
        expression = expression.replaceAll(" ", "");
        List<String> separatedExpression = new ArrayList<>();
        StringBuilder number = new StringBuilder();

        for (char ch : expression.toCharArray()) {
            if (Character.isDigit(ch)) {
                number.append(ch);
            } else {
                if (number.length() > 0) {
                    separatedExpression.add(number.toString());
                    number.setLength(0);
                }
                separatedExpression.add(String.valueOf(ch));
            }
        }

        if (number.length() > 0) {
            separatedExpression.add(number.toString());
        }

        return separatedExpression;
    }
}
