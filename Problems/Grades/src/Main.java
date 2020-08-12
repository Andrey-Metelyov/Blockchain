import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);
        int numberOfGrades = Integer.parseInt(scanner.nextLine());

//        Stream.of(scanner.nextLine())
//                .mapToInt(Integer::parseInt)
//                .limit(numberOfGrades)
//                .collect(groupingBy(Function.identity(), Collectors.counting()));
//        IntStream.generate(() -> Integer.parseInt(scanner.nextLine()))
//                .limit(numberOfGrades)
//                .
//                .collect(Collectors.groupingBy(v -> v, Collectors.counting() ))
//                ;

        Map<Integer, Integer> grades = new HashMap<>();
        for (int i = 2; i <= 5; i++) {
            grades.put(i, 0);
        }
        for (int i = 0; i < numberOfGrades; i++) {
            int grade = Integer.parseInt(scanner.nextLine());
            grades.put(grade, grades.get(grade) + 1);
        }
        grades.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(e -> System.out.print(e.getValue() + " "));
    }
}