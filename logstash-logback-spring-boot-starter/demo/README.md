# Getting Started

### ELK Stack

Before running your application you should start ELK Stack on your machine.

The best way to do that is through Docker containers.

- Create a Docker network to enable communication between containers via container name.

```shell script
 docker network create elk
```

- Run elasticsearch docker container.

```shell script
docker run -d --name elasticsearch --net elk -p 9200:9200 -e "discovery.type=single-node" elasticsearch:7.9.2
```

- Create logstash configuration file `logstash.conf`.

```shell script
cat <<EOF> ~/logstash.conf
input {
  tcp {
    port => 5044
    codec => json_lines
  }
}
output {
  elasticsearch {
    hosts => ["http://elasticsearch:9200"]
    index => "example-%{appname}-%{env}"
  }
}
EOF
```

- Run logstash docker container.

```shell script
docker run -d --name logstash --net elk -p 5044:5044 -v ~/logstash.conf:/usr/share/logstash/pipeline/logstash.conf logstash:7.9.2
```

- Run kibana docker container.
```shell script
docker run -d --name kibana --net elk -e "ELASTICSEARCH_URL=http://elasticsearch:9200" -p 5601:5601 kibana:7.9.2
```

### Greeting Service

- Run the service and then check url [http://localhost:9200/_cat/indices](http://localhost:9200/_cat/indices) and look your index:
```
example-greeting-service-dev
```
- Open kibana dashboard [http://localhost:5601](http://localhost:5601/) and create an index:
```
example-greeting*
``` 
- Run discover and add filter message, then you will see logging:
```
10 hits
Documents
message
	Started DemoApplication in 1.251 seconds (JVM running for 1.714)
	Tomcat started on port(s): 8080 (http) with context path ''
	Initializing Servlet 'dispatcherServlet'
	Initializing Spring DispatcherServlet 'dispatcherServlet'
	Completed initialization in 3 ms
	Greeting Initialized at 2902820304765
	Greeting Initialized at 2904734799104
	Greeting Initialized at 2905670902001
	Greeting Initialized at 2906859407908
	Greeting Initialized at 2907493282685
```

Thank you for reading!