/*
	Starbucks API 
*/

package main

import (

	"fmt"
//	"os"
	"math"
	"strings"
	"math/rand"
	"net/http"
	"strconv"
	"encoding/json"

	"github.com/codegangsta/negroni"
	"github.com/gorilla/mux"
	"github.com/unrolled/render"
)

// NewServer configures and returns a Server.

func NewServer() *negroni.Negroni {
	formatter := render.New(render.Options{
		IndentJSON: true,
	})
	n := negroni.Classic()
	mx := mux.NewRouter()
	initRoutes(mx, formatter)
	n.UseHandler(mx)
	return n
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

GET 	/cards/{num}
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

DELETE 	/cards
		Delete all Cards (Use for Unit Testing Teardown)

		{
		  "Status": "All Cards Cleared!"
		}

DELETE 	/orders
		Delete all Orders (Use for Unit Testing Teardown)

		{
		  "Status": "All Orders Cleared!"
		}

See:  https://www.codementor.io/codehakase/building-a-restful-api-with-golang-a6yivzqdo

*/


// API Routes
func initRoutes(mx *mux.Router, formatter *render.Render) {
	mx.HandleFunc("/ping", pingHandler(formatter)).Methods("GET")
	mx.HandleFunc("/cards", starbucksCardsGetHandler(formatter)).Methods("GET")
	mx.HandleFunc("/cards", starbucksCardsPostHandler(formatter)).Methods("POST")
	mx.HandleFunc("/cards/{num}", starbucksCardsGetHandler(formatter)).Methods("GET")
	mx.HandleFunc("/card/activate/{num}/{code}", starbucksCardActivateHandler(formatter)).Methods("POST")
	mx.HandleFunc("/order/register/{regid}", starbucksNewOrderHandler(formatter)).Methods("POST")
	mx.HandleFunc("/order/register/{regid}", starbucksGetOrderHandler(formatter)).Methods("GET")
	mx.HandleFunc("/order/register/{regid}", starbucksClearOrderHandler(formatter)).Methods("DELETE")
	mx.HandleFunc("/order/register/{regid}/pay/{cardnum}", starbucksProcessOrderPaymentHandler(formatter)).Methods("POST")
	mx.HandleFunc("/orders", starbucksGetActiveOrders(formatter)).Methods("GET")
	mx.HandleFunc("/orders", starbucksDeleteOrdersHandler(formatter)).Methods("DELETE")
	mx.HandleFunc("/cards", starbucksDeleteCardsHandler(formatter)).Methods("DELETE")
}

// API Ping Handler
func pingHandler(formatter *render.Render) http.HandlerFunc {
	return func(w http.ResponseWriter, req *http.Request) {
		formatter.JSON(w, http.StatusOK, struct{ Test string }{"Starbucks API version 1.0.1 alive!"})
	}
}

// Place New Order Starbucks Order Handler
func starbucksNewOrderHandler(formatter *render.Render) http.HandlerFunc {
	return func(w http.ResponseWriter, req *http.Request) {
		params := mux.Vars(req)
		var regid string = params["regid"]
		var order starbucks_order
		_ = json.NewDecoder(req.Body).Decode(&order)
		if orders == nil {
			orders = make(map[string]starbucks_order)
		}
		var active = orders[regid]
		fmt.Printf("Active Order: %+v\n", active)
		if order.Drink == "" || order.Milk == "" || order.Size == "" {
			fmt.Printf("Invalid Order: %+v\n", order)
			formatter.JSON(w, http.StatusBadRequest, struct{ Status string }{ "Invalid Order Request!" })
		} else if active.Status == "Ready for Payment." {
			fmt.Println("Active Order Exists!")
			formatter.JSON(w, http.StatusBadRequest, struct{ Status string }{ "An Active Order Exists!" })			
		} else {
			// Create a New Order
			var index = rand.Intn(10)
			var price = prices[index] + (prices[index] * 0.0725)
			var round = math.Round(price*100)/100
			order.Total = round
			order.Status = "Ready for Payment."
			orders[regid] = order
			fmt.Println("Register: ", regid)
			fmt.Printf("New Order: %+v\n", order)			
			formatter.JSON(w, http.StatusOK, order)
		}
	}
}

// Request the current state of the "active" Order Handler
func starbucksGetOrderHandler(formatter *render.Render) http.HandlerFunc {
	return func(w http.ResponseWriter, req *http.Request) {
		params := mux.Vars(req)
		var regid string = params["regid"]
		var order = orders[regid]
		fmt.Println("Register: ", regid)
		if order == (starbucks_order{}) {
			//name, _ := os.Hostname()
			error := struct{ 					//Server string 
					Status string }{ 
						//name, 
						"Order Not Found!",
					}
			formatter.JSON(w, http.StatusNotFound, error )			
		} else {
			//name, _ := os.Hostname()
			//order.Server = name
			fmt.Printf("Active Order: %+v\n", order)
			formatter.JSON(w, http.StatusOK, order)
		}
	}
}

// Get a list of all active orders (for all registers)
func starbucksGetActiveOrders(formatter *render.Render) http.HandlerFunc {
	return func(w http.ResponseWriter, req *http.Request) {
		fmt.Printf("Active Orders: ", orders)
		formatter.JSON(w, http.StatusOK, orders)
	}
}


// Clear the "active" Order Handler
func starbucksClearOrderHandler(formatter *render.Render) http.HandlerFunc {
	return func(w http.ResponseWriter, req *http.Request) {
		params := mux.Vars(req)
		var regid string = params["regid"]
		var order = orders[regid]
		fmt.Println("Register: ", regid)
		if order == (starbucks_order{}) {
			formatter.JSON(w, http.StatusNotFound, struct{ Status string }{ "Error. Order Not Found!" })			
		} else {
			fmt.Printf("Clearing Active Order: %+v\n", order)
			delete ( orders, regid )
			formatter.JSON(w, http.StatusOK, struct{ Status string }{ "Active Order Cleared!" })
		}
	}
}

// Process payment for the "active" Order Handler
func starbucksProcessOrderPaymentHandler(formatter *render.Render) http.HandlerFunc {
	return func(w http.ResponseWriter, req *http.Request) {
		params := mux.Vars(req)
		var regid string = params["regid"]
		var cardnum string = params["cardnum"]
		var card = cards[cardnum]
		var order = orders[regid]
		fmt.Println("Register: ", regid)
		fmt.Println("Card Number: ", cardnum)
		if order == (starbucks_order{}) {
			formatter.JSON(w, http.StatusNotFound, struct{ Status string }{ "Error. Order Not Found!" })			
		} else if cardnum == "" {
			formatter.JSON(w, http.StatusBadRequest, struct{ Status string }{ "Error. Card Number Not Provided!" })	
		} else if strings.HasPrefix(order.Status, "Paid with Card:") {
			formatter.JSON(w, http.StatusBadRequest, struct{ Status string }{ "Clear Paid Active Order!" })	
		} else {
			var price = order.Total
			fmt.Printf("Processing Payment for Active Order: %+v\n", order)
			if (!card.Activated) {
				order.Status = "Card Not Activated."
				orders[regid] = order
				formatter.JSON( w, http.StatusBadRequest, struct{ Status string }{ "Card Not Activated." } )	
			} else if (card.Balance-price < 0) {
				order.Status = "Insufficient Funds on Card."
				orders[regid] = order
				card.Status = "Insufficient Funds on Card."
				formatter.JSON( w, http.StatusBadRequest, card )	
			} else {
				card.Balance -= price
				cards[cardnum] = card
				bal := fmt.Sprintf("%1.2f", card.Balance)
				order.Status = "Paid with Card: " + cardnum + " Balance: $" + bal + "."
				orders[regid] = order
				formatter.JSON(w, http.StatusOK, card)
			}
		}
	}
}

// Get Starbucks Cards Handler (List of Just One)
func starbucksCardsGetHandler(formatter *render.Render) http.HandlerFunc {
	return func(w http.ResponseWriter, req *http.Request) {
		params := mux.Vars(req)
		var cardnum string = params["num"]
		fmt.Println("Card Number: ", cardnum)
		if cardnum == "" {
			fmt.Println("Cards:", cards)
			var cards_array []starbucks_card
			for key, value := range cards {
				fmt.Println("Key:", key, "Value:", value)
				cards_array = append(cards_array, value)
			}
			formatter.JSON(w, http.StatusOK, cards_array)
		} else {
			var card = cards[cardnum]
			if (starbucks_card{} == card) {
				formatter.JSON(w, http.StatusNotFound, struct{ Status string }{ "Error. Card Not Found!" })	
			} else {
				fmt.Println("Card: ", card)
				formatter.JSON(w, http.StatusOK, card)
			}
		}
	}
}


// Activate Starbucks Card Handler
func starbucksCardActivateHandler(formatter *render.Render) http.HandlerFunc {
	return func(w http.ResponseWriter, req *http.Request) {
		params := mux.Vars(req)
		var cardnum string = params["num"]
		var cardcode string = params["code"]
		fmt.Println("Card Number: ", cardnum)
		fmt.Println("Card Code: ", cardcode)
		var card = cards[cardnum]

		// special card for testing
		if ( cardnum == "123456789" && cardcode == "999" ) {
			var card = starbucks_card{
				CardNumber: cardnum,
				CardCode:   cardcode,
				Balance:    20.00,
				Activated:  true,
			}
			if cards == nil {
				cards = make(map[string]starbucks_card)
			}
			cards[cardnum] = card	
			formatter.JSON(w, http.StatusOK, card)
		}

		if (starbucks_card{} == card) {
			formatter.JSON(w, http.StatusNotFound, struct{ Status string }{ "Error. Card Not Found!" })	
		} else {
			fmt.Println("Card: ", card)
			if ( card.CardNumber == cardnum &&
				 card.CardCode == cardcode ) {
				 	card.Activated = true 
				 	cards[cardnum] = card
				 	formatter.JSON(w, http.StatusOK, card)
			} else {
				formatter.JSON(w, http.StatusNotFound, struct{ Status string }{  "Error. Card Not Valid!" })	
			}			
		}
	}
}


// Create New Starbucks Card Handler
func starbucksCardsPostHandler(formatter *render.Render) http.HandlerFunc {
	return func(w http.ResponseWriter, req *http.Request) {

		var cardnum = strconv.Itoa(100000000 + rand.Intn(999999999-100000000))
		var cardcode = strconv.Itoa(100 + rand.Intn(999-100))

		var card = starbucks_card{
			CardNumber: cardnum,
			CardCode:   cardcode,
			Balance:    20.00,
		}
		if cards == nil {
			cards = make(map[string]starbucks_card)
		}
		cards[cardnum] = card
		card.Status = "New Card."
		fmt.Println("Cards: ", cards)
		formatter.JSON(w, http.StatusOK, card)
	}
}

// Delete all Orders Handler
func starbucksDeleteOrdersHandler(formatter *render.Render) http.HandlerFunc {
	return func(w http.ResponseWriter, req *http.Request) {
		orders = make(map[string]starbucks_order)
		formatter.JSON(w, http.StatusOK, struct{ Status string }{ "All Orders Cleared!" })
	}
}

// Delete all Cards Handler
func starbucksDeleteCardsHandler(formatter *render.Render) http.HandlerFunc {
	return func(w http.ResponseWriter, req *http.Request) {
		cards = make(map[string]starbucks_card)
		formatter.JSON(w, http.StatusOK, struct{ Status string }{ "All Cards Cleared!" })
	}
}



