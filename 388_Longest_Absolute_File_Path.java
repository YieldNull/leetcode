import java.util.LinkedList;

public class Solution {

    public int lengthLongestPath(String input) {

        // return useTree(input);

        return useStack(input);
    }

    private int maxLen;
    private LinkedList<Integer> stack;   // store parent dirs' absolute lengths , include '/'

    private int useStack(String input) {
        stack = new LinkedList<>();
        stack.push(-1);

        int depth = 0, length = 0;
        boolean isFile = false;
        char chr;

        for (int i = 0; i < input.length(); i++) {
            chr = input.charAt(i);

            if (chr == '\n') {

                buildStack(isFile, length, depth);

                depth = 0;
                length = 0;
                isFile = false;
            } else if (chr == '\t') {
                depth++;
            } else {
                length++;

                if (chr == '.') isFile = true;
            }
        }

        buildStack(isFile, length, depth);
        return maxLen;
    }

    private void buildStack(boolean isFile, int length, int depth) {
        while (depth < stack.size() - 1) stack.pop();

        int len = stack.peek() + length + 1;

        if (!isFile) {
            stack.push(len);
        } else {
            if (len > maxLen) maxLen = len;
        }
    }


    private Node lastNode;
    private Node maxNode;

    private static class Node {
        boolean isFile;
        int length;
        int depth;
        Node superNode;

        Node(boolean isFile, int length, int depth, Node superNode) {
            this.isFile = isFile;
            this.length = length;  // absolute length, exclude '/'
            this.depth = depth;
            this.superNode = superNode;
        }
    }


    private int useTree(String input) {
        int depth = 0;
        int length = 0;
        boolean isFile = false;


        Node root = new Node(false, 0, -1, null);

        maxNode = root;
        lastNode = root;

        char chr;

        for (int i = 0; i < input.length(); i++) {
            chr = input.charAt(i);

            if (chr == '\n') {
                buildTree(isFile, length, depth);

                depth = 0;
                length = 0;
                isFile = false;
            } else if (chr == '\t') {
                depth++;
            } else {
                length++;

                if (chr == '.') isFile = true;
            }
        }

        buildTree(isFile, length, depth);

        if (maxNode.depth == -1) maxNode.depth = 0; // maxNode is root node

        return maxNode.length + maxNode.depth;
    }

    private void buildTree(boolean isFile, int length, int depth) {
        Node node = lastNode;

        while (node.depth >= depth) {
            node = node.superNode;
        }

        lastNode = new Node(isFile, node.length + length, depth, node);

        if (lastNode.isFile && lastNode.length + lastNode.depth > maxNode.length + maxNode.depth) maxNode = lastNode;
    }
}
