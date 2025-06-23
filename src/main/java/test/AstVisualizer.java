package test;

import com.dependancy.Parser.AstNodes.AssignmentNode;
import com.dependancy.Parser.AstNodes.DeclarationNode;
import com.dependancy.Parser.AstNodes.ExpressionNode;
import com.dependancy.Parser.AstNodes.ProgramNode;

import java.io.FileWriter;
import java.io.IOException;

public class AstVisualizer {

    private StringBuilder dot;
    private int nodeCount = 0;

    public String generate(ProgramNode root) {
        dot = new StringBuilder();
        dot.append("digraph AST {\n");
        dot.append("    node [shape=box, style=filled, color=lightblue];\n");

        buildGraph(root, null);

        dot.append("}\n");
        System.out.println(nodeCount);
        return dot.toString();
    }

    private String buildGraph(ProgramNode node, String parentId) {
        if (node == null) return null;

        String nodeId = "node" + (nodeCount++);
        String label = getNodeLabel(node);

        dot.append(String.format("    %s [label=\"%s\"];\n", nodeId, label));

        if (parentId != null) {
            dot.append(String.format("    %s -> %s;\n", parentId, nodeId));
        }

        if (node instanceof ExpressionNode expr) {
            // recurse children (preorder traversal)
            if (expr.left != null) buildGraph(expr.left, nodeId);
            if (expr.right != null) buildGraph(expr.right, nodeId);
            if (expr.innerExpression != null) buildGraph(expr.innerExpression, nodeId);
        } else if (node instanceof DeclarationNode n) {
            if (n.right != null)
                buildGraph(n.right, nodeId);
        }

        return nodeId;
    }

    private String getNodeLabel(ProgramNode node) {
        if (node instanceof ExpressionNode expr) {
            return switch (expr.expressionType) {
                case ExpressionNode.ExpressionType.IDENTIFIER -> "Identifier: " + expr.identifierValue;
                case ExpressionNode.ExpressionType.LITERAL -> "Literal: " + expr.literalValue;
                case ExpressionNode.ExpressionType.OPERATOR -> "Operator: " + expr.operatorValue;
                case ExpressionNode.ExpressionType.PARENTHESIS -> "Parenthesis";
                case EXPRESSION -> null;
            };
        } else if (node instanceof DeclarationNode s) {
            return s.toString();
        }
        return "Unknown";
    }

    public void exportToDotFile(String fileName, ProgramNode root) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(generate(root));
            System.out.println("DOT file written to: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
