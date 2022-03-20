
package com.example.springgumball;

import org.springframework.http.HttpStatus ;
import org.springframework.web.bind.annotation.ResponseStatus ;

/*

    References:

    https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
    https://www.baeldung.com/tag/spring-mvc-basics/
    https://www.baeldung.com/spring-response-status
    https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html

*/

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Invalid Request")  // 500
public class GumballServerError extends RuntimeException { }

