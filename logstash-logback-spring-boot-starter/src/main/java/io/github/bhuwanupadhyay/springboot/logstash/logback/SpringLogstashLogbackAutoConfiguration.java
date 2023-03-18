package io.github.bhuwanupadhyay.springboot.logstash.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.net.ssl.KeyStoreFactoryBean;
import ch.qos.logback.core.net.ssl.SSLConfiguration;
import net.logstash.logback.appender.LogstashTcpSocketAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import net.logstash.logback.stacktrace.ShortenedThrowableConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

@Configuration
@ConfigurationProperties(prefix = "boot.logstash")
public class SpringLogstashLogbackAutoConfiguration {
  private static final Logger log =
      LoggerFactory.getLogger(SpringLogstashLogbackAutoConfiguration.class);

  private static final String ASYNC_LOGSTASH_APPENDER_NAME = "ASYNC_LOGSTASH";
  @Value("${spring.application.name:-}")
  private String name;
  private String destination = "localhost:5044";
  private String keyStoreLocation;
  private String keyStorePassword;
  private String customFields;
  private int queueSize = 512;

  @Bean
  @ConditionalOnProperty(value = "boot.logstash.enabled", matchIfMissing = true)
  public LogstashTcpSocketAppender logstashAppender() {
    if (customFields == null) {
      Assert.hasText(name, "property spring.application.name required!");
      this.customFields = "{\"appname\":\"" + name + "\"}";
    }
    log.info("Initializing Logstash...");
    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
    LogstashTcpSocketAppender appender = new LogstashTcpSocketAppender();
    appender.setName(ASYNC_LOGSTASH_APPENDER_NAME);
    appender.setQueueSize(this.getQueueSize());
    appender.addDestination(this.getDestination());
    appender.setContext(context);

    if (this.getKeyStoreLocation() != null) {
      SSLConfiguration sslConfiguration = new SSLConfiguration();
      KeyStoreFactoryBean factory = new KeyStoreFactoryBean();
      factory.setLocation(this.getKeyStoreLocation());
      if (this.getKeyStorePassword() != null) {
        factory.setPassword(this.getKeyStorePassword());
      }
      sslConfiguration.setTrustStore(factory);
      appender.setSsl(sslConfiguration);
    }

    LogstashEncoder encoder = new LogstashEncoder();
    encoder.setThrowableConverter(throwableConverter());
    encoder.setCustomFields(this.getCustomFields());
    appender.setEncoder(encoder);
    appender.start();

    context.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME).addAppender(appender);

    return appender;
  }

  private ShortenedThrowableConverter throwableConverter() {
    final ShortenedThrowableConverter throwableConverter = new ShortenedThrowableConverter();
    throwableConverter.setRootCauseFirst(true);
    return throwableConverter;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public String getKeyStoreLocation() {
    return keyStoreLocation;
  }

  public void setKeyStoreLocation(String keyStoreLocation) {
    this.keyStoreLocation = keyStoreLocation;
  }

  public String getKeyStorePassword() {
    return keyStorePassword;
  }

  public void setKeyStorePassword(String keyStorePassword) {
    this.keyStorePassword = keyStorePassword;
  }

  public int getQueueSize() {
    return queueSize;
  }

  public void setQueueSize(int queueSize) {
    this.queueSize = queueSize;
  }

  public String getCustomFields() {
    return customFields;
  }

  public void setCustomFields(String customFields) {
    this.customFields = customFields;
  }

  public String getName() {
    return name;
  }
}
