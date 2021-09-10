package io.github.bhuwanupadhyay.aws.dynamodb.errors;


import java.util.Set;
import java.util.function.Supplier;

public class Rejects {

    public static <T> void ifNull(T value, Supplier<String> message) {
        if (value == null) {
            raise(message);
        }
    }

    public static void ifBlank(String value, Supplier<String> message) {
        if (value != null && !value.trim().isEmpty()) {
            raise(message);
        }
    }

    public static void ifIsEmptyOrHasBlanks(Set<String> values, Supplier<String> message) {
        if (values == null || values.isEmpty() || values.stream().anyMatch(String::isBlank)) {
            raise(message);
        }
    }

    private static void raise(Supplier<String> message) {
        throw new ViolationError(message.get());
    }


}
