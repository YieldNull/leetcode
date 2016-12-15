import java.util.HashMap;

public class LRUCache {

    private static class Node {
        Node left;
        Node right;
        Integer value; // null means removed

        Node(int value) {
            this.value = value;
        }
    }

    private static class AccessList {
        Node first;
        Node last;

        int size;

        void add(Node node) {
            if (first == null) {
                first = node;
                last = node;
            } else {
                offerFirst(node);
            }

            size++;
        }

        void access(Node node) {
            if (node != first) {
                node.left.right = node.right;

                if (node == last) {
                    last = last.left;
                } else {
                    node.right.left = node.left;
                }

                offerFirst(node);
            }
        }

        void removeLast() {
            Node removed = last;

            last = last.left;
            if (last == null) {
                first = null;
            } else {
                last.right = null;
            }

            removed.value = null;
            removed.left = null;
            removed.right = null;

            size--;
        }

        private void offerFirst(Node node) {
            first.left = node;
            node.right = first;
            node.left = null;
            first = node;
        }
    }

    private int capacity;
    private HashMap<Integer, Node> values;
    private AccessList accessList = new AccessList();

    public LRUCache(int capacity) {
        this.capacity = capacity;
        values = new HashMap<>(capacity);
    }

    public int get(int key) {
        Node node = values.get(key);

        if (node == null || node.value == null) {
            return -1;
        } else {
            accessList.access(node);
            return node.value;
        }
    }

    public void set(int key, int value) {
        Node node = values.get(key);

        if (node != null && node.value != null) {
            node.value = value;
            accessList.access(node);
        } else {
            if (accessList.size == capacity) {
                accessList.removeLast();
            }

            if (node == null) {
                node = new Node(value);
                values.put(key, node);
            } else {
                node.value = value;
            }
            accessList.add(node);
        }
    }
}
