
all: clean

clean:
	docker stop mysql 
	docker rm mysql 

mysql:
	docker run --network test -d --name mysql -td -p 3306:3306 -e MYSQL_ROOT_PASSWORD=cmpe172 mysql:8.0

mysql-shell:
	docker exec -it mysql bash 

