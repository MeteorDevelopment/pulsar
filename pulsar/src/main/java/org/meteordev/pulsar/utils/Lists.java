package org.meteordev.pulsar.utils;

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;

public class Lists {
    @SuppressWarnings("unchecked")
    public static <T> List<T> of() {
        return Collections.EMPTY_LIST;
    }

    public static <T> List<T> of(T element) {
        return new SingleImmutableList<>(element);
    }

    private static class SingleImmutableList<T> extends AbstractList<T> {
        private final T element;

        public SingleImmutableList(T element) {
            this.element = element;
        }

        @Override
        public T get(int index) {
            return element;
        }

        @Override
        public int size() {
            return 1;
        }
    }
}
