package com.example.springgumball;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.util.Optional;
import java.time.*; 
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64.Encoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

import com.example.gumballmachine.GumballMachine ;

@Slf4j
@Controller
@RequestMapping("/")
public class GumballMachineController {

	private static String KEY = "kwRg54x2Go9iEdl49jFENRM12Mp711QI" ;
    private static String MODEL_NUMBER = "SB102927" ;
    private static String SERIAL_NUMBER = "2134998871109" ;

    private GumballModel gmachine ;    

    @Autowired
    private GumballModelRepository mysql ;

    @Autowired
    private GumballQueryRepository query ;
    
    private String hmac_sha256(String secretKey, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256") ;
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256") ;
            mac.init(secretKeySpec) ;
            byte[] digest = mac.doFinal(data.getBytes()) ;
            java.util.Base64.Encoder encoder = java.util.Base64.getEncoder() ;
            String hash_string = encoder.encodeToString(digest) ;
            return hash_string ;
        } catch (InvalidKeyException e1) {
            throw new RuntimeException("Invalid key exception while converting to HMAC SHA256") ;
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException("Java Exception Initializing HMAC Crypto Algorithm") ;
        }
    }    

    @GetMapping
    public String getAction( @ModelAttribute("command") GumballCommand command, 
                            Model model) {

        /* 
            Working with Spring Data Repositories:
            https://docs.spring.io/spring-data/data-commons/docs/current/reference/html 
        */ 
        if ( gmachine == null ) {
            GumballModel findBySerialNum = new GumballModel() ;
            findBySerialNum.setSerialNumber( SERIAL_NUMBER ) ;
            Example<GumballModel> findBySerialNumExample = Example.of(findBySerialNum) ;
            Optional<GumballModel> findBySerialNumResult = query.findOne(findBySerialNumExample) ;
            if ( findBySerialNumResult.isPresent() ) {
                gmachine = findBySerialNumResult.get() ;
            } else {
                gmachine = new GumballModel() ;
                gmachine.setModelNumber( MODEL_NUMBER ) ;
                gmachine.setSerialNumber( SERIAL_NUMBER ) ;
                gmachine.setCountGumballs( 1000 ) ;
                mysql.save(gmachine) ;                 
            }                 
        }

        /* 
            Query by ID:
            https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html 
        */
        Optional<GumballModel> test = mysql.findById(1) ;
        if ( test.isPresent() ) {
            GumballModel gm = test.get() ;
            System.out.println( "Find By Id: " + gm ) ;
        }

        /* 
            Defining Query Methods:
            https://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories.query-methods.details
        */
        List<GumballModel> list = mysql.findBySerialNumber( SERIAL_NUMBER ) ;
        System.out.println( "# Rows Found: " + list.size() ) ;
        for (GumballModel g : list) {
            System.out.println("Find By Serial Number: " + g) ;
        }

        /*
            Query by Example:
            https://www.baeldung.com/spring-data-query-by-example
        */
        GumballModel findMeExample = new GumballModel() ;
        findMeExample.setSerialNumber( SERIAL_NUMBER ) ;
        Example<GumballModel> findMe = Example.of(findMeExample) ;
        Optional<GumballModel> search = query.findOne(findMe) ;
        if ( search.isPresent() ) {
            GumballModel found = search.get() ;
            System.out.println( "Find By Example: " + found ) ;
        }         

        /* Set Banner Message */
        GumballMachine gm = new GumballMachine() ;
        gm.setModelNumber( gmachine.getModelNumber() ) ;
        gm.setSerialNumber( gmachine.getSerialNumber() ) ;
        String message = gm.toString() ;
    

        /* Set Security Message */
        String state = gm.getState().getClass().getName() ;
        command.setState( state ) ;

        long ts_long = java.lang.System.currentTimeMillis() ;
        String ts_string = String.valueOf(ts_long) ;
        command.setTimestamp( ts_string ) ;

        String text = state + "/" + ts_string ;
        String hash_string = hmac_sha256( KEY, text ) ;
        command.setHash( hash_string ) ;

        /* Get Host Information */
        String server_ip = "" ;
        String host_name = "" ;
        try { 
            InetAddress ip = InetAddress.getLocalHost() ;
            server_ip = ip.getHostAddress() ;
            host_name = ip.getHostName() ;
  
        } catch (Exception e) { }
  
        
        /* Render View */
        model.addAttribute( "hash", hash_string ) ;
        model.addAttribute( "message", message ) ;
        model.addAttribute( "server",  host_name + "/" + server_ip ) ;

        return "gumball" ;

    }

    @PostMapping
    public String postAction(@Valid @ModelAttribute("command") GumballCommand command,  
                            @RequestParam(value="action", required=true) String action,
                            Errors errors, Model model, HttpServletRequest request) {
    
        log.info( "Action: " + action ) ;
        log.info( "Command: " + command ) ;
    
        /* 
            Working with Spring Data Repositories:
            https://docs.spring.io/spring-data/data-commons/docs/current/reference/html 
        */ 
        if ( gmachine == null ) {
            GumballModel findBySerialNum = new GumballModel() ;
            findBySerialNum.setSerialNumber( SERIAL_NUMBER ) ;
            Example<GumballModel> findBySerialNumExample = Example.of(findBySerialNum) ;
            Optional<GumballModel> findBySerialNumResult = query.findOne(findBySerialNumExample) ;
            if ( findBySerialNumResult.isPresent() ) {
                gmachine = findBySerialNumResult.get() ;
            } else {
                gmachine = new GumballModel() ;
                gmachine.setModelNumber( MODEL_NUMBER ) ;
                gmachine.setSerialNumber( SERIAL_NUMBER ) ;
                gmachine.setCountGumballs( 1000 ) ;
                mysql.save(gmachine) ;                 
            }                 
        }

        /* Get Security Message */
        String input_hash = command.getHash() ;
        String input_state = command.getState() ;
        String input_timestamp = command.getTimestamp() ;

        String input_text = input_state + "/" + input_timestamp ;
        String calculated_hash = hmac_sha256( KEY, input_text ) ;

        log.info( "Input Hash: " + input_hash ) ;
        log.info( "Valid Hash: " + calculated_hash ) ;

        // Check Message Integrity
        if ( !input_hash.equals(calculated_hash) ) {
            throw new GumballServerError() ;
        }

        long ts1 = Long.parseLong( input_timestamp ) ;
        long ts2 = java.lang.System.currentTimeMillis() ;
        long diff = ts2 - ts1 ;

        log.info( "Input Timestamp: " + String.valueOf(ts1) ) ;
        log.info( "Current Timestamp: " + String.valueOf(ts2) ) ;
        log.info( "Timestamp Delta: " + String.valueOf(diff) ) ;

        // Guard Against Replay Attack
        if ( (diff/1000) > 1000 ) {
            throw new GumballServerError() ;
        }
        
        /*
            Query by Example:
            https://www.baeldung.com/spring-data-query-by-example
        */
        GumballModel found = new GumballModel();
        GumballModel findMeExample = new GumballModel() ;
        findMeExample.setSerialNumber( SERIAL_NUMBER ) ;
        Example<GumballModel> findMe = Example.of(findMeExample) ;
        Optional<GumballModel> search = query.findOne(findMe) ;
        if ( search.isPresent() ) {
            found = search.get() ;
            System.out.println( "Find By Example: " + found ) ;
        }         

        /* Setup Business Object */
        GumballMachine gm = new GumballMachine() ;
        gm.setModelNumber( gmachine.getModelNumber() ) ;
        gm.setSerialNumber( gmachine.getSerialNumber() ) ;
        gm.setState( input_state ) ;

        /* Process Post Action */
        if ( action.equals("Insert Quarter") ) {
            gm.insertQuarter() ;
        }
        if ( action.equals("Turn Crank") ) {
            command.setMessage("") ;
            gm.turnCrank() ;
        } 

        /* Set Banner Message */
        String message = gm.toString() ;

        /* Set Security Message */
        String state = gm.getState().getClass().getName() ;
        command.setState( state ) ;

        long ts_long = java.lang.System.currentTimeMillis() ;
        String ts_string = String.valueOf(ts_long) ;
        command.setTimestamp( ts_string ) ;

        String text = state + "/" + ts_string ;
        String hash_string = hmac_sha256( KEY, text ) ;
        command.setHash( hash_string ) ;

        /* Get Host Information */
        String server_ip = "" ;
        String host_name = "" ;
        try { 
            InetAddress ip = InetAddress.getLocalHost() ;
            server_ip = ip.getHostAddress() ;
            host_name = ip.getHostName() ;  
        } catch (Exception e) { }
          

        /* Update Gumball Inventory */
        if ( input_state.equals("com.example.gumballmachine.HasQuarterState") 
             && state.equals("com.example.gumballmachine.NoQuarterState") ) {
            Integer count = found.getCountGumballs() ;
            found.setCountGumballs( new Integer(count.intValue() - 1) ) ;
            mysql.save(found) ;
        }


        /* Render View */
        model.addAttribute( "hash", hash_string ) ;
        model.addAttribute( "message", message ) ;
        model.addAttribute( "server",  host_name + "/" + server_ip ) ;
     
        if (errors.hasErrors()) {
            return "gumball";
        }

        return "gumball";
    }

}