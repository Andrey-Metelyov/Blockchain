import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class FunctionUtils {
    public static <T> Supplier<Stream<T>> saveStream(Stream<T> stream) {
        // write your code here
        List<T> saved = stream.collect(Collectors.toList());
        return () -> saved.stream();
    }
//    public static <T> Supplier<Stream<T>> saveStream(Stream<T> stream) {
//        // write your code here
//        final Spliterator<T> iter = stream.spliterator();
//        final ArrayList<T> mem = new ArrayList<>();
//        class MemoizeIter extends Spliterators.AbstractSpliterator<T> {
//            MemoizeIter() {
//                super(iter.estimateSize(), iter.characteristics());
//            }
//
//            public boolean tryAdvance(Consumer<? super T> action) {
//                return iter.tryAdvance(item -> {
//                    mem.add(item);
//                    action.accept(item);
//                });
//            }
//        }
//        MemoizeIter srcIter = new MemoizeIter();
//        return () -> concat(mem.stream(), stream(srcIter, false));
//    }
}