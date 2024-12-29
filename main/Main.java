package main;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String rubyCode= "main/ruby.rb";
        for (String line: RubyToJavaInterpreter.textFileToStringArray(rubyCode)) {
            RubyToJavaInterpreter.findVariableDeclaration(line);
            System.out.println(line);
        }

        System.out.println("\n");

        RubyToJavaInterpreter.variableMap.forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });
    }
}
