package com.dependancy.parser.AST_nodes;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class StatementNode {
    private ProgramNodeType nodeType;
    private StatementNode leftChild;
    private StatementNode rightChild;

    public abstract String toString();
}
