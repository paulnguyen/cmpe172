package com.example.springgumball;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

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
  public void testHomePage() {

    this.container.getWebDriver().get( "http://host.docker.internal:" + port + "/");

    RemoteWebDriver driver = this.container.getWebDriver() ;

    String titleText = driver.getTitle();
    assertEquals("Welcome to the Gumball MachineXX", titleText);

    String h1Text = driver.findElementByTagName("h1").getText();
    assertEquals("Welcome to the Gumball Machine", h1Text);

    String imgSrc = driver.findElementByTagName("img").getAttribute("src");
    System.out.println( imgSrc ) ;
    assertTrue(imgSrc.contains("giant-gumball-machine.jpg") );


  }
  
  
}