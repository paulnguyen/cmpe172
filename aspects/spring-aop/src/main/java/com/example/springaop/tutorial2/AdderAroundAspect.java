package com.example.springaop.tutorial2;

import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AdderAroundAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.example.springaop.tutorial2.SampleAdder.*(..))")
    public void adderArroundMethods() {
    }
    
    @Around("adderArroundMethods()")
    public Object aroundAdvice(final ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Arguments passed to method are: " + Arrays.toString(joinPoint.getArgs()));
        final Object result = joinPoint.proceed();
        logger.info("Result from method is: " + result);
        return result;
    }
}

