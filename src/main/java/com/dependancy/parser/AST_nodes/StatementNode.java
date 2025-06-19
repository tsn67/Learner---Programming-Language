package com.dependancy.parser.AST_nodes;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class ProgramNode {
    private ProgramNodeType nodeType;
    private ProgramNode leftChild;
    private ProgramNode rightChild;

    public abstract String toString();
}
