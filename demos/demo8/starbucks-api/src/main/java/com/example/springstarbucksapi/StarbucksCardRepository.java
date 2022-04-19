package com.example.springstarbucksapi;

/* https://spring.io/guides/tutorials/rest/ */

import org.springframework.data.jpa.repository.JpaRepository;

interface StarbucksCardRepository extends JpaRepository<StarbucksCard, Long> {

    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods
	// https://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#repositories.query-methods.query-creation
	
	StarbucksCard findByCardNumber(String cardNumber) ;

}


