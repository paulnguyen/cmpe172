package com.example.springaop.tutorial2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AdderBeforeAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.example.springaop.tutorial2.SampleAdder.*(..))")
    public void adderBeforeMethods() {
    }

    @Before("adderBeforeMethods()")
    public void beforeAdvice() throws Throwable {
        logger.info("I would be executed just before method starts");
    }
}

