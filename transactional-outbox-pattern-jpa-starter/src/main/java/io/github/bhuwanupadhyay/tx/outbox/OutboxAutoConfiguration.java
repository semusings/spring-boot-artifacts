package io.github.bhuwanupadhyay.tx.outbox;

import io.github.bhuwanupadhyay.tx.outbox.config.OutboxEventSource;
import io.github.bhuwanupadhyay.tx.outbox.config.OutboxProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableConfigurationProperties({OutboxProperties.class})
@EnableBinding(OutboxEventSource.class)
@EnableScheduling
@Slf4j
@PropertySource("classpath:outbox.properties")
@ComponentScan(basePackageClasses = {OutboxAutoConfiguration.class})
@EnableJpaRepositories(basePackageClasses = {OutboxAutoConfiguration.class})
@EntityScan(basePackageClasses = {OutboxAutoConfiguration.class})
public class OutboxAutoConfiguration {

    @Bean
    public ApplicationRunner outboxApplicationRunner(Environment environment, OutboxProperties properties) {
        return args -> {
            log.info("Outbox events binding destination: {}", environment.getProperty("spring.cloud.stream.bindings.outbox.destination"));
            log.info("Outbox max events per polling: {}", properties.getMaxEventsPerPolling());
            log.info("Outbox polling interval in milliseconds: {}", properties.getPollingIntervalInMilliseconds());
        };
    }

}
