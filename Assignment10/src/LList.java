public class LList<T> {
    private Node first;

    public class Node {
        final T value;
        Node next;

        public Node(T v, Node n) {
            value = v;
            next = n;
        }
    }
    public LList() {
        first = new Node(null, null);
    }

    public void addFirst(T value) {
        first.next = new Node(value, first.next);
    }

    public char moveToFront(T v) {
        if (first.next == null) {
            throw new java.lang.RuntimeException();
        }
        Node preNode = first;
        char count = 0;
        while (preNode.next.value != v) {
                count += 1;
                preNode = preNode.next;
        }
        if (preNode == first) {
            return count;
        }
        Node head = first.next;
        Node move = preNode.next;
        preNode.next = preNode.next.next;
        first.next = move;
        move.next = head;
        return count;
    }

    @Override
    public String toString() {
        Node sentinel = first.next;
        StringBuilder sb = new StringBuilder();
        while (sentinel!= null) {
            sb.append(sentinel.value);
            sb.append(" ");
            sentinel = sentinel.next;
        }
        sb.append("null");
        return sb.toString();
    }

    public static void main(String[] args) {
    }
}
