package com.example.springpayments;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.Optional;
import java.time.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64.Encoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.BindingResult;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/")
public class PaymentsController {  

    private static Map<String,String> months = new HashMap<>() ;
    static {
        months.put( "January", "JAN" ) ;
        months.put( "February", "FEB" ) ;
        months.put( "March", "MAR" ) ;
        months.put( "April", "APR" ) ;
        months.put( "May", "MAY" ) ;
        months.put( "June", "JUN" ) ;
        months.put( "July", "JUL" ) ;
        months.put( "August", "AUG" ) ;
        months.put( "September", "SEP" ) ;
        months.put( "October", "OCT" ) ;
        months.put( "November", "NOV" ) ;
        months.put( "December", "DEC" ) ;
    }

    private static Map<String,String> states = new HashMap<>() ;
    static {
        states.put( "AL", "Alabama" ) ;
        states.put( "AK", "Alaska" ) ;
        states.put( "AZ", "Arizona" ) ;
        states.put( "AR", "Arkansas" ) ;
        states.put( "CA", "California" ) ;
        states.put( "CO", "Colorado" ) ;
        states.put( "CT", "Connecticut" ) ;
        states.put( "DE", "Delaware" ) ;
        states.put( "FL", "Florida" ) ;
        states.put( "GA", "Georgia" ) ;
        states.put( "HI", "Hawaii" ) ;
        states.put( "ID", "Idaho" ) ;
        states.put( "IL", "Illinois" ) ;
        states.put( "IN", "Indiana" ) ;
        states.put( "IA", "Iowa" ) ;
        states.put( "KS", "Kansas" ) ;
        states.put( "KY", "Kentucky" ) ;
        states.put( "LA", "Louisiana" ) ;
        states.put( "ME", "Maine" ) ;
        states.put( "MD", "Maryland" ) ;
        states.put( "MA", "Massachusetts" ) ;
        states.put( "MI", "Michigan" ) ;
        states.put( "MN", "Minnesota" ) ;
        states.put( "MS", "Mississippi" ) ;
        states.put( "MO", "Missouri" ) ;
        states.put( "MT", "Montana" ) ;
        states.put( "NE", "Nebraska" ) ;
        states.put( "NV", "Nevada" ) ;
        states.put( "NH", "New Hampshire" ) ;
        states.put( "NJ", "New Jersey" ) ;
        states.put( "NM", "New Mexico" ) ;
        states.put( "NY", "New York" ) ;
        states.put( "NC", "North Carolina" ) ;
        states.put( "ND", "North Dakota" ) ;
        states.put( "OH", "Ohio" ) ;
        states.put( "OK", "Oklahoma" ) ;
        states.put( "OR", "Oregon" ) ;
        states.put( "PA", "Pennsylvania" ) ;
        states.put( "RI", "Rhode Island" ) ;
        states.put( "SC", "South Carolina" ) ;
        states.put( "SD", "South Dakota" ) ;
        states.put( "TN", "Tennessee" ) ;
        states.put( "TX", "Texas" ) ;
        states.put( "UT", "Utah" ) ;
        states.put( "VT", "Vermont" ) ;
        states.put( "VA", "Virginia" ) ;
        states.put( "WA", "Washington" ) ;
        states.put( "WV", "West Virginia" ) ;
        states.put( "WI", "Wisconsin" ) ;
        states.put( "WY", "Wyoming"  ) ;          
    }  

    @Autowired
    private PaymentsCommandRepository repository ;

    @GetMapping
    public String getAction( @ModelAttribute("command") PaymentsCommand command, 
                            Model model) {

         /* Render View */
        return "creditcards" ;

    }

    @PostMapping
    public String postAction(@Valid @ModelAttribute("command") PaymentsCommand command,  
                            @RequestParam(value="action", required=true) String action,
                            Errors errors, Model model, HttpServletRequest request) {
    
        log.info( "Action: " + action ) ;
        log.info( "Command: " + command ) ;
    
        /* check for errors */
        boolean hasErrors = false ;
        if ( command.firstname().equals("") )   { hasErrors = true ; log.info( "First Name Required." ) ; }
        if ( command.lastname().equals("") )    { hasErrors = true ; log.info( "Last Name Required." ) ; }
        if ( command.address().equals("") )     { hasErrors = true ; log.info( "Address Required." ) ; }
        if ( command.city().equals("") )        { hasErrors = true ; log.info( "City Required." ) ; }
        if ( command.state().equals("") )       { hasErrors = true ; log.info( "State Required." ) ; }
        if ( command.zip().equals("") )         { hasErrors = true ; log.info( "Zip Required." ) ; }
        if ( command.phone().equals("") )       { hasErrors = true ; log.info( "Phone Required." ) ; }
        if ( command.cardnum().equals("") )     { hasErrors = true ; log.info( "Credit Card Number Required." ) ; }
        if ( command.cardexpmon().equals("") )  { hasErrors = true ; log.info( "Credit Card Expiration Month Required." ) ; }
        if ( command.cardexpyear().equals("") ) { hasErrors = true ; log.info( "Credit Card Expiration Year Required." ) ; }
        if ( command.cardcvv().equals("") )     { hasErrors = true ; log.info( "Credit Card CVV Required." ) ; }
        if ( command.email().equals("") )       { hasErrors = true ; log.info( "Email Address Required." ) ; }

        // regex validations: https://www.vogella.com/tutorials/JavaRegularExpressions/article.html 
        if ( !command.zip().matches("\\d{5}") )                             { hasErrors = true ; log.info( "Invalid Zip Code: " + command.zip() ) ; }
        if ( !command.phone().matches("[(]\\d{3}[)] \\d{3}-\\d{4}") )       { hasErrors = true ; log.info( "Invalid Phone Number: " + command.phone() ) ; }
        if ( !command.cardnum().matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}") )    { hasErrors = true ; log.info( "Invalid Card Number: " + command.cardnum() ) ; }
        if ( !command.cardexpyear().matches("\\d{4}") )                     { hasErrors = true ; log.info( "Invalid Card Expiration Year: " + command.cardexpyear() ) ; }
        if ( !command.cardcvv().matches("\\d{3}") )                         { hasErrors = true ; log.info( "Invalid Card CVV: " + command.cardcvv() ) ; }

        // validate months of the year
        if ( months.get( command.cardexpmon()) == null )  { hasErrors = true ; log.info( "Invalid Card Expiration Month: " + command.cardexpmon() ) ; }

        // validate states of 50 U.S. states
        if ( states.get( command.state()) == null )  { hasErrors = true ; log.info( "Invalid State: " + command.state() ) ; }

        /* Render View */
        if (hasErrors) {
            model.addAttribute( "message", "Form Validation Errors!" ) ;
        } else {
            /* Save Command */
            repository.save( command ) ;
            model.addAttribute( "message", "Thank You for your Payment!" ) ;
        }        
        return "creditcards";
    }

}















