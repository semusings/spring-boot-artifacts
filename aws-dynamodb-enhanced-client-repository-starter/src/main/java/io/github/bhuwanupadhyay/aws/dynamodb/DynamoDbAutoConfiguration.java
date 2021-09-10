package io.github.bhuwanupadhyay.aws.dynamodb;

import io.github.bhuwanupadhyay.aws.dynamodb.config.DynamoDbProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.internal.SystemSettingsCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

import java.net.URI;
import java.util.Objects;


@Configuration
@EnableConfigurationProperties(DynamoDbProperties.class)
public class DynamoDbAutoConfiguration {

    private final DynamoDbProperties dynamoDbProperties;

    public DynamoDbAutoConfiguration(DynamoDbProperties dynamoDbProperties) {
        this.dynamoDbProperties = dynamoDbProperties;
    }

    @Bean
    @ConditionalOnMissingBean(DynamoDbEnhancedClient.class)
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
    }

    @Bean
    @ConditionalOnMissingBean(DynamoDbClient.class)
    public DynamoDbClient dynamoDbClient() {
        DynamoDbClientBuilder builder = DynamoDbClient.builder();

        String endpointUri = dynamoDbProperties.getEndpointUri();
        if (isNotBlank(endpointUri)) {
            builder.endpointOverride(URI.create(endpointUri));
        }

        String region = dynamoDbProperties.getRegion();
        if (isNotBlank(region)) {
            builder.region(Region.of(region));
        }

        return builder.build();
    }

    private boolean isNotBlank(String endpointUri) {
        return Objects.nonNull(endpointUri) && !endpointUri.isBlank();
    }

}
