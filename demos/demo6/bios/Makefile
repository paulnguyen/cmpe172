all: db-clean clean

db-clean:
	docker stop mysql 
	docker rm mysql 
	docker stop mongo 
	docker rm mongo 
	docker stop redis
	docker rm redis

## Databases on Docker

clean-mysql:
	docker stop mysql 
	docker rm mysql 

mysql:
	docker run -d --name mysql -td -p 3306:3306 -e MYSQL_ROOT_PASSWORD=cmpe172 mysql:8.0

mysql-shell:
	docker exec -it mysql bash 

clean-mongo:
	docker stop mongo 
	docker rm mongo 

mongo:
	docker run --name mongo -p 27017:27017  -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=cmpe172 -d mongo:4.4

mongo-shell:
	docker exec -it mongo bash 

clean-redis: 
	docker stop redis
	docker rm redis

redis:
	docker run --name redis -td -p 6379:6379 redis:4.0

redis-shell:
	docker exec -it redis bash 






