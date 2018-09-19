public class LList<T> {
    private Node<T> first;

    public class Node<T> {
        final T value;
        Node<T> next;
        public Node() {
            value = null;
            next = null;
        }

        public Node(T v) {
            value = v;
            next = null;
        }

        public Node(T v, Node<T> n) {
            value = v;
            next = n;
        }
    }

    public LList() {
        first = new Node<>();
    }

    public void addFirst(T value) {
        Node<T> tmpNode = first.next;
        first.next = new Node<T>(value);
        first.next.next = tmpNode;
    }

    public char moveToFront(T v) {
        if (first.next == null) {
            throw new java.lang.IllegalArgumentException();
        }
        return 'A'; /*
        Node<T> checkNode = first;
        char count = 0;
        while (checkNode.next.value != v) {
                count += 1;
                checkNode = checkNode.next;
        }
        if (checkNode == first) {
            return count;
        }
        Node<T> head = first.next;
        Node<T> move = checkNode.next;
        checkNode.next = checkNode.next.next;
        first.next = move;
        move.next = head;
        return count;
        */
    }

    @Override
    public String toString() {
        Node<T> sentinel = first.next;
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
        LList<Character> ll = new LList<>();
        for (int i = 255; i >= 0; i--) {
            ll.addFirst((char) i);
        }
        for (char i = 0; i < 256; i++) {
            System.out.print((int) i + " : ");
            System.out.println(i);
        }
//        System.out.println(ll); //ABRACADABRA!
        int res = ll.moveToFront('A');
        System.out.println(res);
        res = ll.moveToFront('B');
        System.out.println(res);
        res = ll.moveToFront('R');
        System.out.println(res);
        res = ll.moveToFront('A');
        System.out.println(res);
        res = ll.moveToFront('C');
        System.out.println(res);
        res = ll.moveToFront('A');
        System.out.println(res);
        res = ll.moveToFront('D');
        System.out.println(res);
        res = ll.moveToFront('A');
        System.out.println(res);
        res = ll.moveToFront('B');
        System.out.println(res);
        res = ll.moveToFront('R');
        System.out.println(res);
        res = ll.moveToFront('A');
        System.out.println(res);
        res = ll.moveToFront('!');
        System.out.println(res);
//        System.out.println(ll);
    }
}
