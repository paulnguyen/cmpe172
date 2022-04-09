package com.example.springstarbucksapi;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/* 
    https://spring.io/guides/tutorials/rest/ 
	  https://docs.spring.io/spring-data/data-commons/docs/current/reference/html/#mapping.fundamentals
	  https://www.baeldung.com/jpa-indexes    
*/

@Entity
@Table(indexes=@Index(name = "altIndex", columnList = "cardNumber", unique = true))
@Data
@RequiredArgsConstructor
class StarbucksCard {

  private @Id @GeneratedValue Long id;

  @Column(nullable=false)   private String cardNumber ;
  @Column(nullable=false)   private String cardCode ;
  @Column(nullable=false)   private double balance ;
  @Column(nullable=false)   private boolean activated ;
                            private String status ;

  public boolean isActivated() { return activated == true ; }

}

/*
type starbucks_card struct {
	CardNumber string
	CardCode   string
	Balance    float32
	Activated  bool
	Status     string 
}
*/
