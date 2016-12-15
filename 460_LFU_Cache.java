import java.util.HashMap;

/**
 * LFU and LRU list
 * http://dhruvbird.com/lfu.pdf
 */
public class LFUCache {

    private int capacity;
    private FRList frList = new FRList();
    private HashMap<Integer, AccessNode> cache;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        cache = new HashMap<>(capacity);
    }

    public int get(int key) {
        AccessNode accessNode = cache.get(key);

        if (accessNode == null) {
            return -1;
        } else {
            frList.incrFreq(accessNode);

            return accessNode.value;
        }
    }

    public void set(int key, int value) {
        AccessNode accessNode = cache.get(key);

        if (accessNode != null) {
            accessNode.value = value;

            frList.incrFreq(accessNode);
        } else {
            if (capacity != 0) {
                if (cache.size() == capacity) {
                    int leastKey = frList.removeLeast();
                    cache.remove(leastKey);
                }

                accessNode = new AccessNode(key, value);
                cache.put(key, accessNode);

                frList.initFreq(accessNode);
            }
        }
    }


    /**
     * A node recording cache value and its frequency of accessing
     */
    private static class AccessNode {
        AccessNode pre;
        AccessNode post;

        int key;
        int value;

        FreqNode freq;

        AccessNode(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * An double-linked list sorted by recently used order.
     */
    private static class LRUList {
        AccessNode head;
        AccessNode tail;

        int size;

        /**
         * Init with the head node
         *
         * @param head node
         */
        LRUList(AccessNode head) {
            head.pre = null;
            head.post = null;

            this.head = head;
            this.tail = head;
            this.size = 1;
        }

        /**
         * Add a node to the head of list
         *
         * @param node node
         */
        void add(AccessNode node) {
            head.pre = node;
            node.post = head;
            node.pre = null;
            head = node;

            size++;
        }

        /**
         * Remove a node.
         *
         * @param node node
         */
        void remove(AccessNode node) {

            if (node.pre != null) {
                node.pre.post = node.post;
            } else {
                head = node.post; // node is the head
            }

            if (node.post == null) {
                tail = node.pre; // node is the tail
            } else {
                node.post.pre = node.pre;
            }

            size--;
        }

        /**
         * Remove tail node
         *
         * @return tail node
         */
        int removeLast() {
            int toReturn = tail.key;
            remove(tail);
            return toReturn;
        }
    }

    /**
     * Frequency node with a LRUList
     */
    private static class FreqNode {
        FreqNode pre;
        FreqNode post;

        int freq;

        LRUList list;

        FreqNode(AccessNode firstNode) {
            this(0, firstNode);
        }

        FreqNode(int freq, AccessNode firstNode) {
            this.freq = freq;
            list = new LRUList(firstNode);

            firstNode.freq = this;
        }

        void add(AccessNode node) {
            list.add(node);
            node.freq = this;
        }

        boolean remove(AccessNode node) {
            list.remove(node);
            return list.size == 0;
        }

        int removeLast() {
            return list.removeLast();
        }
    }

    /**
     * LFU and LRU list
     * http://dhruvbird.com/lfu.pdf
     */
    private static class FRList {
        FreqNode head;
        FreqNode tail;


        /**
         * Add a new node
         *
         * @param accessNode node
         */
        void initFreq(AccessNode accessNode) {
            if (head == null) {
                // first freqNode
                head = new FreqNode(accessNode);
                tail = head;
            } else if (head.freq == 0) {
                // head node has the same freq
                head.add(accessNode);
            } else {
                // Insert a new node to head
                FreqNode node = new FreqNode(accessNode);
                head.pre = node;
                node.post = head;
                head = node;
            }
        }

        /**
         * Increase frequency
         *
         * @param accessNode node
         */
        void incrFreq(AccessNode accessNode) {
            FreqNode freqNode = accessNode.freq;

            // remove the accessNode from old LRU list
            boolean oldFreqNodeEmpty = freqNode.remove(accessNode);

            // move the accessNode to a new LRU list
            if (freqNode.post != null && freqNode.post.freq == freqNode.freq + 1) {
                // post freqNode is the right choice
                freqNode.post.add(accessNode);

            } else {
                // post freqNode is too large, create a new node
                FreqNode newFreq = new FreqNode(freqNode.freq + 1, accessNode);

                // Insert the new node
                newFreq.pre = freqNode;
                newFreq.post = freqNode.post;

                if (freqNode.post != null) {
                    freqNode.post.pre = newFreq;
                }

                freqNode.post = newFreq;

                // update tail if possible
                if (freqNode == tail) {
                    tail = newFreq;
                }
            }

            if (oldFreqNodeEmpty) {
                removeLRUList(freqNode);
            }
        }

        /**
         * remove the FreqNode if its LRUList is empty
         *
         * @param freqNode node
         */
        private void removeLRUList(FreqNode freqNode) {
            if (freqNode.pre != null) {
                freqNode.pre.post = freqNode.post;
            } else {
                head = freqNode.post; // freqNode is the head
            }

            if (freqNode.post != null) {
                freqNode.post.pre = freqNode.pre;
            } else {
                tail = freqNode.pre; // freqNode is the tail
            }
        }

        /**
         * remove tail node of the LRUList from head FreqNode
         *
         * @return corresponding key
         */
        private int removeLeast() {
            int key = head.removeLast();

            if (head.list.size == 0) {
                removeLRUList(head);
            }

            return key;
        }
    }
}
