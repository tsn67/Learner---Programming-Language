package com.dependancy;

import com.dependancy.Parser.AstNodes.FunctionNode;
import com.dependancy.Parser.AstNodes.ProgramNode;
import com.dependancy.Parser.FunctionBodyParser;
import com.dependancy.Parser.MainParser;
import com.dependancy.errors.InvalidSyntax;
import com.dependancy.errors.InvalidToken;
import com.dependancy.inputpreprocessor.PreLexer;
import com.dependancy.tokenizer.Lexer;
import test.AstTester;
import test.AstVisualizer;

import java.util.List;


public class Main {


    public static void main(String[] args) {

        var inputProgram = "void main(int a, int b, bool c) {\n" +
                "    int a = 10\n" +
                "    int b = 20\n" +
                "    int c = a + b\n" +
                "    while ( 1 + 2 < 4) {\n" +
                "        c = c + 1\n" +
                "        temp = (tesmp * sdf) + 1 + (sdfsf * 2 )\n" +
                "        c = c + 1\n" +
                "    }\n" +
                "    if (t < 4) {\n" +
                "        c = c + 1\n" +
                "    }\n" +
                "    else {\n" +
                "        d = d + 4\n" +
                "    }\n" +
                "    c = c + 1\n" +
                "}\n";

        PreLexer preLexer = new PreLexer();

        String[] inputs = inputProgram.split("\n");
        for(String s : inputs){
            preLexer.addLine(s);
        }


        Lexer lexer = new Lexer(preLexer);


        try {
            var output = lexer.tokenize();
            output.forEach(u -> {
                u.getTokens().forEach(v -> {
                    System.out.println(v.getType() + " " + v.getValue());
                });
            });

            System.out.println("\n\n\n====Parser====\n");

            MainParser mainParser = new MainParser();
            FunctionNode node = mainParser.parse(output);

//            ProgramNode parserResultTreeRoot = new SingleStatementParser().parse(output.get(0).getTokens(), 1);

            AstTester tester = new AstTester();
            tester.report(node);

            System.out.println("\n\n\n====AST Visualizer====\n");
            AstVisualizer astVisualizer = new AstVisualizer();
            astVisualizer.exportToDotFile("ast.dot", node);
            //convert the ast.dot file to .png (image) using the command provided
            // 'dot -Tpng ast.dot -o ast.png'
            // install dot from 'graphnize'


        } catch (InvalidToken e) {
            System.out.println(e.getMessage());
        } catch (InvalidSyntax e) {
            System.out.println(e.getDetailedInfo());
        }
    }
}
