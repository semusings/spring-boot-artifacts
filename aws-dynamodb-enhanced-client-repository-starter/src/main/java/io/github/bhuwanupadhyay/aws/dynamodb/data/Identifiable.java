package io.github.bhuwanupadhyay.aws.dynamodb.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbIgnore;

public interface Identifiable {

    @DynamoDbIgnore
    @JsonIgnore
    String getId();
}
