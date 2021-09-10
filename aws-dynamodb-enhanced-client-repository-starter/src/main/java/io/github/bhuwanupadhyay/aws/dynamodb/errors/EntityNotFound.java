package io.github.bhuwanupadhyay.aws.dynamodb.errors;

public class EntityNotFound extends RuntimeException {

    public EntityNotFound(String id) {
        super(String.format("Entity not found for id [%s]", id));
    }

}
