package main;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String rubyCode= "main/ruby.rb";
        String[] lineArray = RubyToJavaInterpreter.textFileToStringArray(rubyCode);

        for (int current = 0; current < lineArray.length; current++) {
            RubyToJavaInterpreter.elseDetector(lineArray[current]);
            if (RubyToJavaInterpreter.skipLine) {RubyToJavaInterpreter.skipLine = false; continue;}
            RubyToJavaInterpreter.findVariableDeclaration(lineArray[current]);
            RubyToJavaInterpreter.ifElseStatements(lineArray[current]);
            RubyToJavaInterpreter.print(lineArray[current]);
        }

        RubyToJavaInterpreter.variableMap.forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });

        System.out.println();
    }
}
