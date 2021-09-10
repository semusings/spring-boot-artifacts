package io.github.bhuwanupadhyay.aws.dynamodb.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "boot.dynamodb")
public class DynamoDbProperties {

    private String endpointUri;

    private String region;
}
