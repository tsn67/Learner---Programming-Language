package com.dependancy;

import com.dependancy.errors.InvalidSyntax;
import com.dependancy.errors.InvalidToken;
import com.dependancy.inputpreprocessor.PreLexer;
import com.dependancy.parser.AST_nodes.StatementNode;
import com.dependancy.parser.DeclarativeAssignmentParser;
import com.dependancy.tokenizer.Lexer;

import java.util.ArrayList;
import java.util.List;

public class Main {

    int count = 0;
    List<StatementNode> nodes = new ArrayList<>();

    private void helper(StatementNode node) {
        count++;
        if(node.getLeftChild() != null){
            helper(node.getLeftChild());
        }
        if(node.getRightChild() != null){
            helper(node.getRightChild());
        }
    }

    private void traverse(StatementNode node){
        this.nodes.add(node);
        if (node.getLeftChild() != null){
            traverse(node.getLeftChild());
        }
        if (node.getRightChild() != null){
            traverse(node.getRightChild());
        }
    }

    public static void main(String[] args) {

        var input1 = "int 2323";
        var input2 = "a = 2 + a";
        var input3 = "int b = 2 * 3 + a";

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

//        String[] inputs = inputProgram.split("\n");
//        for(String s : inputs){
//            preLexer.addLine(s);
//        }

        preLexer.addLine(input1);
        preLexer.addLine(input2);
        preLexer.addLine(input3);

        Lexer lexer = new Lexer(preLexer);
        DeclarativeAssignmentParser parser1 = new DeclarativeAssignmentParser();



        try {
            var output = lexer.tokenize();
            output.forEach(u -> {
                u.getTokens().forEach(v -> {
                    System.out.println(v.getType() + " " + v.getValue());
                });
            });

            parser1.setLineTokens(output.getFirst().getTokens());
            parser1.setLineNumber(1);
            StatementNode testNode = parser1.parse();

            Main main = new Main();
            main.helper(testNode);
            System.out.println("Total number of nodes: " + main.count);

            main.traverse(testNode);

            System.out.println("\n\n\n");
            main.nodes.forEach(node -> {
                System.out.println(node.toString());
            });

        } catch (InvalidToken e) {
            System.out.println(e.getMessage());
        } catch (InvalidSyntax e) {
            System.out.println(e.getDetailedInfo());
        }
    }
}
