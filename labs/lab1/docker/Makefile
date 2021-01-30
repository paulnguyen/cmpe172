all: clean

clean:
	gradle clean

compile:
	gradle build

jar: compile
	gradle bootJar

run: jar
	echo Starting Spring at:  http://localhost:8080
	java -jar build/libs/demo-docker-1.0.jar

# Docker

docker-build: jar
	docker build -t spring-hello .
	docker images

docker-run: docker-build
	docker run --name spring-hello -td -p 80:8080 spring-hello	
	docker ps

docker-clean:
	docker stop spring-hello
	docker rm spring-hello
	docker rmi spring-hello

docker-shell:
	docker exec -it spring-hello bash 

docker-push:
	docker login
	docker build -t $(account)/spring-hello:latest -t $(account)/spring-hello:latest .
	docker push $(account)/spring-hello:latest 

# Pod

pod-run:
	kubectl apply -f pod.yaml

pod-list:
	kubectl get pods

pod-desc:
	kubectl describe pods spring-hello

pod-delete:
	kubectl delete -f pod.yaml

pod-shell:
	kubectl exec -it spring-hello -- /bin/bash

pod-logs:
	kubectl logs -f spring-hello

# Service

service-create:
	kubectl create -f service.yaml

service-get:
	kubectl get services

service-get-ip:
	kubectl get service spring-hello -o wide

service-delete:
	kubectl delete service spring-hello