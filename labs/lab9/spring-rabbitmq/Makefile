
all: clean

clean: 
	mvm clean

docker-clean:
	docker stop rabbitmq
	docker rm rabbitmq

network-create:
	docker network create --driver bridge springdemo

network-inspect:
	docker network inspect springdemo

network-ls:
	docker network ls

shell:
	docker exec -it rabbitmq bash 

rabbit:
	docker run --name rabbitmq \
	           --network springdemo \
			   -p 8080:15672 -p 4369:4369 -p 5672:5672 \
			   -d rabbitmq:3-management
console:
	open http://localhost:8080

# Management Console: http://localhost:8080
# username and password of guest / guest:

compile:
	mvn compile

run: compile
	mvn spring-boot:run

build:
	mvn package

# Docker

docker-build: jar
	docker build -t spring-rabbitmq .
	docker images

docker-run: docker-build
	docker run --network springdemo --name spring-rabbitmq -td -p 80:8080 spring-rabbitmq	

docker-shell:
	docker exec -it spring-rabbitmq bash 

docker-push:
	docker login
	docker build -t $(account)/spring-rabbitmq:v1.0 -t $(account)/spring-rabbitmq:v1.0 .
	docker push $(account)/spring-rabbitmq:v1.0:v1.0 


# Kubernetes Deployment

deployment-create:
	kubectl create -f deployment.yaml --save-config 

deployment-get:
	kubectl get deployments

deployment-get-pods:
	kubectl get pods -l name=spring-rabbitmq

deployment-pod-shell:
	kubectl exec -it $(pod) -- /bin/bash

deployment-upgrade:
	kubectl apply  -f deployment.yaml

deployment-delete:
	kubectl delete deployment spring-rabbitmq-deployment

# Kubernetes Service

service-create:
	kubectl create -f service.yaml

service-get:
	kubectl get services

service-get-ip:
	kubectl get service spring-rabbitmq-service -o wide

service-delete:
	kubectl delete service spring-rabbitmq-service

# Kubernetes Ingress

ingress-apply:
	kubectl apply -f ingress.yaml

ingress-ip:
	kubectl get ingress spring-rabbitmq-ingress


