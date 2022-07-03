/*
    @author: Simone Nicol <en0mia.dev@gmail.com>
    @created: 09/09/21
    @copyright: Check the repository license.
*/

public class HelloGoodbye {
    public static void main(String[] args) {
        System.out.println(String.format("Hello %s and %s.", args[0], args[1]));
        System.out.println(String.format("Goodbye %s and %s.", args[1], args[0]));
    }
}
