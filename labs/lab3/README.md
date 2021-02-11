# CMPE 172 - Lab #3 - Spring Gumball (Version 1)

In this Lab, you will be buidling a Spring Gumball App, Testing it with Test Containers and Exploring Deployment on Docker and Google Cloud (GKE) with Load Balancers.  Your work should be committed on a regular basis (each day you have a change) to your assigned GitHub Private Repo in the https://github.com/nguyensjsu organization.  Your submission should include all the source code and notes on your work (including required screenshots) in README.md (GitHub Markdown).  

* In the /labs/lab3 folder, include
  * spring-gumball  (note: use starter code here:  https://github.com/paulnguyen/cmpe172/tree/main/spring/spring-gumball-v1)
  * images (screenshots)
  * README.md (lab notes)
  
## Part 1 -- Spring Gumball on Docker 

### Project Dependencies - Note that the project was initialized with Spring Boot as follows:

![spring-gumball-initializr](images/spring-gumball-initializr.png)

### Java State Machine for Gumball Business Logic - The starter code includes a State Machine (Java Pattern) with a JUnit Test.  The State Machine Design is as follows and the code is in the "com.example.gumballmachine" package.

![gumball-state-model](images/gumball-state-model.png)

### Gumball Spring MVC Code

1. GumballModel.java

This code is currently a "place holder" in version 1 of this example.  It does not do much, but will play in important role in future versions.

```
package com.example.springgumball;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
class GumballModel {

    private String modelNumber ;
    private String serialNumber ;
    private Integer countGumballs ;
    
}
```

2. GumballCommand.java

The Command Model is used to capture the actions and inputs from POST request from HTML forms in the View.  It is using Lombok Framework to generate all the required Java Beans Methods.

```
package com.example.springgumball;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
class GumballCommand {

    private String action ;
    private String message ;
    
}
```

3. GumballMachineController.java

The Gumball Machine Controller handles the GET and POST actions from HTTP, creates and manages business logic via HTTP Sessions and the Gumball Machine State Object.

```
package com.example.springgumball;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

import com.example.gumballmachine.GumballMachine ;

@Slf4j
@Controller
@RequestMapping("/")
public class GumballMachineController {

    @GetMapping
    public String getAction( @ModelAttribute("command") GumballCommand command, 
                            Model model, HttpSession session) {
      
        GumballModel g = new GumballModel() ;
        g.setModelNumber( "SB102927") ;
        g.setSerialNumber( "2134998871109") ;
        model.addAttribute( "gumball", g ) ;
        
        GumballMachine gm = new GumballMachine(10) ;
        String message = gm.toString() ;
        session.setAttribute( "gumball", gm) ;
        String session_id = session.getId() ;

        String server_ip = "" ;
        String host_name = "" ;
        try { 
            InetAddress ip = InetAddress.getLocalHost() ;
            server_ip = ip.getHostAddress() ;
            host_name = ip.getHostName() ;
  
        } catch (Exception e) { }
  
        model.addAttribute( "session", session_id ) ;
        model.addAttribute( "message", message ) ;
        model.addAttribute( "server",  host_name + "/" + server_ip ) ;

        return "gumball" ;

    }

    @PostMapping
    public String postAction(@Valid @ModelAttribute("command") GumballCommand command,  
                            @RequestParam(value="action", required=true) String action,
                            Errors errors, Model model, HttpServletRequest request) {
    
        log.info( "Action: " + action ) ;
        log.info( "Command: " + command ) ;
    
        HttpSession session = request.getSession() ;
        GumballMachine gm = (GumballMachine) session.getAttribute("gumball") ;

        if ( action.equals("Insert Quarter") ) {
            gm.insertQuarter() ;
        }

        if ( action.equals("Turn Crank") ) {
            command.setMessage("") ;
            gm.turnCrank() ;
        } 

        session.setAttribute( "gumball", gm) ;
        String message = gm.toString() ;
        String session_id = session.getId() ;        

        String server_ip = "" ;
        String host_name = "" ;
        try { 
            InetAddress ip = InetAddress.getLocalHost() ;
            server_ip = ip.getHostAddress() ;
            host_name = ip.getHostName() ;
  
        } catch (Exception e) { }
  
        model.addAttribute( "session", session_id ) ;
        model.addAttribute( "message", message ) ;
        model.addAttribute( "server",  host_name + "/" + server_ip ) ;
     

        if (errors.hasErrors()) {
            return "gumball";
        }

        return "gumball";
    }

}
```

4. Gumball HTML View Page

```
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" 
      xmlns:th="http://www.thymeleaf.org">

<html>
<head>
    <title>Welcome to the Gumball Machine</title>
    <link rel="stylesheet" th:href="@{/styles.css}" />
</head>

<body>
<h1 align="center">Welcome to the Gumball Machine</h1>

<!-- FORM SECTION -->
<form name="form" th:object="${command}" method="post" action="">
    <p>
    <table id="msg" align="center" style="width:40%">
      <tr>
        <td>
          <pre id="pre">
          <span th:text="${message}" />
          </pre>
        </td>
      </tr>
    </table>
    </p>
    <p align="center"> <img th:src="@{/images/giant-gumball-machine.jpg}" width="385" height="316"/></p>
    <br/>
    <p align="center">
        Special Instructions:  <input type="text" id="message" th:field="*{message}"/>
        <br/>
        <br/>
        <br/>
        <input type="submit" name="action" id="btnInsertQuarter" value="Insert Quarter">
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input type="submit" name="action" id="btnTurnCrank" value="Turn Crank">
    </p>
</form>
<!-- END FORM SECTION -->

<br/>
<br/>
<br/>
<br/>
<br/>
<br/>

<table align="center" style="width:100%" >
<tr>
  <td style="text-align: left;" >
    <pre>Session ID:  <span th:text="${session}" /></pre>
  </td>
  <td style="text-align: right;" >
    <pre>Server Host/IP:  <span th:text="${server}" /></pre>
  </td>  
</tr>
</table>

</body>
</html>
```
### Gumball Spring Docker Compose

* docker-compose.yaml

The Docker Compose Spec is used to deployed a load balanced local spring gumball environment on docker.  Rules in the Makefile can be used to managed this.  Specifically, this commands brings up a load balancer and two insstances of spring gumball spring boot app:  

* docker-compose up --scale gumball=2 -d

```
version: "3"

services:
  gumball:
    image: spring-gumball
    volumes:
      - /tmp:/tmp
    networks:
      - network   
    ports:
      - 8080    
    restart: always     
  lb:
    image: eeacms/haproxy
    depends_on:
    - gumball
    ports:
    - "80:5000"
    - "1936:1936"
    environment:
      BACKENDS: "gumball"
      BACKENDS_PORT: "8080"
      DNS_ENABLED: "true"
      COOKIES_ENABLED: "false"
      LOG_LEVEL: "info"
    networks:
      - network

volumes:
  schemas:
    external: false

networks:
  network:
    driver: bridge
```

### Testing with Test Containers and Selenium 

The Browser Container Test makes use of Selenium Web Driver and Test Containers (in Docker).  The test code also enables VNC recording to a specific path on your local system.  It is currently pointing at "/tmp".  You will need to make the appropriate changes to make this work in your environment.

Once testing is complete, Gradle generates the test results in the project's "build folder".  For example:

![spring-gumball-test-results](images/spring-gumball-test-results.png)

```
package com.example.springgumball;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.By;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class BrowserContainerTest {
  
  @LocalServerPort
  private int port;
 
  @Container
  private BrowserWebDriverContainer container = new BrowserWebDriverContainer()
          .withCapabilities(new ChromeOptions())
          .withRecordingMode(RECORD_ALL, new File("/tmp") ) ;
  
  @Test
  public void testGumballPageLoad() {

    this.container.getWebDriver().get( "http://host.docker.internal:" + port + "/");

    RemoteWebDriver driver = this.container.getWebDriver() ;
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS) ;

    driver.navigate().refresh();

    String titleText = driver.getTitle();
    assertEquals("Welcome to the Gumball Machine", titleText);

    String h1Text = driver.findElementByTagName("h1").getText();
    assertEquals("Welcome to the Gumball Machine", h1Text);

    String imgSrc = driver.findElementByTagName("img").getAttribute("src");
    System.out.println( imgSrc ) ;
    assertTrue(imgSrc.contains("giant-gumball-machine.jpg") );

  }
  
  
  @Test
  public void testGumballPlaceOrder() {

    this.container.getWebDriver().get( "http://host.docker.internal:" + port + "/");

    RemoteWebDriver driver = this.container.getWebDriver() ;
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS) ;

    driver.navigate().refresh();
    String url = driver.getCurrentUrl() ;
    System.out.println( url ) ;
    driver.get(url) ;

    String titleText = driver.getTitle();
    assertEquals("Welcome to the Gumball Machine", titleText);

    String h1Text = driver.findElementByTagName("h1").getText();
    assertEquals("Welcome to the Gumball Machine", h1Text);

    String imgSrc = driver.findElementByTagName("img").getAttribute("src");
    System.out.println( imgSrc ) ;
    assertTrue(imgSrc.contains("giant-gumball-machine.jpg") );

    driver.findElement(By.id("btnInsertQuarter")).click() ;
    String message1 = driver.findElement(By.id("pre")).getText() ;
    System.out.println( message1 ) ;

    driver.findElement(By.id("btnTurnCrank")).click() ;
    String message2 = driver.findElement(By.id("pre")).getText() ;
    System.out.println( message2 ) ;
    

  }
 
}
```

## Part 2 -- Spring Gumball on Google Cloud 

### GKE Configurations Manifests

* deployment.yaml

```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: spring-gumball-deployment
  namespace: default
spec:
  selector:
    matchLabels:
      name: spring-gumball
  replicas: 4 # tells deployment to run 2 pods matching the template
  template: # create pods using pod definition in this template
    metadata:
      # unlike pod.yaml, the name is not included in the meta data as a unique name is
      # generated from the deployment name
      labels:
        name: spring-gumball
    spec:
      containers:
      - name: spring-gumball
        image: paulnguyen/spring-gumball:v1
        ports:
        - containerPort: 8080
```

* service.yaml

```
apiVersion: v1
kind: Service
metadata:
  name: spring-gumball-service 
  namespace: default
spec:
  type: NodePort
  ports:
  - port: 8080
    targetPort: 8080 
  selector:
    name: spring-gumball
```

* ingress.yaml

```
apiVersion: "extensions/v1beta1"
kind: "Ingress"
metadata:
  name: "spring-gumball-ingress"
  namespace: "default"
spec:
  backend:
    serviceName: "spring-gumball-service"
    servicePort: 8080
```

### GCP Deployment Example Screenshots

* Deployment

![spring-gumball-deployment-1](images/spring-gumball-deployment-1.png)
![spring-gumball-deployment-2](images/spring-gumball-deployment-2.png)

* Service

![spring-gumball-service-1](images/spring-gumball-service-1.png)
![spring-gumball-service-2](images/spring-gumball-service-2.png)

* Ingress

![spring-gumball-ingress-1](images/spring-gumball-ingress-1.png)
![spring-gumball-ingress-2](images/spring-gumball-ingress-2.png)



# Lab Notes and References

## Spring Boot Browser Testing

* https://docs.spring.io/spring-boot/docs/current/reference/html
* https://www.selenium.dev/documentation/en/webdriver/browser_manipulation
* https://junit.org/junit5/docs/current/user-guide/#writing-tests
* https://www.testcontainers.org/modules/webdriver_containers
* https://github.com/testcontainers/testcontainers-java/tree/master/examples
* https://www.javadoc.io/doc/org.seleniumhq.selenium/selenium-api/3.141.59/overview-summary.html

# Docker Compose

* https://docs.docker.com/compose
* https://hub.docker.com/r/eeacms/haproxy
* https://github.com/paulnguyen/cloud/blob/master/jumpbox/docker-jumpbox.md

# GKE Concepts & Load Balancers

* https://cloud.google.com/kubernetes-engine/docs/concepts/pod
* https://cloud.google.com/kubernetes-engine/docs/concepts/deployment
* https://cloud.google.com/kubernetes-engine/docs/concepts/service
* https://cloud.google.com/kubernetes-engine/docs/tutorials/http-balancer
* https://kubernetes.io/docs/concepts/services-networking/ingress/
* https://cloud.google.com/kubernetes-engine/docs/concepts/ingress
* https://cloud.google.com/kubernetes-engine/docs/concepts/ingress-xlb
* https://cloud.google.com/kubernetes-engine/docs/concepts/ingress-ilb
* https://cloud.google.com/kubernetes-engine/docs/concepts/container-native-load-balancing











