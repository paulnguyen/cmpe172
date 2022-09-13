clean:
	mvn clean

compile:
	mvn compile

run:
	mvn spring-boot:run

build:
	mvn package

run-jar:
	java -jar target/spring-demo-1.0.jar

run-jar-8081:
	java -jar target/spring-demo-1.0.jar --server.port=8081

run-jar-8082:
	java -jar target/spring-demo-1.0.jar --server.port=8082


# Docker

docker-build: build
	docker build --platform=linux/amd64 -t spring-demo .
	docker images

docker-run: docker-build
	docker run --name spring-demo --platform=linux/amd64 -td -p 80:8080 spring-demo
	docker ps

docker-clean:
	docker stop spring-demo
	docker rm spring-demo
	docker rmi spring-demo

docker-shell:
	docker exec -it spring-demo bash

docker-push:
	docker login
	docker build -t $(account)/spring-demo:latest -t $(account)/spring-demo:latest .
	docker push $(account)/spring-demo:latest

# Pod

pod-run:
	kubectl apply -f pod.yaml

pod-list:
	kubectl get pods

pod-desc:
	kubectl describe pods spring-demo

pod-delete:
	kubectl delete -f pod.yaml

pod-shell:
	kubectl exec -it spring-demo -- /bin/bash

pod-logs:
	kubectl logs -f spring-demo

# Service

service-create:
	kubectl create -f service.yaml

service-get:
	kubectl get services

service-get-ip:
	kubectl get service spring-demo -o wide

service-delete:
	kubectl delete service spring-demo