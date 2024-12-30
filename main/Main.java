package main;

import static main.RubyToJavaInterpreter.*;

public class Main {
    public static void main(String[] args) {
        String rubyCode= "main/ruby.rb";
        String[] lineArray = RubyToJavaInterpreter.TextFileToStringArray(rubyCode);


        for (int current = 0; current < lineArray.length; current++) {
            if(RubyToJavaInterpreter.IsComment(lineArray[current])){
                lineArray[current] = RubyToJavaInterpreter.RemoveComment(lineArray[current]);
            };
            RubyToJavaInterpreter.ElseDetector(lineArray[current]);
            if (RubyToJavaInterpreter.skipLine) {RubyToJavaInterpreter.skipLine = false; continue;}
            RubyToJavaInterpreter.FindVariableDeclaration(lineArray[current]);
            RubyToJavaInterpreter.IfElseStatements(lineArray[current]);
            RubyToJavaInterpreter.Print(lineArray[current]);
        }


        System.out.println();
    }
}