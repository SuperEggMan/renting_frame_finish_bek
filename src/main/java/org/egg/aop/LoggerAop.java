package org.egg.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author dataochen
 * @Description 低级别功能log aop
 * @date: 2017/11/7 18:42
 */
//@Aspect
//@Configuration
public class LoggerAop {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerAop.class);

    //匹配org.egg.controller包及其子包下的所有类的所有方法
    // FIXME: 2017/11/7 多个切断问题 test && ||?
    @Pointcut("execution(* org.egg.controller..*.*(..)) || execution(* org.egg.service..*.*(..))")
    public void executeService(){}

    /**
     * 前置通知，方法调用前被调用
     * @param joinPoint
     */
    @Before("executeService()")
    public void doBeforeAdvice(JoinPoint joinPoint){
        StringBuilder stringBuilder = new StringBuilder("Integration Method:[");
        stringBuilder.append(joinPoint.getSignature().getDeclaringTypeName()).append(".");
        stringBuilder.append(joinPoint.getSignature().getName());
        stringBuilder.append("],Parameters:");
        if (joinPoint.getArgs() != null) {
            stringBuilder.append(Arrays.toString(joinPoint.getArgs()));
        } else {
            stringBuilder.append("[null]");
        }
        LOGGER.info(stringBuilder.toString());
    }

    @AfterReturning (value = "executeService()",returning = "returnValue")
    public void doAfterAdvice(JoinPoint joinPoint, Object returnValue){
        StringBuilder stringBuilder = new StringBuilder("Integration result:");
        if (returnValue != null) {
            stringBuilder.append(returnValue.toString());
        } else {
            stringBuilder.append("[null]");
        }
        LOGGER.info(stringBuilder.toString());
        System.out.println("后置通知执行了!!!!");
    }

    @Around("executeService()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long time = System.currentTimeMillis();
        Object retVal = proceedingJoinPoint.proceed();
        time = System.currentTimeMillis() - time;
        StringBuilder stringBuilder = new StringBuilder("Integration performance Method:[");
        stringBuilder.append(proceedingJoinPoint.getSignature().getDeclaringTypeName()).append(".");
        stringBuilder.append(proceedingJoinPoint.getSignature().getName());
        stringBuilder.append("],Time: ").append(time).append(" ms");
        return retVal;
    }

}
