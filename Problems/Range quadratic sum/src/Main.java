import java.util.stream.*;

class QuadraticSum {
    public static long rangeQuadraticSum(int fromIncl, int toExcl) {
        // write your code with streams here
        return IntStream.range(fromIncl, toExcl)
                .reduce(0, (sum, next) -> sum + next * next);
    }
}