all: clean

network:
	docker network create --driver bridge starbucks

backend:
	docker run --network starbucks --name starbucks-api -p 3000:3000 -td paulnguyen/starbucks-api:v1.0

cashier:
	docker run --network starbucks --name starbucks-nodejs -p 8080:8080  \
	-e "api_endpoint=http://starbucks-api:3000" -td paulnguyen/starbucks-nodejs:v1.0

