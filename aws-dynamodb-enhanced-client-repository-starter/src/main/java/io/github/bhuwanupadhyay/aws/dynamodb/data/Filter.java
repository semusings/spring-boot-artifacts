package io.github.bhuwanupadhyay.aws.dynamodb.data;


import io.github.bhuwanupadhyay.aws.dynamodb.errors.Rejects;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Filter {

    private final String key;
    private final Operator operator;
    private final Object value;
    private final Set<String> values; //Used in case of IN operator

    private Filter(String key, Operator operator, Object value) {
        this.key = key;
        this.operator = operator;
        this.value = value;
        this.values = null;
    }

    public Filter(String key, Operator operator, Set<String> values) {
        this.key = key;
        this.operator = operator;
        this.value = null;
        this.values = Optional.ofNullable(values).orElseGet(HashSet::new);
    }

    public static Filter eq(String key, String value) {
        Rejects.ifBlank(key, () -> "Filter key must be not blank");
        Rejects.ifBlank(value, () -> "Filter value must be not blank");
        return new Filter(key, Operator.EQUALS, value);
    }

    public static Filter eq(String key, boolean value) {
        Rejects.ifBlank(key, () -> "Filter key must be not blank");
        return new Filter(key, Operator.EQUALS, value);
    }

    public static Filter in(String key, String... values) {
        return in(key, Stream.of(values).collect(Collectors.toSet()));
    }

    public static Filter in(String key, Set<String> values) {
        Rejects.ifBlank(key, () -> "Filter key must be not blank");
        Rejects.ifIsEmptyOrHasBlanks(values, () -> "Filter values must be not blank");
        return new Filter(key, Operator.IN, values);
    }

    public static Filter notIn(String key, Set<String> values) {
        Rejects.ifNull(key, () -> "Filter key must be not blank");
        Rejects.ifIsEmptyOrHasBlanks(values, () -> "Filter values must be not blank");
        return new Filter(key, Operator.NOT_IN, values);
    }

    public String getValuesAsCommaJoiner() {
        return String.join(",", Optional.ofNullable(this.values).orElseGet(HashSet::new));
    }

    public Set<String> getValues() {
        return values != null ? Set.copyOf(values) : null;
    }

    public Object getValue() {
        return value;
    }

    public Operator getOperator() {
        return operator;
    }

    public String getKey() {
        return key;
    }
}
