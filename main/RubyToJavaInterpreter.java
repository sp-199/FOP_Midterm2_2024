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
    public static boolean skipLine = false;
    public static boolean shouldSkip = false;

    public static String[] TextFileToStringArray(String fileName){
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

    public static void FindVariableDeclaration(String line) {
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
                    j--;
                }

                variableName = new StringBuilder(variableName).reverse().toString();

                int k = i + 1;
                if (line.charAt(k) == ' ') k++;

                // Getting variable value
                while (k < line.length()) {
                    variableValue += line.charAt(k);
                    k++;
                }

                // Checking for variable type and adding to map
                if (ContainsExpression(variableValue)) {
                    variableMap.put(variableName, EvaluateArithmeticExpression(variableValue));
                    typeMap.put(variableName, Integer.class);
                } else if (variableValue.charAt(0) == '"' || variableValue.charAt(0) == '\'') {
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
    public static void IfElseStatements(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '(' || line.charAt(i) == ')') continue;
            if (line.charAt(i) == 'i' && line.charAt(i + 1) == 'f' && (line.charAt(i + 2) == ' ' || line.charAt(i + 2) == '(')) {
                String condition = "";
                int j = i + 2;
                while (j < line.length() - 1) {
                    j++;
                    if (line.charAt(j) == ' ') {
                        continue;
                    }
                    condition += line.charAt(j);
                }
                boolean conditionState = Comparator(condition);
                skipLine = !conditionState;
                shouldSkip = !conditionState;
                break;
            }
        }
    }

    public static void ElseDetector(String line) {
        if (shouldSkip) {
            if (line.replaceAll(" ", "").equals("else") ||
                    line.replaceAll(" ", "").equals("end")) {
                shouldSkip = false;
            } else skipLine = true;
        } else {
            if (line.replaceAll(" ", "").equals("else")) {
                shouldSkip = true;
            }
        }
    }

    public static boolean Comparator(String condition) {
        String var1 = "", var2 = "", operator = "";
        boolean isRightSide = false;

        for (int i = 0; i < condition.length(); i++) {
            char current = condition.charAt(i);
            if (current == '(' || current == ')') continue;
            if (current == '=' || current == '!' || current == '>' || current == '<') {
                operator += current;
                isRightSide = true;
                continue;
            } else if (isRightSide) {
                var2 += current;
            } else var1 += current;
        }

        int leftValue = 0;
        int rightValue = 0;

        if (ContainsExpression(var1)) var1 = String.valueOf(EvaluateArithmeticExpression(var1));

        if (ContainsExpression(var2)) var2 = String.valueOf(EvaluateArithmeticExpression(var2));


        if (Character.isLetter(var1.charAt(0))) leftValue = (Integer) variableMap.get(var1);
        else leftValue = Integer.parseInt(var1);
        if (Character.isLetter(var2.charAt(0))) rightValue = (Integer) variableMap.get(var2);
        else rightValue = Integer.parseInt(var2);

//        if (leftValue == 0) leftValue = Integer.parseInt(var1);
//        if (rightValue == 0) rightValue = Integer.parseInt(var2);

        if (operator.equals("==")) return leftValue == rightValue;
        else if (operator.equals("!=")) return leftValue != rightValue;
        else if (operator.equals(">=")) return leftValue >= rightValue;
        else if (operator.equals("<=")) return leftValue <= rightValue;
        else if (operator.equals(">")) return leftValue > rightValue;
        else if (operator.equals("<")) return leftValue < rightValue;
        return false;
    }

    public static int EvaluateArithmeticExpression(String expression) {
        List<String> separatedExpression = SeparateExpression(expression);

        for (int i = 0; i < separatedExpression.size(); i++) {
            if (separatedExpression.get(i).equals("*") || separatedExpression.get(i).equals("/") || separatedExpression.get(i).equals("%")) {
                int b = 0;
                int a = 0;
                if (Character.isLetter(separatedExpression.get(i + 1).charAt(0))) {
                    b = (Integer) variableMap.get(separatedExpression.get(i + 1));
                } else b = Integer.parseInt(separatedExpression.get(i + 1));
                if (Character.isLetter(separatedExpression.get(i - 1).charAt(0))) {
                    a = (Integer) variableMap.get(separatedExpression.get(i - 1));
                } else a = Integer.parseInt(separatedExpression.get(i - 1));

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
                int b = 0;
                int a = 0;
                if (Character.isLetter(separatedExpression.get(i + 1).charAt(0))) {
                    b = (Integer) variableMap.get(separatedExpression.get(i + 1));
                } else b = Integer.parseInt(separatedExpression.get(i + 1));
                if (Character.isLetter(separatedExpression.get(i - 1).charAt(0))) {
                    a = (Integer) variableMap.get(separatedExpression.get(i - 1));
                } else a = Integer.parseInt(separatedExpression.get(i - 1));

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

    public static List<String> SeparateExpression(String expression) {
        expression = expression.replaceAll(" ", "");
        List<String> separatedExpression = new ArrayList<>();
        StringBuilder token = new StringBuilder();

        for (char ch : expression.toCharArray()) {
            if (Character.isLetter(ch)) {
                if (!token.isEmpty() && Character.isDigit(token.charAt(0))) {
                    separatedExpression.add(token.toString());
                    token.setLength(0);
                }
                token.append(ch);
            } else if (Character.isDigit(ch)) {
                token.append(ch);
            } else {
                if (!token.isEmpty()) {
                    separatedExpression.add(token.toString());
                    token.setLength(0);
                }
                separatedExpression.add(String.valueOf(ch));
            }
        }
        if (!token.isEmpty()) {
            separatedExpression.add(token.toString());
        }

        return separatedExpression;
    }

    public static void Print(String line){
        if (line.contains("puts ") || line.contains("puts")) {
            line = line.replace("puts", "");
            line = line.trim();
            if(line.charAt(0)=='('){
                line=line.replace("(", "");
                line=line.replace(")", "");
            }
            if(line.charAt(0)=='"'){
                line=line.replace("\"", "");
            }
            if(line.charAt(0)=='\''){
                line=line.replace("'", "");
            }
            System.out.println(line);
        } else{
            System.out.print("");
        }
    }

    public static boolean ContainsExpression(String line) {
        return line.contains("+") || line.contains("-") || line.contains("*") || line.contains("/") || line.contains("%");
    }

    public static int FindWhile(String[] linesArray) {
        for (int i = 0; i < linesArray.length; i++) {
            if (linesArray[i].contains("while ") || linesArray[i].contains("while(")) return i;
        }
        return -1;
    }

    public static int FindLoopEnd(String[] linesArray) {
        int counter = 0;
        for (int i = 0; i < linesArray.length; i++) {
            if (linesArray[i].contains("if")) counter++;
            if (counter == 0 && linesArray[i].contains("end")) return i;
            if (linesArray[i].contains("end")) counter--;
        }
        return -1;
    }

    public static void WhileConditionChecker(String line) {
        String condition = line.split("while")[1];
        condition = condition.replaceAll(" ", "").replaceAll("\\(", "")
                .replaceAll("\\)", "");
        System.out.println(condition);
    }
}
