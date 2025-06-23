package com.dependancy.Parser.AstNodes;

public class ExpressionNode extends ProgramNode{


    public enum LiteralType {
        NUMBER, STRING, BOOLEAN
    }

    public enum ExpressionType {
        OPERATOR, IDENTIFIER, LITERAL, PARENTHESIS, EXPRESSION
    }

    public ExpressionType expressionType = ExpressionType.EXPRESSION; //default expression type

    public String literalValue = "";
    public LiteralType literalType = null; //set only when isLiteral = true

    public String identifierValue = null; //set only when isIdentifier = true

    public String operatorValue = null; //set only when isOperator = true

    public ProgramNode innerExpression = null; //set only when isParenthesis = true

    public ExpressionNode(Integer lineNumber) {
        super.nodeType = AstNodeType.EXPRESSION;
        super.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return "expression_node: "+this.expressionType;
    }
}
