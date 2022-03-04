
all: clean

clean:
	docker stop mongo 
	docker rm mongo 

mongo:
	docker run --network test --name mongo -p 27017:27017  -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=cmpe172 -d mongo:4.4

mongo-shell:
	docker exec -it mongo bash 