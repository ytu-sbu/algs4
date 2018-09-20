import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int end;
    private Item[] queue;

    public RandomizedQueue() {
        end = 0;
        queue = (Item[]) new Object[2];
    }

    public boolean isEmpty() {
        return end == 0;
    }

    public int size() {
        return end;
    }

    private void resize(int capacity) {
        if (capacity <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        Item[] newQueue = (Item[]) new Object[capacity];
        System.arraycopy(queue, 0, newQueue, 0, end);
        queue = newQueue;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }
        if (end == queue.length) {
            resize(queue.length * 2);
        }
        queue[end++] = item;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int index = StdRandom.uniform(end);
        Item value = queue[index];
        queue[index] = queue[--end];
        queue[end] = null;
        if (end * 1.0 / queue.length < 0.25 && queue.length >= 4) {
            resize(queue.length / 2);
        }
        return value;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int index = StdRandom.uniform(end);
        return queue[index];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    /* 1st version
    private class RandomizedQueueIterator implements Iterator<Item> {
        private int size;
        private boolean[] used;
        RandomizedQueueIterator() {
            size = 0;
            used = new boolean[end];
        }
        public boolean hasNext() {
            return size < end;
        }
        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            int index = StdRandom.uniform(end);
            while (used[index]) {
                index = StdRandom.uniform(end);
            }
            Item value = queue[index];
            used[index] = true;
            size += 1;
            return value;
        }
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    } */

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int size;
        private Item[] dup;
        RandomizedQueueIterator() {
            size = end;
            dup = (Item[]) new Object[end];
            System.arraycopy(queue, 0, dup, 0, end);
        }
        public boolean hasNext() {
            return size != 0;
        }
        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            int index = StdRandom.uniform(size);
            Item value = dup[index];
            dup[index] = dup[--size];
            dup[size] = null;
            return value;
        }
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        rq.enqueue("A1");
        rq.enqueue("A2");
        rq.enqueue("A3");
        rq.enqueue("A4");
        rq.enqueue("A5");
        rq.enqueue("A6");
        rq.enqueue("A7");
        rq.enqueue("A8");
        rq.enqueue("A9");
        rq.enqueue("A10");
        // System.out.println(rq.sample());
        for (String i : rq) {
            System.out.println(i);
        }
    }

}
