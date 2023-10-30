package com.intellias.basicsandbox.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.util.Arrays;

@Interceptor
@Loggable
public class LoggingInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

    @AroundInvoke
    public Object logMethod(InvocationContext context) throws Exception {
        log.info("Starting: method {} execution with params: {}",
                context.getMethod().getName(),
                Arrays.toString(context.getParameters()));

        Object result;
        try {
            result = context.proceed();
            log.info("Return after execution method {}. Returned value: {}", context.getMethod().getName(), result);
        } catch (Exception e) {
            log.warn("Exception in method: {}. Cause: {}", context.getMethod().getName(), e.getMessage());
            throw e;
        }
        return result;
    }
}

