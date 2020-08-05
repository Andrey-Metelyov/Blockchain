import java.util.function.*;

class FunctionUtils {

    public static Supplier<Integer> getInfiniteRange() {
        // write your code here
        return new Supplier<Integer>() {
            int number = 0;
            @Override
            public Integer get() {
                return number++;
            }
        };
    }

}