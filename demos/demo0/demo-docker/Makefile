clean:
	mvn clean

compile:
	mvn compile

run:
	mvn spring-boot:run

build:
	mvn package

run-jar:
	java -jar target/demo-docker-1.0.jar


# Docker

docker-clean:
	docker rm demo-docker1 
	docker rm demo-docker2 

docker-build: build
	docker build --platform=linux/amd64 -t demo-docker .
	docker images

docker-run: docker-build
	docker run --name demo-docker1 --platform=linux/amd64 -td -p 81:8080 demo-docker 
	docker run --name demo-docker2 --platform=linux/amd64 -td -p 82:8080 demo-docker 
	docker ps

docker-clean:
	docker stop spring-demo
	docker rm spring-demo
	docker rmi spring-demo

docker-shell:
	docker exec -it demo-docker1 bash

