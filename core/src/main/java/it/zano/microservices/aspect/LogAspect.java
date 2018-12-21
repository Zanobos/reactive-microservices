package it.zano.microservices.aspect;


import it.zano.microservices.util.LogMaskUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restController() {}

    @Pointcut("execution(* *.*(..))")
    public void allMethod() {}

    @Around("restController() && allMethod()")
    public Object logRequestResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String operation = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info("Entering: {}.{}({})", className, operation, LogMaskUtils.maskFields(args));

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();

        long totalTimeMillis = stopWatch.getTotalTimeMillis();

        logger.info("Exiting: {}.{}, Return: {}, Executed in: {} ms", className, operation,
                LogMaskUtils.maskFields(result.toString()), totalTimeMillis);

        return result;
    }

}
