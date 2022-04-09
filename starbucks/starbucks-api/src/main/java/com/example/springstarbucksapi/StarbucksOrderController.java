package com.example.springstarbucksapi;

import java.util.List;
import java.util.HashMap;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

/* 
    https://spring.io/guides/tutorials/rest/ 
    https://www.baeldung.com/building-a-restful-web-service-with-spring-and-java-based-configuration
*/

@RestController
public class StarbucksOrderController {

	private final StarbucksOrderRepository repository;

    @Autowired
    private StarbucksCardRepository cardsRepository ;

	class Message {
		private String status;

		public String getStatus() {
			return status;
		}

		public void setStatus(String msg) {
			status = msg;
		}
	}

	// https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html
	private HashMap<String, StarbucksOrder> orders = new HashMap<>();

	public StarbucksOrderController(StarbucksOrderRepository repository) {
		this.repository = repository;
	}

	/* Get List of Starbucks Orders */
	@GetMapping("/orders")
	List<StarbucksOrder> all() {
		return repository.findAll();
	}

	/* https://docs.spring.io/spring-data/jpa/docs/2.4.5/api/ *

	/* Delete All Starbucks Orders (Cleanup for Unit Testing) */
	@DeleteMapping("/orders")
	Message deleteAll() {
		repository.deleteAllInBatch() ;
		orders.clear() ;
		Message msg = new Message() ;
		msg.setStatus( "All Orders Cleared!" ) ;
		return msg ;
	}

	/* Create a New Starbucks Order */
	@PostMapping("/order/register/{regid}")
	@ResponseStatus(HttpStatus.CREATED)
	StarbucksOrder newOrder(@PathVariable String regid, @RequestBody StarbucksOrder order) {
		System.out.println("Placing Order (Reg ID = " + regid + ") => " + order);
		// check input
		if (order.getDrink().equals("") || order.getMilk().equals("") || order.getSize().equals("")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Order Request!");
		}
		// check for active order
		StarbucksOrder active = orders.get(regid);
		if (active != null) {
			System.out.println("Active Order (Reg ID = " + regid + ") => " + active);
			if (active.getStatus().equals("Ready for Payment."))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Active Order Exists!");
		}
		// set price
		double price = 0.0;
		switch (order.getDrink()) {
		case "Caffe Latte":
			switch (order.getSize()) {
			case "Tall":
				price = 2.95;
				break;
			case "Grande":
				price = 3.65;
				break;
			case "Venti":
			case "Your Own Cup":
				price = 3.95;
				break;
			default:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Size!");
			}
			break;
		case "Caffe Americano":
			switch (order.getSize()) {
			case "Tall":
				price = 2.25;
				break;
			case "Grande":
				price = 2.65;
				break;
			case "Venti":
			case "Your Own Cup":
				price = 2.95;
				break;
			default:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Size!");
			}
			break;
		case "Caffe Mocha":
			switch (order.getSize()) {
			case "Tall":
				price = 3.45;
				break;
			case "Grande":
				price = 4.15;
				break;
			case "Venti":
			case "Your Own Cup":
				price = 4.45;
				break;
			default:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Size!");
			}
			break;
		case "Espresso":
			switch (order.getSize()) {
			case "Short":
				price = 1.75;
				break;
			case "Tall":
				price = 1.95;
				break;
			default:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Size!");
			}
			break;
		case "Cappuccino":
			switch (order.getSize()) {
			case "Tall":
				price = 2.95;
				break;
			case "Grande":
				price = 3.65;
				break;
			case "Venti":
			case "Your Own Cup":
				price = 3.95;
				break;
			default:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Size!");
			}
			break;
		default:
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Drink!");
		}
		double tax = 0.0725;
		double total = price + (price * tax);
		double scale = Math.pow(10, 2);
		double rounded = Math.round(total * scale) / scale;
		order.setTotal(rounded);
		// save order
		order.setStatus("Ready for Payment.");
		StarbucksOrder new_order = repository.save(order);
		orders.put(regid, new_order);
		return new_order;
	}

	/* Get Details of a Starbucks Card */
	@GetMapping("/order/register/{regid}")
	StarbucksOrder getActiveOrder(@PathVariable String regid, HttpServletResponse response) {
		StarbucksOrder active = orders.get(regid);
		if (active != null) {
			return active;
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order Not Found!");
		}
	}

	/* Clear Active Order */
	@DeleteMapping("/order/register/{regid}")
	Message deleteActiveOrder(@PathVariable String regid) {
		StarbucksOrder active = orders.get(regid);
		if (active != null) {
			orders.remove(regid);
			Message msg = new Message();
			msg.setStatus("Active Order Cleared!");
			return msg;
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order Not Found!");
		}
	}


	/*  Process payment for the "active" Order. */
	@PostMapping("/order/register/{regid}/pay/{cardnum}")
	StarbucksCard processOrder(@PathVariable String regid, @PathVariable String cardnum ) {
		System.out.println( "Pay for Order: Reg ID = " + regid + " Using Card = " + cardnum ) ;
		StarbucksOrder active = orders.get(regid);
		if (active == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order Not Found!");
		}
		if ( cardnum.equals("") ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card Number Not Provided!");
		}
		if ( active.getStatus().startsWith("Paid with Card") ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clear Paid Active Order!");
		}
		StarbucksCard card = cardsRepository.findByCardNumber(cardnum) ;
		if ( card == null ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card Not Found!");
		}
		if ( !card.isActivated() ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Card Not Activated.");
		}
		double price = active.getTotal() ;
		double balance = card.getBalance() ;
		if ( balance - price < 0 ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient Funds on Card.");
		}
		double new_balance = balance - price ;
		card.setBalance( new_balance ) ;
		String status = "Paid with Card: " + cardnum + " Balance: $" + new_balance + "." ;
		active.setStatus( status ) ;
		cardsRepository.save( card ) ;
		repository.save( active ) ;
		return card ;
	}

}



/*

https://priceqube.com/menu-prices/%E2%98%95-starbucks

var DRINK_OPTIONS = [ "Caffe Latte", "Caffe Americano", "Caffe Mocha", "Espresso", "Cappuccino" ];
var MILK_OPTIONS  = [ "Whole Milk", "2% Milk", "Nonfat Milk", "Almond Milk", "Soy Milk" ];
var SIZE_OPTIONS  = [ "Short", "Tall", "Grande", "Venti", "Your Own Cup" ];

Caffè Latte
=============
tall 	$2.95
grande 	$3.65
venti 	$3.95 (Your Own Cup)

Caffè Americano
===============
tall 	$2.25
grande 	$2.65
venti 	$2.95 (Your Own Cup)

Caffè Mocha
=============
tall 	$3.45
grande 	$4.15
venti 	$4.45 (Your Own Cup)

Cappuccino
==========
tall 	$2.95
grande 	$3.65
venti 	$3.95 (Your Own Cup)

Espresso
========
short 	$1.75
tall 	$1.95


POST    /order/register/{regid}
        Create a new order. Set order as "active" for register.

        Request:

	    {
	      "Drink": "Latte",
	      "Milk":  "Whole",
	      "Size":  "Grande"
	    }         

	    Response:

		{
		  "Drink": "Latte",
		  "Milk": "Whole",
		  "Size": "Grande",
		  "Total": 2.413125,
		  "Status": "Ready for Payment."
		}	    

GET     /order/register/{regid}
        Request the current state of the "active" Order.

		{
		  "Drink": "Latte",
		  "Milk": "Whole",
		  "Size": "Grande",
		  "Total": 2.413125,
		  "Status": "Ready for Payment."
		}

DELETE  /order/register/{regid}
        Clear the "active" Order.

		{
		  "Status": "Active Order Cleared!"
		}

POST    /order/register/{regid}/pay/{cardnum}
        Process payment for the "active" Order. 

        Response: (with updated card balance)

		{
		  "CardNumber": "627131848",
		  "CardCode": "547",
		  "Balance": 15.17375,
		  "Activated": true,
		  "Status": ""
		}

GET     /orders
        Get a list of all active orders (for all registers)

		{
		  "5012349": {
		    "Drink": "Latte",
		    "Milk": "Whole",
		    "Size": "Grande",
		    "Total": 4.82625,
		    "Status": "Paid with Card: 627131848 Balance: $15.17."
		  }
		}

DELETE 	/orders
		Delete all Orders (Use for Unit Testing Teardown)

		{
		  "Status": "All Orders Cleared!"
		}

*/