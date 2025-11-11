package fr.killiandev.book.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Test;

class ListUtilTest {

    @Test
    void convertsIterableToMutableList() {
        Iterable<String> iterable = new SingleUseIterable<>("a", "b", "c");

        List<String> result = ListUtil.toList(iterable);

        assertThat(result).containsExactly("a", "b", "c");

        result.add("d");
        assertThat(result).containsExactly("a", "b", "c", "d");
    }

    @Test
    void preservesIterationOrder() {
        Iterable<Integer> iterable = new SingleUseIterable<>(3, 1, 2);

        List<Integer> result = ListUtil.toList(iterable);

        assertThat(result).containsExactly(3, 1, 2);
    }

    private static final class SingleUseIterable<T> implements Iterable<T> {

        private final List<T> values;
        private boolean consumed;

        private SingleUseIterable(T... values) {
            this.values = List.of(values);
        }

        @Override
        public Iterator<T> iterator() {
            if (consumed) {
                throw new IllegalStateException("Iterable consumed twice");
            }
            consumed = true;
            return values.iterator();
        }
    }
}
