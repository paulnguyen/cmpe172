
all: clean

clean: 
	find . -name 'starbucks' -type f -exec rm -f {} \; 
	go clean

go-get:
	rm -rf src/github.com
	go get -v github.com/codegangsta/negroni
	go get -v github.com/gorilla/mux
	go get -v github.com/unrolled/render
	go get -v github.com/satori/go.uuid

format:
	go fmt starbucks

install:
	go install starbucks

build:
	go build starbucks

run: build
	./starbucks 

docker-setup:
	docker network create --driver bridge starbucks

docker-launch:
	docker run --network starbucks --name starbucks-api -p 3000:3000 -td paulnguyen/starbucks-api:v1.0


#####################################################


ping:
	curl localhost:3000/ping

get-cards:
	curl localhost:3000/cards

get-card:
	curl localhost:3000/cards/$(num)

new-card:
	curl -X POST localhost:3000/cards

new-order:
	curl --request POST \
  		 --url http://localhost:3000/order/register/123 \
  		 --header 'content-type: application/json' \
  		 --data '{ "Drink": "latte", "Milk":  "whole", "Size":  "large"}'

get-order:
	curl --request GET --url http://localhost:3000/order/register/123

cancel-order:
	curl --request DELETE --url http://localhost:3000/order/register/123

pay-for-order:
	curl --request POST --url http://localhost:3000/order/register/123/pay/$(num)


#####################################################

docker-build: 
	docker build -t starbucks .
	docker images

docker-shell:
	docker exec -it starbucks bash 

network-create:
	docker network create --driver bridge starbucks

network-inspect:
	docker network inspect starbucks

docker-run:
	docker run \
            --network starbucks \
			--name starbucks -p 3000:3000 -td starbucks
	docker ps

kong-database:
	docker run -d --name kong-database --network starbucks -p 9042:9042 cassandra:2.2

kong-run:
	docker run -d --name kong \
	          --network starbucks \
              -e "KONG_DATABASE=cassandra" \
              -e "KONG_CASSANDRA_CONTACT_POINTS=kong-database" \
              -e "KONG_PG_HOST=kong-database" \
              -p 8000:8000 \
              -p 8443:8443 \
              -p 8001:8001 \
              -p 7946:7946 \
              -p 7946:7946/udp \
              kong:0.9.9

docker-network:
	docker network ls

docker-network-prune:
	docker network prune

docker-clean:	
	docker stop kong-database
	docker rm kong-database
	docker stop kong
	docker rm kong
	docker stop starbucks
	docker rm starbucks
	docker rmi starbucks
	docker network prune

docker-ps:
	 docker ps --all --format "table {{.ID}}\t{{.Names}}\t{{.Image}}\t{{.Status}}\t"

docker-ps-ports:
	 docker ps --all --format "table {{.Names}}\t{{.Ports}}\t"


#####################################################


kong-add-api:
	curl -X POST \
  	http://localhost:8001/apis \
  	-H 'Content-Type: application/json' \
  	-d '{ \
	"name": "starbucks", \
	"request_path": "/starbucks", \
	"strip_request_path": "true", \
	"preserve_host": "true", \
	"upstream_url": "http://starbucks:3000/" \
	}'

kong-add-auth:
	curl -X POST \
  	http://localhost:8001/apis/starbucks/plugins \
    -H 'content-type: multipart/form-data;' \
    -F name=key-auth

kong-add-client:
	curl -X POST \
  	http://localhost:8001/consumers/ \
    -H 'content-type: multipart/form-data;' \
    -F username=apiclient

kong-add-apikey:
	curl -X POST \
  	http://localhost:8001/consumers/apiclient/key-auth \
    -H 'Content-Type: text/plain'


#####################################################


kong-ping:
	curl -X GET \
  	http://localhost:8000/starbucks/ping \
  	-H 'Content-Type: application/json' \
  	-H 'apikey: $(key)'

kong-get-cards:
	curl -X GET \
  	http://localhost:8000/starbucks/cards \
  	-H 'Content-Type: application/json' \
  	-H 'apikey: $(key)'

kong-get-card:
	curl -X GET \
  	http://localhost:8000/starbucks/cards/$(num) \
  	-H 'Content-Type: application/json' \
  	-H 'apikey: $(key)'

kong-new-card:
	curl -X POST \
  	http://localhost:8000/starbucks/cards \
  	-H 'Content-Type: application/json' \
  	-H 'apikey: $(key)'

kong-new-order:
	curl -X POST \
  	http://localhost:8000/starbucks/order/register/123 \
  	-H 'Content-Type: application/json' \
  	-H 'apikey: $(key)' \
  	--data '{ "Drink": "latte", "Milk":  "whole", "Size":  "large"}'

kong-get-order:
	curl -X GET \
  	http://localhost:8000/starbucks/order/register/123 \
  	-H 'Content-Type: application/json' \
  	-H 'apikey: $(key)' 

kong-cancel-order:
	curl -X DELETE \
  	http://localhost:8000/starbucks/order/register/123 \
  	-H 'Content-Type: application/json' \
  	-H 'apikey: $(key)' 

kong-pay-for-order:
	curl -X POST \
  	http://localhost:8000/starbucks/order/register/123/pay/$(num) \
  	-H 'Content-Type: application/json' \
  	-H 'apikey: $(key)' 



