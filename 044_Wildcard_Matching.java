public class Solution {
    Node end;
    Set<Node> currentNodes = new HashSet<>();

    public boolean isMatch(String str, String pattern) {
        Node start = buildNFA(pattern);
        addClosure(end.index, start);

        for (int i = 0; i < str.length(); i++) {
            char chr = str.charAt(i);
            int remainSteps = str.length() - 1 - i;

            Node[] currentCopy = currentNodes.toArray(new Node[currentNodes.size()]);
            currentNodes.clear();
            
            // step from current nodes
            for (Node node : currentCopy) {
                Node explicit = node.outArrows.get(chr);
                Node wildcard = node.outArrows.get('?');

                if (explicit != null) addClosure(remainSteps, explicit);
                if (wildcard != null) addClosure(remainSteps, wildcard);
            }
        }

        for (Node node : currentNodes) {
            if (node == end) return true;
        }

        return false;
    }

    void addClosure(int remainSteps, Node node) {
        Node emptyMove = node.outArrows.get(Node.EMPTY_ARROW);

        if (emptyMove == null) {
            if (end.index - node.index <= remainSteps)
                currentNodes.add(node);
        } else {
            if (end.index - emptyMove.index <= remainSteps) {
                currentNodes.add(node);
                currentNodes.add(emptyMove);
            }
        }
    }

    Node buildNFA(String pattern) {
        Node start = new Node(0);
        end = start;

        for (int i = 0; i < pattern.length(); i++) {
            char chr = pattern.charAt(i);

            if (chr == '*') {

                // reduce continuous '*' to single one
                if (i - 1 >= 0 && pattern.charAt(i - 1) == '*') continue;

                // do not increase wildcard index
                Node body = new Node(end.index);
                Node tail = new Node(end.index);

                end.addArrow('?', body);
                end.addArrow(Node.EMPTY_ARROW, tail);
                body.addArrow('?', body);
                body.addArrow(Node.EMPTY_ARROW, tail);

                end = tail;

            } else {
                Node node = new Node(end.index + 1);
                end.addArrow(chr, node);
                end = node;
            }
        }
        return start;
    }

    static class Node {
        static final char EMPTY_ARROW = 0;

        int index; // for remain steps estimation
        Map<Character, Node> outArrows = new HashMap<>();

        Node(int index) {
            this.index = index;
        }

        void addArrow(char chr, Node dest) {
            outArrows.put(chr, dest);
        }
    }
}
