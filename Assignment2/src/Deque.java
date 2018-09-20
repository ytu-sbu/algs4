import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private final Node first, last;
    private int size;

    private class Node {
        private final Item item;
        private Node next;
        private Node pre;

        Node() {
            item = null;
            next = null;
            pre = null;
        }

        Node(Item t) {
            item = t;
            next = null;
            pre = null;
        }
    }

    public Deque() {
        first = new Node();
        last = first;
        first.next = last;
        last.pre = first;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        checkAdd(item);
        Node oldItem = first.next;
        Node newItem = new Node(item);
        first.next = newItem;
        newItem.pre = first;
        newItem.next = oldItem;
        oldItem.pre = newItem;
        size += 1;
    }

    private void checkAdd(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    public void addLast(Item item) {
        checkAdd(item);
        Node oldItem = last.pre;
        Node newItem = new Node(item);
        last.pre = newItem;
        newItem.next = last;
        oldItem.next = newItem;
        newItem.pre = oldItem;
        size += 1;
    }

    private void checkRemove() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
    }
    public Item removeFirst() {
        checkRemove();
        Item value = first.next.item;
        Node next = first.next.next;
        first.next = next;
        next.pre = first;
        size -= 1;
        return value;
    }

    public Item removeLast() {
        checkRemove();
        Item value = last.pre.item;
        Node pre = last.pre.pre;
        pre.next = last;
        last.pre = pre;
        size -= 1;
        return value;
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current;

        DequeIterator() {
            current = first.next;
        }

        public boolean hasNext() {
            return current != last;
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            else {
                Item value = current.item;
                current = current.next;
                return value;
            }
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    public static void main(String[] args) {
        Deque<Integer> dq = new Deque<>();
        System.out.println("is the Deque empty? Yes: " + dq.isEmpty());
        try {
            System.out.println("try add null to first");
            dq.addFirst(null);
        }
        catch (java.lang.IllegalArgumentException e) {
            // System.out.println("catch IllegalArgumentException");
            System.out.println(e);
        }
        try {
            System.out.println("try add null to last");
            dq.addLast(null);
        }
        catch (java.lang.IllegalArgumentException e) {
        // } catch (Exception e) {
            // System.out.println("catch IllegalArgumentException");
            System.out.println(e);
        }
        System.out.println("add 1 on first, 4 on last");
        dq.addFirst(1);
        dq.addLast(4);
        System.out.println("rm all from first");
        System.out.print(dq.removeFirst() + " ");
        System.out.println(dq.removeFirst());

        System.out.println("add 1 on first, 4 on last");
        dq.addFirst(1);
        dq.addLast(4);
        System.out.println("rm all from last");
        System.out.print(dq.removeLast() + " ");
        System.out.println(dq.removeLast());
        System.out.println("try remove empty");
        try {
            dq.removeFirst();
        }
        catch (java.util.NoSuchElementException e) {
            System.out.println(e);
        }

        dq.addLast(1);
        dq.addLast(2);
        dq.addLast(3);
        dq.addLast(4);
        dq.addLast(5);
        dq.addLast(6);
        for (int i : dq) {
            System.out.print(i + " ");
        }
        System.out.println();

        try {
            Iterator<Integer> ii = dq.iterator();
            ii.remove();
        }
        catch (java.lang.UnsupportedOperationException e) {
            System.out.println(e);
        }
    }
}
