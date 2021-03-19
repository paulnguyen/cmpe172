/*
	Gumball API in Go (Version 1)
	Basic Version with no Backend Services
*/

package main

type starbucks_card struct {
	CardNumber string
	CardCode   string
	Balance    float64
	Activated  bool
	Status     string 
}

type starbucks_order struct {
	Drink 		string
	Milk   		string
	Size    	string
	Total		float64
	Status		string
}

// Map of Card ID to Card Details
var cards map[string]starbucks_card

// Map of Register ID to Active Order
var orders map[string]starbucks_order

// Array of Sample Drink Prices
var prices = [10]float64{ 1.00, 1.25, 1.50, 2.00, 2.25, 2.50, 1.75, 1.10, 2.10, 1.30 }


