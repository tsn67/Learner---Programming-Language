package com.dependancy.SemanticAnalyzer;

import com.dependancy.Parser.AstNodes.*;
import com.dependancy.errors.SemanticError;

public class TypeMismatchChecker {

    public void typeMismatchCheck(ProgramNode node, SymbolTable symbolTable) {

        if (node.nodeType == AstNodeType.ASSIGNMENT) {
            var v = (AssignmentNode) node;
            checker(symbolTable.symbolTable.get(v.identifier), (ExpressionNode) v.right, symbolTable);

        } else if (node.nodeType == AstNodeType.WHILE) {
            var v = (WhileNode) node;
            checker(DeclarationNode.DataType.BOOLEAN, v.condition, symbolTable);

        } else if (node.nodeType == AstNodeType.IF) {
            var v = (IfNode) node;
            checker(DeclarationNode.DataType.BOOLEAN, v.condition, symbolTable);

        } else if (node.nodeType == AstNodeType.DECLARATION && node.right != null) {
            var v = (DeclarationNode) node;
            checker(v.dataType, (ExpressionNode) v.right, symbolTable);

        } else {
            throw new SemanticError(node.lineNumber, "null", "Unexpected statement!");
        }
    }

    public void checker(DeclarationNode.DataType expectedType, ExpressionNode node, SymbolTable symbolTable) {
        DeclarationNode.DataType actualType = evaluateType(node, symbolTable);
        if (actualType != expectedType) {
            throw new SemanticError(node.lineNumber, node.toString(),
                    "Type mismatch: expected " + expectedType + " but found " + actualType);
        }
    }

    private DeclarationNode.DataType evaluateType(ExpressionNode node, SymbolTable symbolTable) {
        switch (node.expressionType) {

            case LITERAL:
                if (node.literalValue.matches("^-?\\d+$")) {
                    return DeclarationNode.DataType.NUMBER;
                }
                if (node.literalValue.equals("true") || node.literalValue.equals("false")) {
                    return DeclarationNode.DataType.BOOLEAN;
                }
                return DeclarationNode.DataType.STRING;

            case IDENTIFIER:
                if (!symbolTable.symbolTable.containsKey(node.identifierValue)) {
                    throw new SemanticError(node.lineNumber, node.identifierValue, "Undeclared variable");
                }
                return symbolTable.symbolTable.get(node.identifierValue);

            case PARENTHESIS:
                if (node.innerExpression == null) {
                    throw new SemanticError(node.lineNumber, node.toString(), "Empty parenthesized expression");
                }
                return evaluateType((ExpressionNode) node.innerExpression, symbolTable);

            case OPERATOR:
                String op = node.operatorValue;

                // Handle unary 'not'
                if (op.equals("not")) {
                    if (node.left == null) {
                        throw new SemanticError(node.lineNumber, node.toString(),
                                "Unary operator 'not' requires one BOOLEAN operand");
                    }

                    DeclarationNode.DataType operandType = evaluateType((ExpressionNode) node.left, symbolTable);

                    if (operandType != DeclarationNode.DataType.BOOLEAN) {
                        throw new SemanticError(node.lineNumber, node.toString(),
                                "'not' operator requires a BOOLEAN operand. Got " + operandType);
                    }

                    return DeclarationNode.DataType.BOOLEAN;
                }

                // All other binary operators
                DeclarationNode.DataType leftType = evaluateType((ExpressionNode) node.left, symbolTable);
                DeclarationNode.DataType rightType = evaluateType((ExpressionNode) node.right, symbolTable);

                switch (op) {
                    case "==":
                    case "<":
                    case ">":
                    case "<=":
                    case ">=":
                        if (leftType != DeclarationNode.DataType.NUMBER || rightType != DeclarationNode.DataType.NUMBER) {
                            throw new SemanticError(node.lineNumber, node.toString(),
                                    "Relational operator '" + op + "' requires NUMBER operands but got " + leftType + " and " + rightType);
                        }
                        return DeclarationNode.DataType.BOOLEAN;

                    case "+":
                        if (leftType == DeclarationNode.DataType.NUMBER && rightType == DeclarationNode.DataType.NUMBER) {
                            return DeclarationNode.DataType.NUMBER;
                        } else if (leftType == DeclarationNode.DataType.STRING && rightType == DeclarationNode.DataType.STRING) {
                            return DeclarationNode.DataType.STRING;
                        } else {
                            throw new SemanticError(node.lineNumber, node.toString(),
                                    "Operator '+' requires both operands to be either NUMBER or STRING. Got " + leftType + " and " + rightType);
                        }

                    case "-":
                    case "*":
                    case "/":
                        if (leftType == DeclarationNode.DataType.NUMBER && rightType == DeclarationNode.DataType.NUMBER) {
                            return DeclarationNode.DataType.NUMBER;
                        } else {
                            throw new SemanticError(node.lineNumber, node.toString(),
                                    "Arithmetic operator '" + op + "' requires NUMBER operands. Got " + leftType + " and " + rightType);
                        }

                    case "and":
                    case "or":
                    case "xor":
                        if (leftType == DeclarationNode.DataType.BOOLEAN && rightType == DeclarationNode.DataType.BOOLEAN) {
                            return DeclarationNode.DataType.BOOLEAN;
                        } else {
                            throw new SemanticError(node.lineNumber, node.toString(),
                                    "Boolean operator '" + op + "' requires BOOLEAN operands. Got " + leftType + " and " + rightType);
                        }

                    default:
                        throw new SemanticError(node.lineNumber, node.toString(), "Unknown operator '" + op + "'");
                }


            default:
                throw new SemanticError(node.lineNumber, node.toString(),
                        "Unsupported expression type: " + node.expressionType);
        }
    }
}
