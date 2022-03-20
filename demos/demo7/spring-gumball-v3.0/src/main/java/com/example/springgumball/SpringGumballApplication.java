package com.example.springgumball;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringGumballApplication {

	public static void main(String[] args) {

		// REF:  https://docs.oracle.com/javase/8/docs/api/java/lang/System.html
		System.out.println( "MYSQL_HOST: " + System.getenv("MYSQL_HOST") ) ;

		// Start Spring App
		SpringApplication.run(SpringGumballApplication.class, args);
	}

}
