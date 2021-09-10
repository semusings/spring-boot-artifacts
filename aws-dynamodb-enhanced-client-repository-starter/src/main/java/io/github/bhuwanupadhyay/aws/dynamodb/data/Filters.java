package io.github.bhuwanupadhyay.aws.dynamodb.data;


import io.github.bhuwanupadhyay.aws.dynamodb.errors.Rejects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Filters {

    private final List<Filter> filters;

    private Filters() {
        this.filters = new ArrayList<>();
    }

    private Filters(List<Filter> filters) {
        this.filters = filters;
    }

    public static Filters create() {
        return new Filters();
    }

    public static Filters combines(Filters... filters) {
        return new Filters(Arrays.stream(filters).filter(Objects::nonNull).map(Filters::getFilters).flatMap(List::stream).collect(Collectors.toList()));
    }

    public Filters with(Filter filter) {
        Rejects.ifNull(filter, () -> "Filter must be not null");
        this.filters.add(filter);
        return this;
    }

    public String toRSQL() {
        List<String> list = new ArrayList<>();
        for (Filter filter : filters) {
            switch (filter.getOperator()) {
                case IN:
                    list.add(filter.getKey() + "=in=(" + filter.getValuesAsCommaJoiner() + ")");
                    break;
                case NOT_IN:
                    list.add(filter.getKey() + "=out=(" + filter.getValuesAsCommaJoiner() + ")");
                    break;
                case NOT_EQUALS:
                    list.add(filter.getKey() + "!=" + filter.getValue());
                    break;
                case EQUALS:
                    list.add(filter.getKey() + "==" + filter.getValue());
                    break;
            }
        }
        return String.join(";", list);
    }

    public boolean isEmpty() {
        return filters.isEmpty();
    }

    public List<Filter> getFilters() {
        return List.copyOf(filters);
    }
}
