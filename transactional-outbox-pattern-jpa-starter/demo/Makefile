infra:
	docker-compose rm -f -s -v && docker-compose up -d
app:
	cd .. && mvn clean install && cd demo && mvn clean spring-boot:run
test:
	k6 run k6-script.js --vus=10 --duration=60s