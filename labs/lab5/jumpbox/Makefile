all: clean

jumpbox:
	docker run --network test --name jumpbox -t -d ubuntu

shell:
	docker exec -it jumpbox bash 

clean:
	docker stop jumpbox
	docker rm jumpbox

jumpbox-create:
	kubectl create -f jumpbox.yaml

jumpbox-get:
	kubectl get pod jumpbox

jumpbox-shell:
	kubectl exec  -it jumpbox -- /bin/bash

jumpbox-delete:
	kubectl delete pod jumpbox

jumpbox-tools:
	apt-get update
	apt-get install curl
	apt-get install iputils-ping




