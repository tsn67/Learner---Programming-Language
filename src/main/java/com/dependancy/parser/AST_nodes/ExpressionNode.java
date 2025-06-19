package com.dependancy.parser.AST_nodes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpressionNode extends ProgramNode{

    private String operator = null; //should be added

    public ExpressionNode(){
        super.setNodeType(ProgramNodeType.EXPRESSION);
        super.setLeftChild(null);
        super.setRightChild(null);
    }

    public void addLeftChild(ProgramNode node) {
        super.setLeftChild(node);
    }

    public void addRightChild(ProgramNode node) {
        super.setRightChild(node);
    }

    public String toString(){
        return "Expression: "+"("+super.getLeftChild().toString()+")"+operator+"("+super.getRightChild().toString()+")";
    }
}
