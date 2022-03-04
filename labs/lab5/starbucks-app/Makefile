all: clean

clean: 
	find . -name "*.class" -exec rm -rf {} \;
	find . -name .DS_Store -exec rm -rf {} \;
	rm -rf build/*
	rm -f *.log
	rm -rf .gradle

compile:
	gradle build -x test 

build: compile
	gradle shadowJar


run: build
	java -cp build/libs/starbucks-all.jar starbucks.Main 2>debug.log

network:
	docker network create --driver bridge starbucks

starbucks-api:
	docker run --network starbucks --name starbucks-api -p 3000:3000 -td paulnguyen/starbucks-api:v1.0

starbucks-cashier:
	docker run --network starbucks --name starbucks-nodejs -p 8080:8080  \
	-e "api_endpoint=http://starbucks-api:3000" -td paulnguyen/starbucks-nodejs:v1.0
