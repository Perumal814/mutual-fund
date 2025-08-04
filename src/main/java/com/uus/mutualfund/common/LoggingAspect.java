package com.uus.mutualfund.common;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Aspect
@Component
public class LoggingAspect {

	@Pointcut("execution(* com.uus.mutualfund.controller..*(..))")
	public void controllerLayer() {
	}

	@Pointcut("execution(* com.uus.mutualfund.service..*(..))")
	public void serviceLayer() {
	}

	@Pointcut("execution(* com.uus.mutualfund.repository..*(..))")
	public void repositoryLayer() {
	}

	@Before("controllerLayer()")
	public void logBeforeController(JoinPoint joinPoint) {
		log.info("[Controller] Entering: " + joinPoint.getSignature());
	}

	@After("controllerLayer()")
	public void logAfterController(JoinPoint joinPoint) {
		log.info("[Controller] Exiting: " + joinPoint.getSignature());
	}

	@Before("serviceLayer()")
	public void logBeforeService(JoinPoint joinPoint) {
		log.info("[Service] Entering: " + joinPoint.getSignature());
	}

	@After("serviceLayer()")
	public void logAfterService(JoinPoint joinPoint) {
		log.info("[Service] Exiting: " + joinPoint.getSignature());
	}

	@Around("serviceLayer()")
	public Object logServiceExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		Object output = joinPoint.proceed();
		long timeTaken = System.currentTimeMillis() - start;
		log.info("[Service] Execution time: " + joinPoint.getSignature() + " = " + timeTaken + "ms");
		return output;
	}

	@Before("repositoryLayer()")
	public void logBeforeRepository(JoinPoint joinPoint) {
		log.info("[Repository] Entering: " + joinPoint.getSignature());
	}

	@After("repositoryLayer()")
	public void logAfterRepository(JoinPoint joinPoint) {
		log.info("[Repository] Exiting: " + joinPoint.getSignature());
	}

	@AfterThrowing(pointcut = "controllerLayer() || serviceLayer() || repositoryLayer()", throwing = "ex")
	public void logAnyException(JoinPoint joinPoint, Throwable ex) {
		log.error("Exception in: {} | Args: {} | Error: {}", joinPoint.getSignature(),
				Arrays.toString(joinPoint.getArgs()), ex.getMessage());
	}
}
