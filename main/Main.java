package main;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String rubyCode= "main/ruby.rb";
        for (String line: RubyToJavaInterpreter.textFileToStringArray(rubyCode)) {
            System.out.println(line);
            RubyToJavaInterpreter.findVariableDeclaration(line);
            RubyToJavaInterpreter.ifElseStatements(line);
        }

        System.out.println();

        RubyToJavaInterpreter.variableMap.forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });
        RubyToJavaInterpreter.typeMap.forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });
    }
}
