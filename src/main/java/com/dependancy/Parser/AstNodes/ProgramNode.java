package com.dependancy.Parser.AstNodes;

public abstract class ProgramNode {

    public ProgramNode left = null;
    public ProgramNode right = null;

    public Integer lineNumber;
    public AstNodeType nodeType;

    abstract public String toString();
}
