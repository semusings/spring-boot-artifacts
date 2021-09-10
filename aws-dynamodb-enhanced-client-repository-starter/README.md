# AWS DynamoDB Enhanced Client Repository Starter

Library for AWS DynamoDB Enhanced Client for DynamoRepository with zero configuration.

## Usage

### Add dependency in your spring boot project

- Maven

```xml
<dependency>
    <groupId>io.github.bhuwanupadhyay</groupId>
    <artifactId>aws-dynamodb-enhanced-client-repository-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

- Gradle

```
implementation 'io.github.bhuwanupadhyay:aws-dynamodb-enhanced-client-repository-starter:1.0.0'
```

### Customize configurations

```yaml
boot:
  dynamodb:
    endpoint-uri: http://localhost:4566
    region: us-east-1
```


### Using `AbstractDynamoRepository`

```
@Repository
@DynamoRepository(tableName = "Payments", entityClass = Payment.class)
public class PaymentRepository extends AbstractDynamoRepository<Payment> {

}

```

### Demo Example

You can find demo example [Demo App](demo) 
