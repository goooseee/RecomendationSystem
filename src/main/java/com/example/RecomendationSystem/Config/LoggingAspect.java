package com.example.RecomendationSystem.Config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class LoggingAspect {
	
	@Pointcut("execution(* com.example.RecomendationSystem.Service.*.*(..))")
	public void serviceLayer() {}
	
	@Around("serviceLayer()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		String className = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		
		log.atInfo().log("[Aop-autologging] Entering {}.{}() with args {}", className, methodName, args);
		
		long start = System.currentTimeMillis();
		
		try {
			Object result = joinPoint.proceed();
			
			long finish = System.currentTimeMillis() - start;
			log.atInfo().log("[Aop-autologging] Exiting {}.{}() | Time {} ms", className, methodName, finish);
			return result;
		}
		catch (Exception e) {
			log.atError().log("[Aop-autologging] Exception in {}.{}(): {}", className, methodName, e.getMessage());
			throw e;
		}
	}
	
}
