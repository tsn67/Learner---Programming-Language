package com.dependancy.SemanticAnalyzer;

import com.dependancy.Parser.AstNodes.*;
import com.dependancy.errors.SemanticError;

import java.util.HashMap;

public class DuplicateDeclarationChecker {

    private final TypeMismatchChecker typeMismatchChecker = new TypeMismatchChecker();

    public void checkDuplicateDeclarations(SymbolTable symbolTable, ProgramNode node) {

        typeMismatchChecker.typeMismatchCheck(node, symbolTable);

        if (node.nodeType == AstNodeType.DECLARATION) {
            DeclarationNode decl = (DeclarationNode) node;
            if (symbolTable.contains(decl.identifier)) {
                throw new SemanticError(decl.lineNumber, "duplicate declaration",
                        "identifier: " + decl.identifier + " has duplicate declaration!");
            } else {
                symbolTable.addNewSymbol(decl.identifier, decl.dataType);
            }
        }

        else if (node.nodeType == AstNodeType.WHILE) {
            WhileNode whileNode = (WhileNode) node;
            SymbolTable loopScope = new SymbolTable(new HashMap<>(symbolTable.symbolTable));
            for (ProgramNode stmt : whileNode.body) {
                checkDuplicateDeclarations(loopScope, stmt);
            }
        }

        else if (node.nodeType == AstNodeType.IF) {
            IfNode ifNode = (IfNode) node;

            SymbolTable ifScope = new SymbolTable(new HashMap<>(symbolTable.symbolTable));
            for (ProgramNode stmt : ifNode.body) {
                checkDuplicateDeclarations(ifScope, stmt);
            }

            if (ifNode.elseNode != null) {
                SymbolTable elseScope = new SymbolTable(new HashMap<>(symbolTable.symbolTable));
                for (ProgramNode stmt : ((IfNode) ifNode.elseNode).body) {
                    checkDuplicateDeclarations(elseScope, stmt);
                }
            }
        }
    }

    public void undeclaredVariableCheck(SymbolTable symbolTable, ProgramNode node) {
        if (node.nodeType == AstNodeType.DECLARATION) {
            DeclarationNode decl = (DeclarationNode) node;
            if (decl.right != null) {
                // Create a copy and remove current variable to avoid false positive usage in its own RHS
                SymbolTable inner = new SymbolTable(new HashMap<>(symbolTable.symbolTable));
                inner.symbolTable.remove(decl.identifier);
                checkExpressionIdentifiers(inner, (ExpressionNode) decl.right);
            }
            symbolTable.addNewSymbol(decl.identifier, decl.dataType);
        }

        else if (node.nodeType == AstNodeType.ASSIGNMENT) {
            AssignmentNode assign = (AssignmentNode) node;
            if (!symbolTable.contains(assign.identifier)) {
                throw new SemanticError(assign.lineNumber, "undeclared variable",
                        "identifier: " + assign.identifier + " is not declared.");
            }
            checkExpressionIdentifiers(symbolTable, (ExpressionNode) assign.right);
        }

        else if (node.nodeType == AstNodeType.IF) {
            IfNode ifNode = (IfNode) node;
            checkExpressionIdentifiers(symbolTable, ifNode.condition);

            SymbolTable ifScope = new SymbolTable(new HashMap<>(symbolTable.symbolTable));
            for (ProgramNode stmt : ifNode.body) {
                undeclaredVariableCheck(ifScope, stmt);
            }

            if (ifNode.elseNode != null) {
                SymbolTable elseScope = new SymbolTable(new HashMap<>(symbolTable.symbolTable));
                for (ProgramNode stmt : ((IfNode) ifNode.elseNode).body) {
                    undeclaredVariableCheck(elseScope, stmt);
                }
            }
        }

        else if (node.nodeType == AstNodeType.WHILE) {
            WhileNode whileNode = (WhileNode) node;
            checkExpressionIdentifiers(symbolTable, whileNode.condition);

            SymbolTable whileScope = new SymbolTable(new HashMap<>(symbolTable.symbolTable));
            for (ProgramNode stmt : whileNode.body) {
                undeclaredVariableCheck(whileScope, stmt);
            }
        }
    }

    private void checkExpressionIdentifiers(SymbolTable symbolTable, ExpressionNode expr) {
        if (expr == null) return;

        if (expr.nodeType == AstNodeType.EXPRESSION &&
                expr.expressionType == ExpressionNode.ExpressionType.IDENTIFIER) {

            if (!symbolTable.contains(expr.identifierValue)) {
                throw new SemanticError(expr.lineNumber, "undeclared variable",
                        "identifier: " + expr.identifierValue + " is not declared.");
            }
        }

        else if (expr.nodeType == AstNodeType.EXPRESSION &&
                expr.expressionType == ExpressionNode.ExpressionType.OPERATOR) {

            checkExpressionIdentifiers(symbolTable, (ExpressionNode) expr.left);
            checkExpressionIdentifiers(symbolTable, (ExpressionNode) expr.right);
        }
    }
}
