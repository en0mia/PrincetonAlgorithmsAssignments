/*
    @author: Simone Nicol <en0mia.dev@gmail.com>
    @created: 13/02/22
    @copyright: Check the repository license.
*/

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String []args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        int k = Integer.parseInt(args[0]);

        while (!StdIn.isEmpty()) {
            rq.enqueue(StdIn.readString());
        }

        for (int i = 0; i < k; i++) {
            System.out.println(rq.dequeue());
        }
    }
}
