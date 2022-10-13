clean: docker-build
	
docker-clean:
	docker stop redis
	docker rm redis
	docker rmi redis

docker-build: 
	docker build --platform=linux/amd64 -t redis .
	docker images

redis-local:
	docker run --platform=linux/amd64 --name redis --network gumball -td -p 6379:6379 redis

redis-official:
	docker run --platform=linux/amd64 --name redis --network gumball -td -p 6379:6379 redis:4.0

