package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.lang.StringBuilder;

public class RubyToJavaInterpreter {

    public static HashMap<String, Object> variableMap = new HashMap<>();
    public static HashMap<String, Class<?>> typeMap = new HashMap<>();
    public static boolean skipLine = false;
    public static boolean shouldSkip = false;
    public static ArrayList<Integer> whileStartList = new ArrayList<>();
    public static ArrayList<String> whileConditionList = new ArrayList<>();
    public static int endCounter = 0;
    public static boolean inWhile = false;

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
                } else if (variableValue.equals("true")) {
                    variableMap.put(variableName, true);
                    typeMap.put(variableName, Boolean.class);
                } else if (variableValue.equals("false")) {
                    variableMap.put(variableName, false);
                    typeMap.put(variableName, Boolean.class);
                } else if (Character.isLetter(variableValue.charAt(0)) || variableValue.charAt(0) == '_') {
                    variableMap.put(variableName, variableMap.get(variableValue));
                    typeMap.put(variableName, typeMap.get(variableValue));
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
            if (i + 2 < line.length() - 1) {
                if (line.charAt(i) == 'i' && line.charAt(i + 1) == 'f' && (i == 0 || line.charAt(i - 1) == ' ') &&
                        (line.charAt(i + 2) == ' ' || line.charAt(i + 2) == '(')) {
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
    }

    public static void ElseEndDetector(String line) {
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


        if (Character.isLetter(var1.charAt(0)) || var1.charAt(0) == '_') leftValue = (Integer) variableMap.get(var1);
        else leftValue = Integer.parseInt(var1);
        if (Character.isLetter(var2.charAt(0)) || var2.charAt(0) == '_') rightValue = (Integer) variableMap.get(var2);
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
                if (Character.isLetter(separatedExpression.get(i + 1).charAt(0))
                        || separatedExpression.get(i + 1).charAt(0) == '_') {
                    b = (Integer) variableMap.get(separatedExpression.get(i + 1));
                } else b = Integer.parseInt(separatedExpression.get(i + 1));
                if (Character.isLetter(separatedExpression.get(i - 1).charAt(0))
                        || separatedExpression.get(i - 1).charAt(0) == '_') {
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
                if (Character.isLetter(separatedExpression.get(i + 1).charAt(0))
                        || separatedExpression.get(i + 1).charAt(0) == '_') {
                    b = (Integer) variableMap.get(separatedExpression.get(i + 1));
                } else b = Integer.parseInt(separatedExpression.get(i + 1));
                if (Character.isLetter(separatedExpression.get(i - 1).charAt(0))
                        || separatedExpression.get(i - 1).charAt(0) == '_') {
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
            if (Character.isLetter(ch) || ch == '_') {
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

    public static boolean ContainsComparators(String line) {
        if(line.contains("<") || line.contains(">") || line.contains("==")){
            return true;
        }
        return false;
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
            line = line.replaceAll(" ", "");
            if(ContainsExpression(line)){
                System.out.println(EvaluateArithmeticExpression(line));
            }else if(ContainsComparators(line)){
                System.out.println(Comparator(line));
            }else{
                if(variableMap.containsKey(line)){
                    System.out.println(variableMap.get(line));
                }else{
                    System.out.println(line);
                }
            }
        } else{
            System.out.print("");
        }
    }

    public static boolean ContainsExpression(String line) {
        return line.contains("+") || line.contains("-") || line.contains("*") || line.contains("/") || line.contains("%");
    }

    public static void WhileLoop (String line) {
        String condition = "";
        if (line.contains("while ") || line.contains("while(")) {
            condition = line.split("while")[1].replaceAll(" ", "")
                    .replaceAll("\\(", "").replaceAll("\\)", "");
            whileConditionList.add(condition);
            whileStartList.add(Main.current);
            inWhile = true;
            if (!Comparator(condition)) {skipLine = true; shouldSkip = true;}
        }
        if (inWhile && (line.contains(" if ") || line.contains(" if("))) endCounter++;
        try {
            if (line.replaceAll(" ", "").equals("end")) {
                if (endCounter == 0) {
                    if (Comparator(whileConditionList.getLast())) {
                        Main.current = whileStartList.getLast();
                    } else {
                        whileConditionList.remove(whileConditionList.getLast());
                        whileStartList.remove(whileStartList.getLast());
                        inWhile = false;
                    }
                } else endCounter--;
            }
        } catch (NoSuchElementException _) {}
    }

    public static String RemoveComment(String line){
        return line.substring(0, line.indexOf("#"));
    }

    public static boolean IsComment(String line){
        line = line.trim();
        return line.contains("#");
    }
}
