package com.dependancy.Parser.AstNodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionNode extends ProgramNode{

    public List<ProgramNode> body = new ArrayList<>();

    public enum FunctionType {VOID, NUMBER, STRING, BOOLEAN}
    public String functionName;
    public Map<DeclarationNode.DataType, String> parameters = new HashMap<>();
    public FunctionType returnType;

    public FunctionNode(Integer lineNumber) {
        super.nodeType = AstNodeType.FUNCTION;
        super.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return "FunctionNode:" + functionName + "(" + returnType.toString() + ")";
    }
}
