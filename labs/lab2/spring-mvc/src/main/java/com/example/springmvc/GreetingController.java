package com.example.springmvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;

@Controller
public class GreetingController {

    private final GreetingService service;
    private String name ;

    public GreetingController(GreetingService service) {
        this.service = service;
    }

    @GetMapping("/greeting-mvc")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/greeting-no-session")
    public String greeting_no_session(@RequestParam(name="name", required=false) String name, Model model) {
        if (name!=null) {
            this.name = name ;
        } else {
            this.name = "World" ;
        }
        model.addAttribute("name", this.name);
        return "greeting";
    }

    @GetMapping("/greeting-with-session")
    public String greeting_with_session(@RequestParam(name="name", required=false) String name, Model model, HttpSession session) {
        if (name!=null) {
            this.name = name ;
            session.setAttribute( "name", name) ;
        } else {
            String session_value = (String) session.getAttribute("name") ;
            if ( session_value != null ) {
                this.name = session_value ;
            } else {
                this.name = "World" ;
            }
        }
        model.addAttribute("name", this.name);
        return "greeting";
    }

    @RequestMapping("/greeting")
    public @ResponseBody String greeting() {
        return service.greet();
    }

}