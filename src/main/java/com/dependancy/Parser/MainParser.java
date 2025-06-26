package com.dependancy.Parser;

import com.dependancy.Parser.AstNodes.DeclarationNode;
import com.dependancy.Parser.AstNodes.FunctionNode;
import com.dependancy.Parser.AstNodes.FunctionNode.FunctionType;
import com.dependancy.Parser.AstNodes.ProgramNode;
import com.dependancy.errors.InvalidSyntax;
import com.dependancy.tokenizer.LineToken;
import com.dependancy.tokenizer.Token;
import com.dependancy.tokenizer.TokenType;

import java.util.*;

public class MainParser {

    public FunctionNode parse(List<LineToken> input) {
        if (input.isEmpty()) {
            throw new InvalidSyntax(1, "function identifier(type name, ...)", "Empty input");
        }

        List<Token> header = input.get(0).getTokens();
        int index = 0;

// Step 1: Return Type
        String returnTypeStr = header.get(index).getValue();

        FunctionType returnType = switch (returnTypeStr) {
            case "void" -> FunctionType.VOID;
            case "int" -> FunctionType.NUMBER;
            case "string" -> FunctionType.STRING;
            case "bool" -> FunctionType.BOOLEAN;
            default -> throw new InvalidSyntax(1, "function identifier(type name, ...)", "Invalid function return type: " + returnTypeStr);
        };

        index++;


        System.out.println(header.get(index).getValue());
        if ((index >= header.size() || header.get(index).getType() != TokenType.IDENTIFIER) && !header.get(index).getValue().equals("main")) {
            throw new InvalidSyntax(1, "function identifier(type name, ...)", "Function name expected");
        }


        String functionName = header.get(index).getValue();
        index++;

        if (index >= header.size() || !header.get(index).getValue().equals("(")) {
            throw new InvalidSyntax(1, "function identifier(type name, ...)", "'(' expected");
        }

        index++;

        // Step 4: Parameters
        Map<DeclarationNode.DataType, String> parameters = new HashMap<>();
        while (index < header.size() && !header.get(index).getValue().equals(")")) {
            // Expect type
            if (index >= header.size() || header.get(index).getType() != TokenType.KEYWORD) {
                throw new InvalidSyntax(1, "function identifier(type name, ...)", "Parameter type expected");
            }

            DeclarationNode.DataType paramType = switch (header.get(index).getValue()) {
                case "int" -> DeclarationNode.DataType.NUMBER;
                case "string" -> DeclarationNode.DataType.STRING;
                case "bool" -> DeclarationNode.DataType.BOOLEAN;
                default -> throw new InvalidSyntax(1, "function identifier(type name, ...)", "Invalid function return type: " + returnTypeStr);
            };

            index++;

            // Expect identifier
            if (index >= header.size() || header.get(index).getType() != TokenType.IDENTIFIER) {
                throw new InvalidSyntax(1, "function identifier(type name, ...)", "Parameter name expected");
            }
            String paramName = header.get(index).getValue();
            parameters.put(paramType, paramName);
            index++;

            // Optional comma
            if (index < header.size() && header.get(index).getValue().equals(",")) {
                index++;
            }
        }

        // Step 5: Closing Parenthesis
        if (index >= header.size() || !header.get(index).getValue().equals(")")) {
            throw new InvalidSyntax(1, "function identifier(type name, ...)", "')' expected");
        }

        index++;

        // Step 6: Opening Brace {
        if (input.size() < 2 || !header.get(index).getValue().equals("{")) {
            throw new InvalidSyntax(2, "{", "'{' expected to start function body");
        }

        List<LineToken> bodyTokens = input.subList(1, input.size()-1); // Assume rest is body

        if (input.getLast().getTokens().size() != 1 || !input.getLast().getTokens().getFirst().getValue().equals("}")) {
            throw new InvalidSyntax(1, "function identifier(type name, ...)", "'}' expected");
        }

        FunctionBodyParser functionBodyParser = new FunctionBodyParser();
        List<ProgramNode> functionBody = functionBodyParser.parse(bodyTokens);

        FunctionNode node = new FunctionNode(1);
        node.parameters = parameters;
        node.functionName = functionName;
        node.returnType = returnType;
        node.body = functionBody;

        return node;
    }
}
