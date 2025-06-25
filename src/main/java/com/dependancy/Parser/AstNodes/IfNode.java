package com.dependancy.Parser.AstNodes;

import java.util.List;

public class IfNode extends ProgramNode{

    //optional else node
    public ProgramNode elseNode = null;

    public ExpressionNode condition = null; //for else node, it is null

    public List<ProgramNode> body = null;

    public IfNode(Integer lineNumber) {
        super.nodeType = AstNodeType.IF;
        super.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return "IF NODE:" + this.condition.toString();
    }
}
