/*
	Gumball API in Go (Version 1)
	Basic Version with no Backend Services
*/

package main

type starbucks_card struct {
	CardNumber string
	CardCode   string
	Balance    float32
//	Server 	   string
	Status     string 
}

type starbucks_order struct {
	Drink 		string
	Milk   		string
	Size    	string
	Total		float32
//	Server		string
	Status		string
}

// Map of Card ID to Card Details
var cards map[string]starbucks_card

// Map of Register ID to Active Order
var orders map[string]starbucks_order

// Array of Sample Drink Prices
var prices = [10]float32{ 2.00, 2.25, 2.50, 3.00, 3.25, 3.50, 3.75, 4.00, 4.50, 5.00}


