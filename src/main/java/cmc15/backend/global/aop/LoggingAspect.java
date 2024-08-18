package cmc15.backend.global.aop;

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

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    @Pointcut("execution(public * cmc15.backend.domain..controller..*(..))")
    public void controllerMethods() {}

    @Around("controllerMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        logger.info("\n호출된 메서드 : {} \n요청 값 : {}", joinPoint.getSignature(), joinPoint.getArgs());

        Object result;
        try {
            result = joinPoint.proceed(); // 실제 메서드 호출
        } catch (Throwable throwable) {
            logger.error("\n예외발생 메서드 {}: {}", joinPoint.getSignature(), throwable.getMessage());
            throw throwable;
        }

        long elapsedTime = System.currentTimeMillis() - start;

        // 메서드 호출 후 로깅
        logger.info("\n호출 완료된 메서드 : {} \nResponse: {} \nExecution time: {} ms",
                joinPoint.getSignature(),
                result != null ? result.toString() : "null",
                elapsedTime);

        return result;
    }
}