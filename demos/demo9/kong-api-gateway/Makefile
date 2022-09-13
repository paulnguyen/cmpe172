
all: clean 

clean:
	docker stop kong
	docker rm kong 
	docker stop kong-database
	docker rm kong-database
	docker stop starbucks-api
	docker rm starbucks-api

docker-shell:
	docker exec -it starbucks bash 

kong-shell:
	docker exec -it kong bash 

network-create:
	docker network create --driver bridge kong-network

network-inspect:
	docker network inspect kong-network

starbucks-api:	
	docker run --platform=linux/amd64 -d --name starbucks-api --network kong-network -td spring-starbucks-api

jumpbox:
	docker run --platform=linux/amd64 --name jumpbox --network kong-network -td ubuntu

jumpbox-shell:
	docker exec -it jumpbox bash

jumpbox-tools:
	apt-get update
	apt-get install curl
	apt-get install iputils-ping
	apt-get install telnet
	apt-get install httpie

install-curl:
	apt-get update; apt-get install curl
	yum install curl
	apk --no-cache add curl

kong-db:
	docker run --platform=linux/amd64 -d --name kong-database \
               --network=kong-network \
               -p 5432:5432 \
               -e "POSTGRES_USER=kong" \
               -e "POSTGRES_DB=kong" \
               -e "POSTGRES_PASSWORD=kong" \
               postgres:9.6

kong-setup:
	docker run --platform=linux/amd64 --rm \
     --network=kong-network \
     -e "KONG_DATABASE=postgres" \
     -e "KONG_PG_HOST=kong-database" \
     -e "KONG_PG_USER=kong" \
     -e "KONG_PG_PASSWORD=kong" \
     -e "KONG_CASSANDRA_CONTACT_POINTS=kong-database" \
     kong:2.4.0 kong migrations bootstrap

kong:
	docker run --platform=linux/amd64 -d --name kong \
     --network=kong-network \
     -e "KONG_DATABASE=postgres" \
     -e "KONG_PG_HOST=kong-database" \
     -e "KONG_PG_USER=kong" \
     -e "KONG_PG_PASSWORD=kong" \
     -e "KONG_CASSANDRA_CONTACT_POINTS=kong-database" \
     -e "KONG_PROXY_ACCESS_LOG=/dev/stdout" \
     -e "KONG_ADMIN_ACCESS_LOG=/dev/stdout" \
     -e "KONG_PROXY_ERROR_LOG=/dev/stderr" \
     -e "KONG_ADMIN_ERROR_LOG=/dev/stderr" \
     -e "KONG_ADMIN_LISTEN=0.0.0.0:8001, 0.0.0.0:8444 ssl" \
     -p 80:8000 \
     -p 443:8443 \
     -p 127.0.0.1:8001:8001 \
     -p 127.0.0.1:8444:8444 \
     kong:2.4.0

kong-dbless:
	docker run --platform=linux/amd64 -d --name kong \
    --network=kong-network \
    -e "KONG_DATABASE=off" \
    -e "KONG_PROXY_ACCESS_LOG=/dev/stdout" \
    -e "KONG_ADMIN_ACCESS_LOG=/dev/stdout" \
    -e "KONG_PROXY_ERROR_LOG=/dev/stderr" \
    -e "KONG_ADMIN_ERROR_LOG=/dev/stderr" \
    -e "KONG_ADMIN_LISTEN=0.0.0.0:8001, 0.0.0.0:8444 ssl" \
    -p 80:8000 \
    -p 443:8443 \
    -p 8001:8001 \
    -p 8444:8444 \
    kong:2.4.0

kong-get-config:
	docker exec -it kong kong config init /home/kong/kong.yml
	docker exec -it kong cat /home/kong/kong.yml >> kong-initial.yml

kong-load-config:
	http :8001/config config=@kong.yml

kong-reload-config:
	docker exec -it kong kong reload

kong-ping:
	http localhost/api/ping apikey:2H3fONTa8ugl1IcVS7CjLPnPIS2Hp9dJ


jumpbox:
    docker run --platform=linux/amd64 --network test --name jumpbox -t -d ubuntu

shell:
    docker exec -it jumpbox bash 


gke-jumpbox-create:
    kubectl create -f jumpbox.yaml

gke-jumpbox-get:
    kubectl get pod jumpbox

gke-jumpbox-shell:
    kubectl exec  -it jumpbox -- /bin/bash

gke-jumpbox-delete:
    kubectl delete pod jumpbox

gke-jumpbox-tools:
    apt-get update
    apt-get install curl
    apt-get install iputils-ping








