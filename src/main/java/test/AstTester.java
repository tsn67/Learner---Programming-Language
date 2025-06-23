package test;

import com.dependancy.Parser.AstNodes.ExpressionNode;
import com.dependancy.Parser.AstNodes.ProgramNode;

import java.util.ArrayList;
import java.util.List;

public class AstTester {

    private final List<ProgramNode> result = new ArrayList<>();

    public void preOrderTraversal(ProgramNode node) {
        if (node != null) {
            result.add(node);

            if (node instanceof ExpressionNode && ((ExpressionNode) node).expressionType == ExpressionNode.ExpressionType.PARENTHESIS) {
                preOrderTraversal(((ExpressionNode) node).innerExpression);
                return;
            }

            preOrderTraversal(node.left);
            preOrderTraversal(node.right);
        }
    }

    public void report(ProgramNode root) {

        preOrderTraversal(root);

        System.out.println("Total nodes: "+ result.size());

        System.out.println("\nPre-order traversal: \n");

        result.forEach(u -> {
            System.out.println(u.toString());
        });
    }

}
