# Transactional Outbox Pattern JPA Starter

Library for Transaction Outbox Pattern using Spring Boot JPA with zero configuration.

## Usage

### Add dependency in your spring boot project

- Maven

```xml
<dependency>
    <groupId>io.github.bhuwanupadhyay</groupId>
    <artifactId>transactional-outbox-pattern-jpa-starter</artifactId>
    <version>[version]</version>
</dependency>
```

- Gradle

```
implementation 'io.github.bhuwanupadhyay:transactional-outbox-pattern-jpa-starter:[version]'
```

### Customize configurations

```yaml
boot:
  outbox:
    max-events-per-polling: 50
    polling-interval-in-milliseconds: 10000
```

### Using `OutboxPublisher`

```
@Autowired
private OutboxPublisher outboxPublisher;
```

### Demo Example

You can find demo example [Demo App](demo) 
