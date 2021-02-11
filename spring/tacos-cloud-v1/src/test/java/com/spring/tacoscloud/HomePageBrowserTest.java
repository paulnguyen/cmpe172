package com.spring.tacoscloud;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class HomePageBrowserTest {
  
  @LocalServerPort
  private int port;
 
  @Container
  private BrowserWebDriverContainer container = new BrowserWebDriverContainer()
          .withCapabilities(new ChromeOptions());
  
  @Test
  public void testHomePage() {

    this.container.getWebDriver().get( "http://host.docker.internal:" + port + "/");

    RemoteWebDriver driver = this.container.getWebDriver() ;

    String titleText = driver.getTitle();
    assertEquals("Taco Cloud", titleText);

    String h1Text = driver.findElementByTagName("h1").getText();
    assertEquals("Welcome to...", h1Text);

    String imgSrc = driver.findElementByTagName("img").getAttribute("src");
    System.out.println( imgSrc ) ;
    //http://host.docker.internal:52970/images/TacoCloud.png
    assertTrue(imgSrc.contains("TacoCloud.png") );


  }
  
  
}
