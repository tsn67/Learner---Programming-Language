package com.dependancy.Parser;

import com.dependancy.Parser.AstNodes.ProgramNode;
import com.dependancy.tokenizer.LineToken;

import java.util.ArrayList;
import java.util.List;

public class FunctionBodyParser {

    List<ProgramNode> functionBody = new ArrayList<>();
    private final ConditionalStatementParser conditionalStatementParser = new ConditionalStatementParser();

    public List<ProgramNode> parse(List<LineToken> input) {
        conditionalStatementParser.input = input;
        conditionalStatementParser.index = 0;

        Integer lineNumber = 2;

        while (conditionalStatementParser.index < input.size()) {
            conditionalStatementParser.lineNumber = lineNumber;
            ProgramNode stmt = conditionalStatementParser.parse();
            lineNumber = conditionalStatementParser.lineNumber;
            functionBody.add(stmt);
        }

        return functionBody;
    }
}
