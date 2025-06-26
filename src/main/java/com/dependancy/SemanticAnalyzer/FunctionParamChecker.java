package com.dependancy.SemanticAnalyzer;

import com.dependancy.Parser.AstNodes.FunctionNode;
import com.dependancy.errors.SemanticError;

public class FunctionParamChecker {

    public SymbolTable checkFunctionParams(FunctionNode functionNode) {

        SymbolTable symbolTable = new SymbolTable();

        functionNode.parameters.forEach((dataType, identifier) -> {
            if (symbolTable.contains(identifier)) {
                throw new SemanticError(functionNode.lineNumber, "duplicate function parameter", "identifier: " + identifier + " has duplicate!");
            }
            symbolTable.addNewSymbol(identifier, dataType);
        });

        return symbolTable;
    }
}
