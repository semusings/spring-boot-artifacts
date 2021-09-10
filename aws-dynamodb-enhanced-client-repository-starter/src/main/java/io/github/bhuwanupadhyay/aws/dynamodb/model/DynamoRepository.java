package io.github.bhuwanupadhyay.aws.dynamodb.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamoRepository {

    String tableName();

    Class<?> entityClass();

}
