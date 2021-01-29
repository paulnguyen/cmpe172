# CMPE 172 - Lab #1 - Hello Spring

In this Lab, you will be bulling the Spring Demo App using different Spring Tools and deploying the Demos on Docker and Google Cloud.  Your work should be commited on a regular basis (each day you have a change) to your assigned GitHub Private Repo in the https://github.com/nguyensjsu organization.  You submission should include all the source code and notes on your work (including required screenshots) in README.md (GitHub Markdown).  

* In the /labs/lab1 folder, include
  * demo-initializr
  * demo-vscode
  * demo-docker
  * tacos-cloud
  * images (screenshots)
  * README.md (lab notes)

## Part 1 -- Spring Demo App

### Spring Demo App Using Spring Initializr

1. Generate a New Spring Boot Project using the following parameters via https://start.spring.io/ (online Spring Boot Initializr).

* Project: Gradle Project
* Language: Java Language
* Spring Boot Version: 2.4.2
* Group: com.example
* Artifact: demo-initializr
* Name: demo-initializr
* Package Name: come.example.demo-initializr
* Packaging: Jar
* Dependencies:
  * Spring Web

2. Extract the Zip file and store in your Git Folder /labs/lab1/demo-initializr.  Make changes to the App using the following code. Note: you will need to modify the Class Name to match the code generated.

```
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoXXXApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoXXXApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}
}
```

3. Run the Spring Demo App on your Local Machine with the message: *Hello CMPE 172!*.  Take a "Screenshot" of your Browser and Full Desktop and include this in your Lab Notes README.md in GitHub.

![Spring Initializr](images/spring-initializr.png)

### Spring Demo App Using Spring Tools in Visual Studio Code

1. Generate a New Spring Boot Project using the following parameters from Visual Studio Code. Store in your Git Folder /labs/lab1/demo-vscode.

* Project: Gradle Project
* Language: Java Language
* Spring Boot Version: 2.4.2
* Group: com.example
* Artifact: demo-vscode
* Name: demo-vscode
* Package Name: come.example.demo-vscode
* Packaging: Jar
* Dependencies:
  * Spring Web

2. Make the Appropriate Code Changes using the following:

```
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoXXXApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoXXXApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}
}
```

3. Run the Spring Demo App from Visual Studio Code in "Debug Mode" with the message: *Hello CMPE 172!*.  Take a "Screenshot" of your Browser and Full Desktop and include this in your Lab Notes README.md in GitHub.

![Spring Debug](images/spring-debug.png)


### Spring Demo App Configured for Docker and Kubernettes

1. Generate a New Spring Boot Project using the following parameters from Visual Studio Code or using Spring Boot Initializr. Store in your Git Folder /labs/lab1/demo-docker.

* Project: Gradle Project
* Language: Java Language
* Spring Boot Version: 2.4.2
* Group: com.example
* Artifact: demo-docker
* Name: demo-vscode
* Package Name: come.example.demo-docker
* Packaging: Jar
* Dependencies:
  * Spring Web

2. Make the Appropriate Code Changes using the following:

```
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoXXXApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoXXXApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}
}
```
3. Using the Lab files provided, Do the following:

- [ ] Build Docker Image
- [ ] Run Docker Container in Docker Desktop (Locally)
- [ ] Run Docker Container in Google Cloud Kubernettes Engine

Files (add to demo-docker project):
* Dockerfile 
* Makefile
* pod.yaml
* service.yaml

Take "Screenshots" of your Browser and Full Desktop and include this in your Lab Notes README.md in GitHub.  Screenshots should include:  Spring Boot App running from Docker Desktop and Google Cloud.

Follow the instructions from Demos in class.


## Part 2 -- Spring Taco Cloud App

Using the Sample code from Chapter 1 of *Spring in Action 5th Edition*, create the Tacos Spring Boot project in /labs/lab1/tacos-cloud and deploy the Application on go Google Cloud Kubernettes Engine.  

Take Screenshots showing:  

1. The Tacos Cloud Pod Workload
2. The Tacos Cloud Service (show Public IP and Port)
3. Browser Showing Tacos Cloud App Running
