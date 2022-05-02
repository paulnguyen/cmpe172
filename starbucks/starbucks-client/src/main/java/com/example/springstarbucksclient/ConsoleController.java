package com.example.springstarbucksclient;

import com.example.springstarbucksclient.model.Card;
import com.example.springstarbucksclient.model.Order;
import com.example.springstarbucksclient.model.Ping;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/*
    RestTemplate JavaDoc:
        * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html

    Tutorial Resources:
        * https://reflectoring.io/spring-resttemplate
        * https://www.baeldung.com/rest-template
        * https://springframework.guru/enable-pretty-print-of-json-with-jackson

 */

@Controller
@RequestMapping("/")
public class ConsoleController {

    @GetMapping
    public String getAction(@ModelAttribute("command") ConsoleCommand command,
                            Model model) {
        return "console";
    }

    @PostMapping
    public String postAction(@ModelAttribute("command") ConsoleCommand command,
                             @RequestParam(value = "action", required = true) String action,
                             Errors errors, Model model, HttpServletRequest request) {

        System.out.println( command );

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "" ;
        String message = "";

        if (action.equals("PING")) {
            message = "PING";
            resourceUrl = "http://localhost:8080/ping";
            // get response as string
            ResponseEntity<String> stringResponse = restTemplate.getForEntity(resourceUrl, String.class);
            message = stringResponse.getBody();
            // get response as POJO
            ResponseEntity<Ping> pingResponse = restTemplate.getForEntity(resourceUrl, Ping.class);
            Ping pingMsg = pingResponse.getBody();
            System.out.println( pingMsg );
            // pretty print JSON
            try {
                ObjectMapper objectMapper = new ObjectMapper() ;
                Object object = objectMapper.readValue(message, Object.class);
                String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
                System.out.println(jsonString) ;
                message = "\n" + jsonString ;
            }
            catch ( Exception e ) {}
        }
        if (action.equals("NEW CARD")) {
            message = "";
            resourceUrl = "http://localhost:8080/cards";
            // get response as POJO
            String emptyRequest = "" ;
            HttpEntity<String> newCardRequest = new HttpEntity<String>(emptyRequest) ;
            ResponseEntity<Card> newCardResponse = restTemplate.postForEntity(resourceUrl, newCardRequest, Card.class);
            Card newCard = newCardResponse.getBody();
            System.out.println( newCard );
            // pretty print JSON
            try {
                ObjectMapper objectMapper = new ObjectMapper() ;
                String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(newCard);
                System.out.println( jsonString) ;
                message = "\n" + jsonString ;
            }
            catch ( Exception e ) {}
        }
        if (action.equals("NEW ORDER")) {
            message = "";
            resourceUrl = "http://localhost:8080/order/register/5012349";
            // get response as POJO
            Order orderRequest = new Order() ;
            orderRequest.setDrink("Caffe Latte") ;
            orderRequest.setMilk("Whole") ;
            orderRequest.setSize("Grande");
            HttpEntity<Order> newOrderRequest = new HttpEntity<Order>(orderRequest) ;
            ResponseEntity<Order> newOrderResponse = restTemplate.postForEntity(resourceUrl, newOrderRequest, Order.class);
            Order newOrder = newOrderResponse.getBody();
            System.out.println( newOrder );
            // pretty print JSON
            try {
                ObjectMapper objectMapper = new ObjectMapper() ;
                String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(newOrder);
                System.out.println( jsonString) ;
                message = "\n" + jsonString ;
            }
            catch ( Exception e ) {}
        }
        if (action.equals("ACTIVATE CARD")) {
            message = "";
            resourceUrl = "http://localhost:8080/card/activate/"+command.getCardnum()+"/"+command.getCardcode();
            // get response as POJO
            String emptyRequest = "" ;
            HttpEntity<String> newCardRequest = new HttpEntity<String>(emptyRequest) ;
            ResponseEntity<Card> newCardResponse = restTemplate.postForEntity(resourceUrl, newCardRequest, Card.class);
            Card newCard = newCardResponse.getBody();
            System.out.println( newCard );
            // pretty print JSON
            try {
                ObjectMapper objectMapper = new ObjectMapper() ;
                String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(newCard);
                System.out.println( jsonString) ;
                message = "\n" + jsonString ;
            }
            catch ( Exception e ) {}
        }
        if (action.equals("PAY")) {
            message = "";
            resourceUrl = "http://localhost:8080/order/register/5012349/pay/"+command.getCardnum() ;
            System.out.println(resourceUrl) ;
            // get response as POJO
            String emptyRequest = "" ;
            HttpEntity<String> paymentRequest = new HttpEntity<String>(emptyRequest) ;
            ResponseEntity<Card> payForOrderResponse = restTemplate.postForEntity(resourceUrl, paymentRequest, Card.class);
            Card orderPaid = payForOrderResponse.getBody();
            System.out.println( orderPaid );
            // pretty print JSON
            try {
                ObjectMapper objectMapper = new ObjectMapper() ;
                String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(orderPaid);
                System.out.println( jsonString) ;
                message = "\n" + jsonString ;
            }
            catch ( Exception e ) {}
        }
        model.addAttribute("message", message);
        return "console";
    }

}