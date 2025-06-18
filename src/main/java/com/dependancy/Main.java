package com.dependancy;

import com.dependancy.errors.InvalidToken;
import com.dependancy.inputpreprocessor.PreLexer;
import com.dependancy.tokenizer.Lexer;

public class Main {

    public static void main(String[] args) {

        var input = "void main()";
        var input1 = "print(\"hello,World\",temp,121)";
        var input2 = "int temp=123";
        var input3 = "string temp1=\"hoi hello\"";
        var input4 = "is_correct=true";
        var input5 = "is_correct=true and false";
        var input6 = "}";


        var inputProgram = "void main() {\n" +
                "print(\"program started\")\n"+
                "int temp = 0\n"+
                "while(temp < 100){\n"+
                "print(\"temp is: \" + temp)\n"+
                "temp = temp + 1\n"+
                "}\n"+
                "print(\"program ended\")\n"+
                "return\n"+
                "}\n\n\n"+
                "int add(int x, int y){\n"+
                "return x + y\n"+
                "}";


        PreLexer preLexer = new PreLexer();

        String[] inputs = inputProgram.split("\n");
        for(String s : inputs){
            preLexer.addLine(s);
        }

//        preLexer.addLine(input);
//        preLexer.addLine(input1);
//        preLexer.addLine(input2);
//        preLexer.addLine(input3);
//        preLexer.addLine(input4);
//        preLexer.addLine(input5);
//        preLexer.addLine(input6);

        Lexer lexer = new Lexer(preLexer);

        try {
            var output = lexer.tokenize();
            output.forEach(u -> {
                u.getTokens().forEach(v -> {
                    System.out.println(v.getType() + " " + v.getValue());
                });
            });
        } catch (InvalidToken e) {
            System.out.println(e.getMessage());
        }
    }
}
