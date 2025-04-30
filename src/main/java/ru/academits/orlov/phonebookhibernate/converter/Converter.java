package ru.academits.orlov.phonebookhibernate.converter;

import java.util.List;

public interface Converter<S, D> {
    D convert(S source);

    default List<D> convert(List<S> source) {
        return source.stream()
                .map(this::convert)
                .toList();
    }
}
