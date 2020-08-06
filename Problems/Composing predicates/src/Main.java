import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

class CombiningPredicates {

    /**
     * The method represents a disjunct operator for a list of predicates.
     * For an empty list it returns the always false predicate.
     */
    public static IntPredicate disjunctAll(List<IntPredicate> predicates) {
        if (predicates.isEmpty()) {
            return v -> false;
        }
        IntPredicate result = predicates.get(0);
        for (int i = 1, predicatesSize = predicates.size(); i < predicatesSize; i++) {
            IntPredicate predicate = predicates.get(i);
            result = result.or(predicate);
        }
        return result;
    }

    // Don't change the code below
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String[] strings = scanner.nextLine().split(" ");

        List<Boolean> values = Arrays.stream(strings)
                .map(Boolean::parseBoolean)
                .collect(Collectors.toList());

        List<IntPredicate> predicates = new ArrayList<>();
        values.forEach(v -> predicates.add((x) -> v));

        System.out.println(disjunctAll(predicates).test(0));
    }
}