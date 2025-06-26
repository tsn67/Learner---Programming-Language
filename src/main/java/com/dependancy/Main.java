package com.dependancy;

import com.dependancy.Parser.AstNodes.FunctionNode;
import com.dependancy.Parser.MainParser;
import com.dependancy.SemanticAnalyzer.SemanticAnalyzer;
import com.dependancy.errors.InvalidSyntax;
import com.dependancy.errors.InvalidToken;
import com.dependancy.errors.SemanticError;
import com.dependancy.inputpreprocessor.PreLexer;
import com.dependancy.tokenizer.Lexer;
import test.AstTester;
import test.AstVisualizer;

public class Main {

    public static void main(String[] args) {

        var inputProgram = "int main(int test, bool isValid, string greeting) {\n" +
                "string temp = greeting + \"hello\"\n" +
                "if (not true and false) {\n" +
                "    temp = \"10\"\n" +
                "    greeting = \"Hello World\"\n" +
                "}\n" +
                "else {\n" +
                "    greeting = \"Goodbye World\"\n" +
                "}\n" +
                "int n = 20 * (10 + 20)\n" +
                "isValid = (true) and (not false)\n" +
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



            System.out.println("\n\n\n====Semantic Analyzer====\n");
            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();

            semanticAnalyzer.semanticAnalysis(node);



        } catch (InvalidToken e) {
            System.out.println(e.getMessage());
        } catch (InvalidSyntax e) {
            System.out.println(e.getDetailedInfo());
        } catch (SemanticError e) {
            System.out.println(e.getDetailedInfo());
        }
    }
}
