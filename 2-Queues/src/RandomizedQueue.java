/*
    @author: Simone Nicol <en0mia.dev@gmail.com>
    @created: 13/02/22
    @copyright: Check the repository license.
*/

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Object[] array;
    private int n;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.array = new Object[1];
        this.n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return this.n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return this.n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (this.n == this.array.length) {
            this.resize(this.array.length * 2);
        }

        this.array[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (this.size() == 0) {
            throw new NoSuchElementException();
        }

        if (this.n == this.array.length / 4) {
            this.resize(this.array.length / 2);
        }

        int index = getRandomIndex(this.n);
        Item item = (Item) this.array[index];

        this.array[index] = this.array[n - 1];
        this.array[n - 1] = null;
        this.n --;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (this.size() == 0) {
            throw new NoSuchElementException();
        }

        int index = getRandomIndex(this.n);
        return (Item) this.array[index];
    }

    // return an independent iterator over items in random order
    @Override
    public Iterator<Item> iterator() {
        return new RandomIterator<>(this);
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        int index = 0;

        for (Object o : this.array) {
            if (o != null) {
                copy[index] = (Item) o;
                index++;
            }
        }

        this.array = copy;
    }

    private int getRandomIndex(int max) {
        return StdRandom.uniform(max);
    }

    private class RandomIterator<IteratorItem> implements Iterator<IteratorItem> {
        Object[] array;
        int items;

        public RandomIterator(RandomizedQueue<IteratorItem> queue) {
            this.array = new Object[queue.array.length];
            System.arraycopy(queue.array, 0, this.array, 0, queue.array.length);
            this.items = queue.n;
        }

        @Override
        public boolean hasNext() {
            return this.items > 0;
        }

        @Override
        public IteratorItem next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }

            if (this.items == this.array.length / 4) {
                this.resize(this.array.length / 2);
            }

            int index = getRandomIndex(this.items);
            IteratorItem item = (IteratorItem) this.array[index];;

            this.array[index] = this.array[this.items - 1];
            this.array[this.items - 1] = null;
            this.items --;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void resize(int capacity) {
            Object[] copy = new Object[capacity];
            int index = 0;

            for (Object o : this.array) {
                if (o != null) {
                    copy[index] = o;
                    index++;
                }
            }

            this.array = copy;
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();

        rq.enqueue("a");
        rq.enqueue("b");
        rq.enqueue("c");
        rq.enqueue("d");
        rq.enqueue("e");
        rq.enqueue("f");
        rq.enqueue("g");
        rq.enqueue("h");
        rq.enqueue("i");
        rq.enqueue("l");

        // Expected output: a-l in random order
        for (String s : rq) {
            System.out.println(s);
        }

        // Expected output: false, 10
        System.out.printf("%b, %d\n", rq.isEmpty(), rq.size());

        // Expected output: one of a-l, one of a-l - {the char before}
        System.out.printf("%s, %s \n", rq.dequeue(), rq.sample());
    }
}
