import java.util.Scanner;
import java.util.Stack;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        System.out.println(check(input));
    }

    private static boolean check(String input) {
        Stack<Character> characters = new Stack<>();
        String openingBrackets = "([{";
        String closingBrackets = ")]}";
        for (int i = 0; i < input.length(); i++) {
            char character = input.charAt(i);
            if (openingBrackets.contains(String.valueOf(character))) {
                characters.push(character);
            } else {
                char matchingBracket = openingBrackets.charAt(closingBrackets.indexOf(character));
                if (characters.empty() || characters.pop() != matchingBracket) {
                    return false;
                }
            }
        }
        return characters.empty();
    }
}