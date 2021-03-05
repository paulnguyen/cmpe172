
all: clean

clean: 
	docker stop redis
	docker rm redis

redis:
	docker run --name redis -td -p 6379:6379 redis:4.0

redis-shell:
	docker exec -it redis bash 


