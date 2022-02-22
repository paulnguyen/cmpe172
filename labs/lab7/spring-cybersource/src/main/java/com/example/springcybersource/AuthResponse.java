package com.example.springcybersource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class AuthResponse extends Payload {
    
    public String id ;
    public int code ;
    public String status ;
    public String reconciliationId ;
    public String reason ;
    public String message ;

    public static AuthResponse fromJson( String json ) {
        /* 
            https://www.baeldung.com/jackson-object-mapper-tutorial
        */
        ObjectMapper mapper = new ObjectMapper() ;
        AuthResponse response = new AuthResponse() ;
        try { 
            JsonNode jsonNode = mapper.readTree(json);
            if ( jsonNode.get("response") != null ) {
                response.status = "ERROR" ;
                response.message = jsonNode.get("response").get("rmsg").asText() ;
            } else {
                response.id = jsonNode.get("id").asText() ;
                response.status = jsonNode.get("status").asText() ;
                if ( !response.status.equals("AUTHORIZED") ) {
                    response.status = "ERROR" ;
                    response.reason = jsonNode.get("errorInformation").get("reason").asText() ;
                    response.message = jsonNode.get("errorInformation").get("message").asText() ;
                } else { 
                    response.reconciliationId = jsonNode.get("reconciliationId").asText() ;   
                }                
            }

        } catch ( Exception e ) { System.out.println( e ) ; }	
        return response ;
    }  

}



// Auth Response: {"id":"6181562114716109903002","status":"AUTHORIZED","reconciliationId":"70073664SN5UVB3H"}
// Capture Response: {"id":"6181562121596671603003","status":"PENDING","reconciliationId":"70073664SN5UVB3H"}
// Auth Response: {"id":"6181559647146109603002","status":"INVALID_REQUEST","reconciliationId":null}
// Capture Response: {"id":"6181559652546671203003","status":"INVALID_REQUEST","reconciliationId":null}
// {"id":"6181559652546671203003","submitTimeUtc":"2021-04-11T15:46:05Z","status":"INVALID_REQUEST","reason":"INVALID_DATA","message":"Declined - One or more fields in the request contains invalid data"}            
