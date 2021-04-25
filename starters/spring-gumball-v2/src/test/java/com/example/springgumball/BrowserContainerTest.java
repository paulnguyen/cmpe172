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