/*
    @author: Simone Nicol <en0mia.dev@gmail.com>
    @created: 11/02/22
    @copyright: Check the repository license.
*/

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque <Item> implements Iterable<Item> {
    private static class Node<NodeItem> {
        public NodeItem value;
        public Node<NodeItem> next;
        public Node<NodeItem> previous;

        public Node(NodeItem val) {
            this.value = val;
            this.next = null;
            this.previous = null;
        }
    }

    private Node<Item> first;
    private Node<Item> last;
    private int size;

    // construct an empty deque
    public Deque() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return this.size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return this.size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Illegal null argument");
        }

        Node<Item> node = new Node<>(item);

        node.next = first;
        node.previous = null;

        if (this.first != null) {
            this.first.previous = node;
        }

        this.first = node;
        this.size ++;

        if (this.size == 1) {
            this.last = this.first;
        }
    }

    // add the item to the back
    public void addLast(Item item)  {
        if (item == null) {
            throw new IllegalArgumentException("Illegal null argument");
        }

        Node<Item> node = new Node<>(item);

        if (this.isEmpty()) {
            node.previous = null;
            this.first = node;
            this.last = node;
            this.size ++;
            return;
        }


        node.previous = this.last;
        this.last.next = node;
        this.last = node;
        this.size ++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (this.size == 0) {
            throw new NoSuchElementException("Deque is empty!");
        }

        Node<Item> current = this.first;
        this.first = this.first.next;
        this.size --;
        return current.value;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (this.size == 0) {
            throw new NoSuchElementException("Deque is empty!");
        }

        Node<Item> current = this.last;
        if (this.size - 1 == 0) {
            this.last = this.first = null;
            this.size --;
            return current.value;
        }
        this.last.previous.next = null;
        this.last = this.last.previous;
        this.size --;

        return current.value;
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeueIterator(this);
    }

    private class DequeueIterator implements Iterator<Item> {
        Node<Item> current;

        public DequeueIterator(Deque<Item> dequeue) {
            this.current = dequeue.first;
        }

        @Override
        public boolean hasNext() {
            return this.current != null;
        }

        @Override
        public Item next() {
            if (this.current == null) {
                throw new NoSuchElementException();
            }
            Item value = this.current.value;
            this.current = this.current.next;
            return value;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main (String[] args) {
        Deque<String> deck = new Deque<>();
        deck.addLast("2");
        deck.addFirst("1");
        deck.addFirst("2");
        deck.addLast("2");
        deck.addLast("3");
        deck.addFirst("4");
        deck.removeLast();

        // Expected output: 4 2 1 2 2
        for (String s : deck) {
            System.out.println(s);
        }

        // Expected output: false, 5
        System.out.printf("%b, %d\n", deck.isEmpty(), deck.size());

        // Expected output: 4 2 2
        System.out.printf("%s, %s, %s\n", deck.removeFirst(), deck.removeLast(), deck.removeLast());
    }
}
