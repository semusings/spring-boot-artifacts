package io.github.bhuwanupadhyay.aws.dynamodb.errors;

public class ViolationError extends RuntimeException {

    public ViolationError(String message) {
        super(message);
    }

    public ViolationError(String message, Exception ex) {
        super(message, ex);
    }

}
