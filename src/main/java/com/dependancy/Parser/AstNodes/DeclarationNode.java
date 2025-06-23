package com.dependancy.Parser.AstNodes;

public class DeclarationNode extends ProgramNode{

    public String identifier;
    public DataType dataType = null;

    public enum DataType {
        NUMBER, STRING, BOOLEAN
    }

    public DeclarationNode(Integer lineNumber) {
        super.nodeType = AstNodeType.DECLARATION;
        super.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return "DeclarationNode: "+this.identifier+" ("+this.dataType+") ";
    }
}
