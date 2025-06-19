package com.dependancy.parser.AST_nodes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LiteralNode extends ProgramNode{

    private String value;
    public enum LiteralType{INT, STRING, BOOLEAN}
    private LiteralType type;

    public LiteralNode(LiteralType type, String value){
        super.setNodeType(ProgramNodeType.LITERAL);
        super.setLeftChild(null);
        super.setRightChild(null);
        this.type = type;
        this.value = value;
    }

    public String toString(){
        return this.type+": "+"("+this.value+")";
    }
}
