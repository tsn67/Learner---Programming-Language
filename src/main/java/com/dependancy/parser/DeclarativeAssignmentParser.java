package com.dependancy.parser;

import com.dependancy.errors.InvalidSyntax;
import com.dependancy.parser.AST_nodes.*;
import com.dependancy.tokenizer.Token;
import com.dependancy.tokenizer.TokenType;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class DeclarativeAssignmentParser {

    private List<Token> lineTokens;
    private int position = 0;
    private int lineNumber = 0;

    public DeclarativeAssignmentParser() {

    }


    private boolean advance() {
        if (position < this.lineTokens.size()-1) {
            position++;
            return true;
        }
        return false;
    }

    private Token current() {
        if (position < this.lineTokens.size()) {
            return lineTokens.get(position);
        } else return null;
    }

    public StatementNode parse() throws InvalidSyntax{

        String[] dataTypes = {"int", "string", "bool"};
        StatementNode root = null;
        if(Arrays.stream(dataTypes).toList().contains(current().getValue())) {
            root = declarativeParse();
        } else if (current().getType().equals(TokenType.IDENTIFIER)) {
            root = assignmentParse();
        } else {
            throw new InvalidSyntax(this.lineNumber, current().getValue(), "Invalid program statement!");
        }

        return root;
    }

    public StatementNode declarativeParse(){
        DeclarativeNode declarativeNode = new DeclarativeNode(LiteralNode.LiteralType.valueOf(current().getValue().toUpperCase()), null);

        if(advance() && current().getType().equals(TokenType.IDENTIFIER)) {
            declarativeNode.setIdentifier(current().getValue());
        } else {
            throw new InvalidSyntax(this.lineNumber, current().getValue(), "Invalid variable declaration!");
        }
        if (advance()) {
            if (!(current().getType().equals(TokenType.OPERATOR) && current().getValue().equals("="))) {
                throw new InvalidSyntax(this.lineNumber, current().getValue(), "Invalid variable declaration!");
            } else {
                advance(); //overcome =
            }
            declarativeNode.setRightChild(expressionParse());
        } else {
            return declarativeNode;
        }

        return declarativeNode;
    }

    public ExpressionNode expressionParse() {
        ExpressionNode termNode = termParse();
        return expressionTailParse(termNode);
    }

    private ExpressionNode expressionTailParse(ExpressionNode left) {
        if (advance() && current().getType().equals(TokenType.OPERATOR)) {
            ExpressionNode node = new ExpressionNode();
            node.setOperator(current().getValue());
            node.setLeftChild(left);
            if (advance()) {
                ExpressionNode right = termParse();
                node.setRightChild(expressionTailParse(right)); // right-associative chaining
                return node;
            } else {
                throw new InvalidSyntax(lineNumber, "empty", "Expected term after operator: " + current().getValue());
            }
        }

        // No more operator? Backtrack position (because advance was called before check)
        position--;
        return left; // ε production
    }


    private ExpressionNode termParse() {
        Token token = current();

        if (token.getType().equals(TokenType.IDENTIFIER)) {
            ExpressionNode node = new ExpressionNode();
            node.setIdentifier(token.getValue());
            return node;
        }

        if (token.getType().equals(TokenType.NUMBER) || token.getType().equals(TokenType.STRING)|| token.getType().equals(TokenType.BOOLEAN)) {
            ExpressionNode node = new ExpressionNode();
            if (token.getType().equals(TokenType.NUMBER)) {
                node.setLiteralType(LiteralNode.LiteralType.INT);
                node.setLiteralValue(token.getValue());
            } else if (token.getType().equals(TokenType.STRING)) {
                node.setLiteralType(LiteralNode.LiteralType.STRING);
                node.setLiteralValue(token.getValue());
            } else if (token.getType().equals(TokenType.BOOLEAN)) {
                node.setLiteralType(LiteralNode.LiteralType.BOOLEAN);
                node.setLiteralValue(token.getValue());
            }
            return node;
        }

        if (token.getType().equals(TokenType.SEPARATOR) && token.getValue().equals("(")) {
            if (advance()) {
                ExpressionNode inner = expressionParse();
                if (advance() && current().getType().equals(TokenType.SEPARATOR) && current().getValue().equals(")")) {
                    return inner;
                } else {
                    throw  new InvalidSyntax(lineNumber, current().getValue(), "Expected closing parenthesis: " + current().getValue());
                }
            }
        }

        throw new InvalidSyntax(lineNumber, current().getValue(), "Unexpected token in term: " + token.getValue());
    }

    public StatementNode assignmentParse(){
        return null;
    }
}
