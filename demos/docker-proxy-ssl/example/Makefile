all: clean

clean:
	gradle clean

docker-clean:
	docker stop spring-gumball
	docker rm spring-gumball


# Shells

gumball-shell:
	docker exec -it spring-gumball bash 


# Docker Deployments

gumball-run: 
	docker run --name spring-gumball --network gumball -td -p 8080 spring-gumball	
	

# Docker Compose

compose-up:
	docker-compose up -d

compose-down:
	docker-compose down 	


# Docker Admin Commands

network-create:
	docker network create --driver bridge gumball

network-inspect:
	docker network inspect gumball

docker-network:
	docker network ls

docker-network-prune:
	docker network prune

docker-network-inspect:
	docker network inspect host

docker-ip:
	docker-machine ip

docker-ps:
	 docker ps --all --format "table {{.ID}}\t{{.Names}}\t{{.Image}}\t{{.Status}}\t"

docker-ps-ports:
	 docker ps --all --format "table {{.Names}}\t{{.Ports}}\t"


# Kubernetes Deployment

deployment-create:
	kubectl create -f deployment.yaml --save-config 

deployment-get:
	kubectl get deployments

deployment-get-pods:
	kubectl get pods -l name=spring-gumball

deployment-pod-shell:
	kubectl exec -it $(pod) -- /bin/bash

deployment-upgrade:
	kubectl apply  -f deployment.yaml

deployment-delete:
	kubectl delete deployment spring-gumball-deployment


# Service

service-create:
	kubectl create -f service.yaml

service-get:
	kubectl get services

service-get-ip:
	kubectl get service spring-gumball -o wide

service-delete:
	kubectl delete service spring-gumball


# Jumpbox

jumpbox-create:
	kubectl create  -f jumpbox.yaml

jumpbox-get:
	kubectl get pod jumpbox

jumpbox-shell:
	kubectl exec  -it jumpbox -- /bin/bash

jumpbox-delete:
	kubectl delete pod jumpbox

jumpbox-tools:
	apt-get update
	apt-get install curl
	apt-get install iputils-ping




