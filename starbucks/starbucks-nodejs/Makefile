all: clean

clean: 
	find . -name '*.log' -type f -exec rm -f {} \; 

run:
	node app.js

docker-build: 
	docker build -t starbucks-nodejs .
	docker images

docker-clean:
	docker stop nodejs
	docker rm nodejs

docker-shell:
	docker exec -it starbucks bash 

network-create:
	docker network create --driver bridge starbucks

network-inspect:
	docker network inspect starbucks

docker-run:
	docker run --network starbucks --name starbucks-nodejs -p 90:9090  \
	-e "api_endpoint=http://spring-starbucks-api:8080" -td starbucks-nodejs

docker-push:
	docker login
	docker build -t $(account)/starbucks-nodejs:latest .
	docker push $(account)/starbucks-nodejs:latest
	docker build -t $(account)/starbucks-nodejs:v3.0 .
	docker push $(account)/starbucks-nodejs:v3.0

			