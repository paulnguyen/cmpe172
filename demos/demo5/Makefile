clean:
	docker stop dvwa
	docker stop zap
	docker rm zap
	docker network prune

dvwa-run: 
	docker run --platform=linux/amd64 --name dvwa --network attack --rm -td -p 80:80 vulnerables/web-dvwa
	docker ps

dvwa-shell:
	docker exec -it dvwa bash 

zap-run:
	docker run --platform=linux/amd64 --name zap --network attack -u zap -p 8080:8080 -p 8090:8090 -td owasp/zap2docker-stable zap-webswing.sh
	docker ps

kali-run:
	docker run --platform=linux/amd64 --name kali --network attack --tty --interactive kalilinux/kali-rolling /bin/bash

net:
	docker network create --driver bridge attack

net-ls:
	docker network ls 

net-prune:
	docker network prune

