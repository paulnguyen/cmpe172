package com.example.springreqresclient;

import com.example.springreqresclient.model.NewUser;
import com.example.springreqresclient.model.User;
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

        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "" ;
        String message = "";
        if (action.equals("GET")) {
            message = "GET";
            resourceUrl = "https://reqres.in/api/users/2";
            // get response as string
            ResponseEntity<String> stringResponse = restTemplate.getForEntity(resourceUrl, String.class);
            message = stringResponse.getBody();
            // get response as POJO
            ResponseEntity<User> userResponse = restTemplate.getForEntity(resourceUrl, User.class);
            User user = userResponse.getBody();
            System.out.println( user );
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
        if (action.equals("POST")) {
            message = "POST";
            resourceUrl = "https://reqres.in/api/users";
            // get response as string
            NewUser newUserRequest = new NewUser() ;
            newUserRequest.setName("John Smith") ;
            newUserRequest.setJob("CEO") ;
            HttpEntity<NewUser> newUser = new HttpEntity<NewUser>(newUserRequest) ;
            String stringResponse = restTemplate.postForObject(resourceUrl, newUser, String.class);
            message = stringResponse;
            // get response as POJO
            ResponseEntity<NewUser> newUserResponse = restTemplate.postForEntity(resourceUrl, newUser, NewUser.class);
            NewUser newUserObject = newUserResponse.getBody();
            System.out.println( newUserObject );
            // pretty print JSON
            try {
                ObjectMapper objectMapper = new ObjectMapper() ;
                Object object = objectMapper.readValue(message, Object.class);
                String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
                System.out.println( jsonString) ;
                message = "\n" + jsonString ;
            }
            catch ( Exception e ) {}
        }
        model.addAttribute("message", message);
        return "console";
    }

}