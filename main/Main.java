package main;

public class Main {

    public static int current = 0;

    public static void main(String[] args) {
        String rubyCode= "main/ruby.rb";
        String[] lineArray = RubyToJavaInterpreter.TextFileToStringArray(rubyCode);

        for (; current < lineArray.length; current++) {

            if(RubyToJavaInterpreter.IsComment(lineArray[current])){
                lineArray[current] = RubyToJavaInterpreter.RemoveComment(lineArray[current]);
            }

            RubyToJavaInterpreter.ElseEndDetector(lineArray[current]);
            if (RubyToJavaInterpreter.skipLine) {RubyToJavaInterpreter.skipLine = false; continue;}

            RubyToJavaInterpreter.FindVariableDeclaration(lineArray[current]);
            RubyToJavaInterpreter.IfElseStatements(lineArray[current]);
            RubyToJavaInterpreter.Print(lineArray[current]);
            RubyToJavaInterpreter.WhileLoop(lineArray[current]);
        }

    }
}