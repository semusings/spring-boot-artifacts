package io.github.bhuwanupadhyay.aws.dynamodb;

import io.github.bhuwanupadhyay.aws.dynamodb.config.DynamoDbProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;


@Configuration
@EnableConfigurationProperties(DynamoDbProperties.class)
public class DynamoDbAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DynamoDbEnhancedClient.class)
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
    }

    @Bean
    @ConditionalOnMissingBean(DynamoDbClient.class)
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder().build();
    }

}
