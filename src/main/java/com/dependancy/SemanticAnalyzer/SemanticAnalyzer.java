package com.dependancy.SemanticAnalyzer;

import com.dependancy.Parser.AstNodes.FunctionNode;

public class SemanticAnalyzer {

    private final DuplicateDeclarationChecker duplicateDeclarationChecker = new DuplicateDeclarationChecker();

    public void semanticAnalysis(FunctionNode functionNode){

        FunctionParamChecker functionParamChecker = new FunctionParamChecker();
        SymbolTable symbolTable = functionParamChecker.checkFunctionParams(functionNode);


        functionNode.body.forEach(node -> {
           duplicateDeclarationChecker.checkDuplicateDeclarations(symbolTable, node);
            duplicateDeclarationChecker.undeclaredVariableCheck(symbolTable, node);
        });

    }
}
