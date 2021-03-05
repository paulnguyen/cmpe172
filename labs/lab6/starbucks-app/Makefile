all: clean

clean: 
	find . -name "*.class" -exec rm -rf {} \;
	find . -name .DS_Store -exec rm -rf {} \;
	rm -rf build/*
	rm -f *.log
	rm -rf .gradle


# Gradle: 		https://guides.gradle.org/creating-new-gradle-builds/
# CircleCI:		https://circleci.com/docs/2.0/language-java/

init:
	gradle init

compile:
	gradle build -x test 

test:
	gradle test

build: compile
	gradle shadowJar

spotbugs:
	gradle spotbugsMain

codesmells:
	gradle smartsmells

run: build
	java -cp build/libs/starbucks-all.jar starbucks.Main 2>debug.log

