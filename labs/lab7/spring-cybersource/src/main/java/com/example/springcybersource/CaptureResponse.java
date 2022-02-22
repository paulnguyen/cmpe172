package com.example.springcybersource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class CaptureResponse extends Payload {

    public String id ;
    public int code ;
    public String status ;
    public String reconciliationId ;
    public String reason ;
    public String message ;    

    public static CaptureResponse fromJson( String json ) {
        /* 
            https://www.baeldung.com/jackson-object-mapper-tutorial
        */
        ObjectMapper mapper = new ObjectMapper() ;
        CaptureResponse response = new CaptureResponse() ;
        try { 
            JsonNode jsonNode = mapper.readTree(json);
            if ( jsonNode.get("response") != null ) {
                response.status = "ERROR" ;
                response.message = jsonNode.get("response").get("rmsg").asText() ;
            } else {
                response.id = jsonNode.get("id").asText() ;
                response.status = jsonNode.get("status").asText() ;
                if ( !response.status.equals("PENDING") ) {
                    response.status = "ERROR" ;
                    response.reason = jsonNode.get("errorInformation").get("reason").asText() ;
                    response.message = jsonNode.get("errorInformation").get("message").asText() ;
                } else { 
                    response.reconciliationId = jsonNode.get("reconciliationId").asText() ;   
                }
            }
        } catch ( Exception e ) { 
            System.out.println( e ) ; 
        }	
        return response ;
    }

}

