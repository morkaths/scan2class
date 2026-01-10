package com.morkath.scan2class.aspect;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)" +
            " || within(@org.springframework.stereotype.Controller *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the
        // advices.
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut("execution(* com.morkath.scan2class.controller..*(..))" +
            " || execution(* com.morkath.scan2class.service..*(..))")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the
        // advices.
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws IllegalArgumentException
     */
    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
        String methodName = joinPoint.getSignature().getName();

        if (log.isDebugEnabled()) {
            log.debug("Enter: {}() with argument[s] = {}", methodName, Arrays.toString(joinPoint.getArgs()));
        }

        Instant start = Instant.now();
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}()", Arrays.toString(joinPoint.getArgs()), methodName);
            throw e;
        } catch (Exception e) {
            log.error("Exception in {}() with cause = {}", methodName, e.getCause() != null ? e.getCause() : "NULL");
            throw e;
        }
        Instant end = Instant.now();
        long timeElapsed = Duration.between(start, end).toMillis();

        if (log.isDebugEnabled()) {
            log.debug("Exit: {}() with result = {} (Execution time: {} ms)", methodName, result, timeElapsed);
        } else {
            // Info level logs for execution time tracking (useful for perf monitoring)
            log.info("Finished: {}() in {} ms", methodName, timeElapsed);
        }

        return result;
    }
}
