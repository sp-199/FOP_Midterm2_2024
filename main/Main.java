package main;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String rubyCode= "main/ruby.rb";
        for (String line: RubyToJavaInterpreter.textFileToStringArray(rubyCode)) {
            RubyToJavaInterpreter.elseDetector(line);
            if (RubyToJavaInterpreter.skipLine) {RubyToJavaInterpreter.skipLine = false; continue;}
            RubyToJavaInterpreter.findVariableDeclaration(line);
            RubyToJavaInterpreter.ifElseStatements(line);
            RubyToJavaInterpreter.print(line);
        }

        System.out.println();
    }
}
