package com.dependancy.Parser;

import com.dependancy.Parser.AstNodes.ExpressionNode;
import com.dependancy.Parser.AstNodes.IfNode;
import com.dependancy.Parser.AstNodes.ProgramNode;
import com.dependancy.Parser.AstNodes.WhileNode;
import com.dependancy.errors.InvalidSyntax;
import com.dependancy.tokenizer.LineToken;
import com.dependancy.tokenizer.Token;

import java.util.ArrayList;
import java.util.List;

public class ConditionalStatementParser {

    List<LineToken> input;
    public int index = 0;
    int tokenIndex = 0;
    Integer lineNumber;
    private final SingleStatementParser singleStatementParser = new SingleStatementParser();

    public ConditionalStatementParser() {}

    public Token getCurrentToken() {
        while (index < input.size()) {
            List<Token> tokens = input.get(index).getTokens();
            if (tokenIndex < tokens.size()) {
                return tokens.get(tokenIndex);
            }
            index++;
            tokenIndex = 0;
        }
        return null;
    }

    public void advanceToNextToken() {
        tokenIndex++;
        while (index < input.size()) {
            List<Token> tokens = input.get(index).getTokens();
            if (tokenIndex < tokens.size()) {
                break;
            }
            index++;
            tokenIndex = 0;
        }
    }

    public ProgramNode parse() {
        Token current = getCurrentToken();
        if (current == null) return null;

        if (current.getValue().equals("while")) {
            advanceToNextToken();
            return whileParse();
        } else if (current.getValue().equals("if")) {
            advanceToNextToken();
            return ifParse();
        } else {
            ProgramNode node = singleStatementParser.parse(input.get(index).getTokens(), lineNumber);
            index++;
            tokenIndex = 0;
            return node;
        }
    }

    private ProgramNode whileParse() {
        WhileNode node = new WhileNode(lineNumber);

        // Expect '('
        Token token = getCurrentToken();
        if (token == null || !token.getValue().equals("(")) {
            throw new InvalidSyntax(lineNumber, "while ( condition )", "Expected '(' after 'while'");
        }
        advanceToNextToken();

        // Parse condition
        List<Token> conditionTokens = new ArrayList<>();
        while (getCurrentToken() != null && !getCurrentToken().getValue().equals(")")) {
            conditionTokens.add(getCurrentToken());
            advanceToNextToken();
        }

        if (getCurrentToken() == null || !getCurrentToken().getValue().equals(")")) {
            throw new InvalidSyntax(lineNumber, "while ( condition )", "Missing closing ')'");
        }
        advanceToNextToken();

        node.condition = (ExpressionNode) singleStatementParser.parse(conditionTokens, lineNumber);

        // Expect '{' in new line
        if (getCurrentToken() == null || !getCurrentToken().getValue().equals("{")) {
            throw new InvalidSyntax(lineNumber, "while (condition) {", "Expected '{' after while condition");
        }
        advanceToNextToken();

        // Parse body
        node.body = parseBody();
        return node;
    }

    private ProgramNode ifParse() {
        IfNode ifNode = new IfNode(lineNumber);

        // Expect '('
        if (getCurrentToken() == null || !getCurrentToken().getValue().equals("(")) {
            throw new InvalidSyntax(lineNumber, "if ( condition )", "Expected '(' after 'if'");
        }
        advanceToNextToken();

        // Parse condition
        List<Token> conditionTokens = new ArrayList<>();
        while (getCurrentToken() != null && !getCurrentToken().getValue().equals(")")) {
            conditionTokens.add(getCurrentToken());
            advanceToNextToken();
        }

        if (getCurrentToken() == null || !getCurrentToken().getValue().equals(")")) {
            throw new InvalidSyntax(lineNumber, "if ( condition )", "Missing closing ')'");
        }
        advanceToNextToken();

        ifNode.condition = (ExpressionNode) singleStatementParser.parse(conditionTokens, lineNumber);

        // Expect '{'
        if (getCurrentToken() == null || !getCurrentToken().getValue().equals("{")) {
            throw new InvalidSyntax(lineNumber, "if ( condition ) {", "Expected '{' after condition");
        }
        advanceToNextToken();

        // Parse if body
        ifNode.body = parseBody();

        // Check next line for 'else'
        if (getCurrentToken() != null && getCurrentToken().getValue().equals("else")) {
            advanceToNextToken();
            if (getCurrentToken() == null || !getCurrentToken().getValue().equals("{")) {
                throw new InvalidSyntax(lineNumber, "else {", "Expected '{' after 'else'");
            }
            advanceToNextToken();

            IfNode elseNode = new IfNode(lineNumber);
            elseNode.body = parseBody();
            ifNode.elseNode = elseNode;
        }

        return ifNode;
    }

    private List<ProgramNode> parseBody() {
        List<ProgramNode> body = new ArrayList<>();
        int braceCount = 1;

        while (index < input.size()) {
            Token token = getCurrentToken();
            if (token == null) {
                advanceToNextToken();
                continue;
            }

            if (token.getValue().equals("{")) {
                braceCount++;
                advanceToNextToken();
                continue;
            }

            if (token.getValue().equals("}")) {
                braceCount--;
                advanceToNextToken();
                if (braceCount == 0) break;
                continue;
            }

            ProgramNode stmt = parse();
            if (stmt != null) body.add(stmt);
        }

        if (braceCount != 0) {
            throw new InvalidSyntax(lineNumber, "{ ... }", "Unmatched braces in block");
        }

        return body;
    }
}
