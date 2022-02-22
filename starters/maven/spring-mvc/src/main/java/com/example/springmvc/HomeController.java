package com.example.springmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @RequestMapping("/")
    public @ResponseBody String greeting() {

        ValAndVarUserDemo.print() ;
        FieldLevelGetterSetterDemo.print() ;
        GetterSetterUserDemo.print() ;
        ConstructorUserDemo.print() ;
        DataUserDemo.print() ;
        NonNullUserDemo.print() ;

        return "Hello, World";
    }

}