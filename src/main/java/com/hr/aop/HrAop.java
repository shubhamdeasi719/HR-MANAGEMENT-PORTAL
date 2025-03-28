package com.hr.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class HrAop {
	 private Logger logger = LoggerFactory.getLogger(HrAop.class);

	    @Around("execution(* com.hr.controller.HrController.*(..))")
	    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
	        long startTime = System.currentTimeMillis();
	        Object result = joinPoint.proceed();
	        long endTime = System.currentTimeMillis();
	        logger.info("Method {} executed in {} ms", joinPoint.getSignature().getName(), endTime - startTime);
	        return result;
	    }
}

