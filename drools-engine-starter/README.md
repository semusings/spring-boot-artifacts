# Drools Engine Starter

Library for Drools Engine for KieSession with zero configuration

## Usage

### Add dependency in your spring boot project

- Maven

```xml
<dependency>
    <groupId>io.github.bhuwanupadhyay</groupId>
    <artifactId>drools-engine-starter</artifactId>
    <version>[version]</version>
</dependency>
```

- Gradle

```
implementation 'io.github.bhuwanupadhyay:drools-engine-starter:[version]'
```

### Customize configurations

```yaml
boot:
  drools:
    rules-path: payment/rules # default 'drools/rules'
```

According to your rules' path create directory under `src/main/resource/<rule_path>` and place all drools rule file.

### Using `KieSession`

```
@Autowired
private KieContainer kieContainer;
```

### Demo Example

You can find demo example [Test App](src/test/java/io/github/bhuwanupadhyay/drools) 
