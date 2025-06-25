package com.dependancy.Parser.AstNodes;

import java.util.ArrayList;
import java.util.List;

public class WhileNode extends ProgramNode{

    public ExpressionNode condition;

    public List<ProgramNode> body = new ArrayList<>();

    public WhileNode(Integer lineNumber) {
        super.nodeType = AstNodeType.WHILE;
        super.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return "while node "+ this.condition.toString();
    }
}
