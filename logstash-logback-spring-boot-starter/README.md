# Logstash Logback Spring Boot Starter

Library for Logstash logback for ELK with zero configuration

## Usage

### Add dependency in your spring boot project

- Maven

```xml
<dependency>
    <groupId>io.github.bhuwanupadhyay</groupId>
    <artifactId>logstash-logback-spring-boot-starter</artifactId>
    <version>[version]</version>
</dependency>
```

- Gradle

```
implementation 'io.github.bhuwanupadhyay:logstash-logback-spring-boot-starter:[version]'
```

### Customize configurations

Autoconfiguration enable logstash logging automatically, and connected with logstash server url `localhost:5044`.

In case if you have to change logstash server url, override following properties:

```yaml
boot:
  logstash:
    destination: localhost:5044
```

By default custom fields are:
```json
{ "appname" : "<spring.application.name>" }
```

To disable logstash logging, override following properties:

```yaml
boot:
  logstash:
    enabled: false
```

All configuration properties:

```yaml
boot:
  logstash:
    destination: localhost:5044
    enabled: true
    key-store-location: keystore/trust.pk
    key-store-password: 12345  
    custom-fields: |-
      {"appname":"${spring.application.name}", "env": "${spring.profiles.active}"}
    queue-size: 512
``` 

## Demo

[Greeting Service](demo)