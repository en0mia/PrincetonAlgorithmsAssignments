/*
    @author: Simone Nicol <en0mia.dev@gmail.com>
    @created: 09/09/21
    @copyright: Check the repository license.
*/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String input, champion = "";

        double i = 1;

        while (!StdIn.isEmpty()) {
            input = StdIn.readString();

            if (StdRandom.bernoulli(1.0/i)) {
                champion = input;
            }

            i++;
        }

        System.out.println(champion);
    }
}
