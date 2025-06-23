package com.dependancy.Parser.AstNodes;

public class AssignmentNode extends ProgramNode{

    public ProgramNode right; //the assignment node will have only right-child (and it will be expression node type)
    public String identifier;

    public AssignmentNode(Integer lineNumber) {
        super.nodeType = AstNodeType.ASSIGNMENT;
        super.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return "assignment_node: "+this.identifier;
    }
}
