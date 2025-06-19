package com.dependancy.parser.AST_nodes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentNode extends ProgramNode{

    //assignment node left must be a identifier node (variable)
    private final String identifier;

    public AssignmentNode(String identifier){
        super.setNodeType(ProgramNodeType.ASSIGNMENT);
        super.setLeftChild(null); //left child will always null
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return " identifier = "+this.identifier+" value: "+super.getRightChild().toString();
    }
}
