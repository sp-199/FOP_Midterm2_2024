package main;

public class Main {
    public static void main(String[] args) {
        String rubyCode= "main/ruby.rb";
        String[] lineArray = RubyToJavaInterpreter.TextFileToStringArray(rubyCode);

//        RubyToJavaInterpreter.WhileConditionChecker(lineArray[RubyToJavaInterpreter.FindWhile(lineArray)]);
//        System.out.println(RubyToJavaInterpreter.FindLoopEnd(lineArray));

        for (int current = 0; current < lineArray.length; current++) {
            RubyToJavaInterpreter.ElseDetector(lineArray[current]);
            if (RubyToJavaInterpreter.skipLine) {RubyToJavaInterpreter.skipLine = false; continue;}
            RubyToJavaInterpreter.FindVariableDeclaration(lineArray[current]);
            RubyToJavaInterpreter.IfElseStatements(lineArray[current]);
            RubyToJavaInterpreter.Print(lineArray[current]);
        }



//        RubyToJavaInterpreter.variableMap.forEach((key, value) -> {
//            System.out.println(key + ": " + value);
//        });

        System.out.println();
    }
}
