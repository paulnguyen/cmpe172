package com.example.springaop.tutorial2;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AdderAfterAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.example.springaop.tutorial2.SampleAdder.*(..))")
    public void adderAfterMethods() {
    }

    @After("adderAfterMethods()")
    public void afterAdvice() throws Throwable {
        logger.info("I'm done calling the method");
    }

}

