package com.dependancy;

import com.dependancy.Parser.AstNodes.ProgramNode;
import com.dependancy.Parser.SingleStatementParser;
import com.dependancy.errors.InvalidSyntax;
import com.dependancy.errors.InvalidToken;
import com.dependancy.inputpreprocessor.PreLexer;
import com.dependancy.tokenizer.Lexer;
import test.AstTester;
import test.AstVisualizer;


public class Main {


    public static void main(String[] args) {

        var inputProgram = "int sum = (2 * 10) / 100 + (10 - 3) * 2 * (20 * 40) + hello";

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

            ProgramNode parserResultTreeRoot = new SingleStatementParser().parse(output.get(0).getTokens(), 1);

            AstTester tester = new AstTester();
            tester.report(parserResultTreeRoot);

            System.out.println("\n\n\n====AST Visualizer====\n");
            AstVisualizer astVisualizer = new AstVisualizer();
            astVisualizer.exportToDotFile("ast.dot", parserResultTreeRoot);
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
