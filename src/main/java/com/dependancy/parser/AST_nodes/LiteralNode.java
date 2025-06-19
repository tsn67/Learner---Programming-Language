package com.dependancy.parser.AST_nodes;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LiteralNode extends StatementNode {

    private String value;
    public enum LiteralType{INT, STRING, BOOLEAN}
    private LiteralType type;

    public LiteralNode(){
        super.setNodeType(ProgramNodeType.LITERAL);
        super.setLeftChild(null);
        super.setRightChild(null);
    }

    public String toString(){
        return this.type+": "+"("+this.value+")";
    }
}
