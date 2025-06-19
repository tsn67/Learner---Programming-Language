package com.dependancy.parser.AST_nodes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpressionNode extends StatementNode {

    private String operator = null; //should be added
    private String identifier = null;
    private String literalValue = null;
    private LiteralNode.LiteralType literalType = null;
    private boolean isSeperated = false; //seperated by (expression)

    public ExpressionNode(){
        super.setNodeType(ProgramNodeType.EXPRESSION);
        super.setLeftChild(null);
        super.setRightChild(null);
    }


    public String toString(){
        if (this.identifier != null) {
            return "identifier: "+this.identifier;
        } else if (this.literalValue != null) {
            return "literal: "+this.literalValue;
        } else {
            return "operator: "+this.operator;
        }
    }
}
