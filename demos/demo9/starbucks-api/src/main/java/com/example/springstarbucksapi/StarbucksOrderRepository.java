package com.example.springstarbucksapi;

/* https://docs.spring.io/spring-data/jpa/docs/2.4.6/reference/html/#repositories */

import org.springframework.data.jpa.repository.JpaRepository;

interface StarbucksOrderRepository extends JpaRepository<StarbucksOrder, Long> {

}


