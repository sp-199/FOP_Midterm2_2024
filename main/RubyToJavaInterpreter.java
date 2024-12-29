package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RubyToJavaInterpreter {
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

    public static int evaluateArithmeticExpression(String expression) {
        List<String> separatedExpression = separateExpression(expression);
        System.out.println("Tokenized Expression: " + separatedExpression);

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
