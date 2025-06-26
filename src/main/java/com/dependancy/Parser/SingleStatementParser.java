package com.dependancy.Parser;

import com.dependancy.Parser.AstNodes.AssignmentNode;
import com.dependancy.Parser.AstNodes.DeclarationNode;
import com.dependancy.Parser.AstNodes.ExpressionNode;
import com.dependancy.Parser.AstNodes.ProgramNode;
import com.dependancy.errors.InvalidSyntax;
import com.dependancy.tokenizer.Token;
import com.dependancy.tokenizer.TokenType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SingleStatementParser {

    private List<Token> tokens;
    private int index = 0;
    private Integer lineNumber;

    private Token getCurrentToken() {
        return index < tokens.size() ? tokens.get(index) : null;
    }

    private boolean outOfTokens() {
        return index >= tokens.size();
    }

    public ProgramNode parse(List<Token> tokens, Integer lineNumber) {
        this.tokens = tokens;
        this.lineNumber = lineNumber;
        this.index = 0;

        if (outOfTokens()) {
            throw new InvalidSyntax(this.lineNumber, "EOF", "Empty or invalid statement");
        }

        String[] datatypes = { "string", "int", "bool" };

        if (Arrays.asList(datatypes).contains(getCurrentToken().getValue())) {
            return parseDeclaration();
        }

        try {
            if (getCurrentToken().getType() == TokenType.IDENTIFIER &&
                    index + 1 < tokens.size() &&
                    tokens.get(index + 1).getType() == TokenType.OPERATOR &&
                    tokens.get(index + 1).getValue().equals("=")) {
                return parseAssignment();
            }
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            throw new InvalidSyntax(lineNumber, "EOF", "Unexpected end of input");
        }

        return parseExpression();
    }

    private ProgramNode parseExpression() {
        return parseBinaryExpression(0);
    }

    // Operator precedence from high to low
    private static final List<List<String>> OPERATOR_PRECEDENCE = List.of(
            List.of("*", "/", "%"),
            List.of("+", "-"),
            List.of("==", "!=", "<", ">", "<=", ">="),
            List.of("and"),
            List.of("or", "xor")
    );

    private int getPrecedence(String operator) {
        for (int i = 0; i < OPERATOR_PRECEDENCE.size(); i++) {
            if (OPERATOR_PRECEDENCE.get(i).contains(operator)) return i;
        }
        return -1;
    }

    private ProgramNode parseBinaryExpression(int minPrecedence) {
        ProgramNode left = parseTerm();

        while (!outOfTokens() && getCurrentToken().getType() == TokenType.OPERATOR) {
            String op = getCurrentToken().getValue();
            int precedence = getPrecedence(op);
            if (precedence < minPrecedence) break;

            index++; // consume operator

            ProgramNode right = parseBinaryExpression(precedence + 1);

            ExpressionNode operatorNode = new ExpressionNode(lineNumber);
            operatorNode.expressionType = ExpressionNode.ExpressionType.OPERATOR;
            operatorNode.operatorValue = op;
            operatorNode.left = left;
            operatorNode.right = right;

            left = operatorNode;
        }

        return left;
    }

    private ProgramNode parseTerm() {
        Token token = getCurrentToken();
        if (token == null) throw new InvalidSyntax(lineNumber, "EOF", "Unexpected end of input");

        // Handle unary 'not'
        if (token.getType() == TokenType.OPERATOR && token.getValue().equals("not")) {
            index++; // consume 'not'
            ExpressionNode node = new ExpressionNode(lineNumber);
            node.expressionType = ExpressionNode.ExpressionType.OPERATOR;
            node.operatorValue = "not";
            node.left = parseTerm(); // only left used in unary
            node.right = null;
            return node;
        }

        ExpressionNode node = new ExpressionNode(lineNumber);

        switch (token.getType()) {
            case IDENTIFIER -> {
                node.expressionType = ExpressionNode.ExpressionType.IDENTIFIER;
                node.identifierValue = token.getValue();
                index++;
            }

            case NUMBER, STRING, BOOLEAN -> {
                node.expressionType = ExpressionNode.ExpressionType.LITERAL;
                node.literalValue = token.getValue();
                node.literalType = switch (token.getType()) {
                    case NUMBER -> ExpressionNode.LiteralType.NUMBER;
                    case STRING -> ExpressionNode.LiteralType.STRING;
                    case BOOLEAN -> ExpressionNode.LiteralType.BOOLEAN;
                    default -> throw new IllegalStateException("Unexpected literal type");
                };
                index++;
            }

            case SEPARATOR -> {
                if (token.getValue().equals("(")) {
                    index++; // consume '('
                    node.expressionType = ExpressionNode.ExpressionType.PARENTHESIS;
                    node.innerExpression = parseExpression();

                    if (outOfTokens() || !getCurrentToken().getValue().equals(")")) {
                        throw new InvalidSyntax(lineNumber, token.getValue(), "Expected closing ')'");
                    }
                    index++; // consume ')'
                    return node;
                } else {
                    throw new InvalidSyntax(lineNumber, token.getValue(), "Unexpected separator");
                }
            }

            default -> throw new InvalidSyntax(lineNumber, token.getValue(), "Unexpected token in expression");
        }

        return node;
    }

    private ProgramNode parseAssignment() {
        AssignmentNode node = new AssignmentNode(lineNumber);

        node.identifier = getCurrentToken().getValue();
        index++;

        if (outOfTokens() || !getCurrentToken().getValue().equals("=")) {
            throw new InvalidSyntax(lineNumber, tokens.get(index - 1).getValue(), "Expected '=' (assignment)");
        }
        index++;

        node.right = parseExpression();

        if (!outOfTokens()) {
            throw new InvalidSyntax(lineNumber, tokens.get(index).getValue(), "Invalid assignment!");
        }
        return node;
    }

    private ProgramNode parseDeclaration() {
        DeclarationNode node = new DeclarationNode(lineNumber);

        Map<String, DeclarationNode.DataType> dataTypes = new java.util.HashMap<>(Map.of("string", DeclarationNode.DataType.STRING));
        dataTypes.put("bool", DeclarationNode.DataType.BOOLEAN);
        dataTypes.put("int", DeclarationNode.DataType.NUMBER);

        node.dataType = dataTypes.get(getCurrentToken().getValue());

        index++;
        if (outOfTokens() || getCurrentToken().getType() != TokenType.IDENTIFIER) {
            throw new InvalidSyntax(lineNumber, tokens.get(index - 1).getValue(), "Expected identifier (declaration)");
        }
        node.identifier = getCurrentToken().getValue();
        index++;

        if (outOfTokens() || getCurrentToken().getType() != TokenType.OPERATOR || !getCurrentToken().getValue().equals("=")) {
            throw new InvalidSyntax(lineNumber, tokens.get(index - 1).getValue(), "Expected '=' (declaration)");
        }
        index++;

        node.right = parseExpression();
        return node;
    }
}
