package id.co.bni.parameter.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@Slf4j
public class AspectConfig {
    static final String METHOD = " in method ";

    @Around("execution(* id.co.bni.parameter.services..*(..))")
    public Object interceptorForServices(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        String className = getMethodSignatute(joinPoint).getDeclaringType().getSimpleName();
        String methodName = getMethodSignatute(joinPoint).getName();
        try {
            stopWatch.start();
            log.info("Start Services " + className + METHOD + methodName);
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            log.info("End Services " + className + METHOD + methodName + " " + stopWatch.getTotalTimeMillis() + " ms");
        }
    }

    @Around("execution(* id.co.bni.parameter.controller..*(..))")
    public Object interceptorForControllers(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        String className = getMethodSignatute(joinPoint).getDeclaringType().getSimpleName();
        String methodName = getMethodSignatute(joinPoint).getName();
        try {
            stopWatch.start();
            log.info("Start Request " + className + METHOD + methodName);
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            log.info("End Request " + className + METHOD + methodName + " " + stopWatch.getTotalTimeMillis() + " ms");
        }
    }

    private MethodSignature getMethodSignatute(ProceedingJoinPoint joinPoint) {
        return (MethodSignature) joinPoint.getSignature();
    }
}
