package com.dependancy.parser.AST_nodes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeclarativeNode extends ProgramNode{

    private LiteralNode.LiteralType dataType;
    private String identifier;
    //left child will always null
    //if right child exists it will be a expression node

    public DeclarativeNode(LiteralNode.LiteralType dataType, String identifier){
        super.setNodeType(ProgramNodeType.DECLARATION);
        super.setLeftChild(null);
        this.dataType = dataType;
        this.setDefaultValue();
        this.identifier = identifier;
    }

    private void setDefaultValue() {
        LiteralNode literalNode = new LiteralNode();
        literalNode.setType(dataType);
        if (dataType == LiteralNode.LiteralType.INT) {
            literalNode.setValue("0");
        } else if (dataType == LiteralNode.LiteralType.BOOLEAN) {
            literalNode.setValue("false");
        } else {
            literalNode.setValue("\"\"");
        }
        super.setRightChild(literalNode);
    }

    @Override
    public String toString() {
        return "Type: "+this.dataType+" identifier: "+this.identifier+" value: "+super.getRightChild().toString();
    }
}
