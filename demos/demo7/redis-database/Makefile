clean:
	docker stop redis
	docker rm redis

network:
	docker network create --driver bridge gumball

redis:
	docker run --name redis --network gumball -td -p 6379:6379 redis:4.0


shell:
	docker exec -it redis bash 

