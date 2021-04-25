all: clean

clean:
	gradle clean

compile:
	gradle build

test:
	gradle test

jar: 
	gradle build -x test
	gradle bootJar

run: jar
	echo Starting Spring at:  http://localhost:8080
	java -jar build/libs/tacos-cloud-1.0.jar

# Docker

docker-build: jar
	docker build -t tacos-cloud .
	docker images

docker-run: docker-build
	docker run --name tacos-cloud -td -p 80:8080 tacos-cloud	
	docker ps

docker-clean:
	docker stop tacos-cloud
	docker rm tacos-cloud
	docker rmi tacos-cloud

docker-shell:
	docker exec -it tacos-cloud bash 

docker-push:
	docker login
	docker build -t $(account)/tacos-cloud:v1 -t $(account)/tacos-cloud:v1 .
	docker push $(account)/tacos-cloud:v1 

# Pod

pod-run:
	kubectl apply -f pod.yaml

pod-list:
	kubectl get pods

pod-desc:
	kubectl describe pods tacos-cloud

pod-delete:
	kubectl delete -f pod.yaml

pod-shell:
	kubectl exec -it tacos-cloud -- /bin/bash

pod-logs:
	kubectl logs -f tacos-cloud

# Service

service-create:
	kubectl create -f service.yaml

service-get:
	kubectl get services

service-get-ip:
	kubectl get service tacos-cloud -o wide

service-delete:
	kubectl delete service tacos-cloud