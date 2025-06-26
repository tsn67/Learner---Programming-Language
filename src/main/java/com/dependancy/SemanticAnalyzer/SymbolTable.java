package com.dependancy.SemanticAnalyzer;

import com.dependancy.Parser.AstNodes.DeclarationNode;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    public Map<String, DeclarationNode.DataType> symbolTable;

    public SymbolTable() {
        symbolTable = new HashMap<>();
    }

    public SymbolTable(Map<String, DeclarationNode.DataType> symbolTable) {
        this.symbolTable = symbolTable;
    }

    public boolean contains(String identifier){
        return this.symbolTable.containsKey(identifier);
    }

    public void addNewSymbol(String identifier, DeclarationNode.DataType dataType){
        this.symbolTable.put(identifier, dataType);
    }
}
