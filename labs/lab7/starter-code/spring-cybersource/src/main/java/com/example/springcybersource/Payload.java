package com.example.springcybersource;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Payload {

    public String toJson() {
        /* 
            https://www.baeldung.com/jackson-object-mapper-tutorial
        */
        String json = "" ;
        ObjectMapper mapper = new ObjectMapper();
        try { 
            json = mapper.writeValueAsString(this);
        } catch ( Exception e ) { }
        return json ;
    }

}
