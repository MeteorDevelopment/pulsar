package org.meteordev.pulsar.utils;

public interface ICharFilter {
    boolean filter(String text, char c, int cursor);
}
