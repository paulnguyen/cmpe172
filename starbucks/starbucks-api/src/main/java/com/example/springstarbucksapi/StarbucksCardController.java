package com.example.springstarbucksapi;

import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus ;

/* https://spring.io/guides/tutorials/rest/ */

@RestController
class StarbucksCardController {

  	private final StarbucksCardRepository repository;

	class Message {
		private String status;

		public String getStatus() {
			return status;
		}

		public void setStatus(String msg) {
			status = msg;
		}
	}

 	StarbucksCardController(StarbucksCardRepository repository) {
    	this.repository = repository;
  	}

  /* Create a New Starbucks Card */
  @PostMapping("/cards")
  @ResponseStatus(HttpStatus.CREATED)
  StarbucksCard newCard() {
    StarbucksCard newcard = new StarbucksCard() ;

    Random random = new Random();
    int num = random.nextInt(900000000) + 100000000;
    int code = random.nextInt(900) + 100;
    
    newcard.setCardNumber(String.valueOf(num)) ;
    newcard.setCardCode(String.valueOf(code)) ;
    newcard.setBalance(20.00) ;
    newcard.setActivated(false) ;
    newcard.setStatus("New Card") ;
    return repository.save( newcard );

  }

  /* Get List of Starbucks Cards */
  @GetMapping("/cards")
  List<StarbucksCard> all() {
    return repository.findAll();
  }

  /* https://docs.spring.io/spring-data/jpa/docs/2.4.5/api/ *

  /* Delete All Starbucks Cards (Cleanup for Unit Testing) */
  @DeleteMapping("/cards")
  Message deleteAll() {
    repository.deleteAllInBatch() ;
	Message msg = new Message() ;
	msg.setStatus( "All Cards Cleared!" ) ;
	return msg ;
  }

  /* 
    https://www.baeldung.com/spring-pathvariable
    https://www.baeldung.com/exception-handling-for-rest-with-spring
  */

  /* Get Details of a Starbucks Card */
  @GetMapping("/card/{num}")
  StarbucksCard getOne(@PathVariable String num, HttpServletResponse response) {
    StarbucksCard card = repository.findByCardNumber(num) ;
    if ( card == null )
        throw new ResponseStatusException( HttpStatus.NOT_FOUND, "Error. Card Not Found!" ) ;
    return card ;
  }

  /* Activate a Starbucks Card */
  @PostMapping("/card/activate/{num}/{code}")
  StarbucksCard activate(@PathVariable String num, @PathVariable String code, HttpServletResponse response) {
    StarbucksCard card = repository.findByCardNumber(num) ;
    if ( card == null )
        throw new ResponseStatusException( HttpStatus.NOT_FOUND, "Error. Card Not Found!" ) ;
    if ( card.getCardCode().equals(code) ) {
        card.setActivated(true) ;
        repository.save(card) ;
    } else {
        throw new ResponseStatusException( HttpStatus.NOT_FOUND, "Error. Card Not Valid!" ) ;
    }
    return card ;
  }


}

/*

GET 	/ping
		Ping Health Check.

		{
		  "Test": "Starbucks API version 1.0 alive!"
		}		

GET 	/cards 
		Get a list of Starbucks Cards (along with balances).

		[
		  {
		    "CardNumber": "498498082",
		    "CardCode": "425",
		    "Balance": 20,
		    "Activated": false,
		    "Status": ""
		  },
		  {
		    "CardNumber": "627131848",
		    "CardCode": "547",
		    "Balance": 20,
		    "Activated": false,
		    "Status": ""
		  }
		]		

POST 	/cards
		Create a new Starbucks Card.

		{
		  "CardNumber": "498498082",
		  "CardCode": "425",
		  "Balance": 20,
		  "Activated": false,
		  "Status": "New Card."
		}

GET 	/card/{num}
		Get the details of a specific Starbucks Card.

		{
		  "CardNumber": "627131848",
		  "CardCode": "547",
		  "Balance": 20,
		  "Activated": false,
		  "Status": ""
		}		

POST 	/card/activate/{num}/{code}
		Activate Card 

		{
		  "CardNumber": "627131848",
		  "CardCode": "547",
		  "Balance": 20,
		  "Activated": true,
		  "Status": ""
		}

DELETE 	/cards
		Delete all Cards (Use for Unit Testing Teardown)

		{
		  "Status": "All Cards Cleared!"
		}

*/