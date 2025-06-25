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

        while (conditionalStatementParser.index < input.size()) {
            ProgramNode stmt = conditionalStatementParser.parse();
            functionBody.add(stmt);
        }

        return functionBody;
    }
}
