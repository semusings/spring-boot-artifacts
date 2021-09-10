# Demo: How to Run?

1. Run docker-compose

```
docker-compose rm -f -s -v && docker-compose up -d
```

2. Run

```
mvn spring-boot:run
```

3. Test

```
curl -X GET http://localhost:8080/outboxes/fire
```
