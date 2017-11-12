package org.egg.aop;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.egg.utils.HideDataUtil;
import org.egg.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;

/**
 * @author dataochen
 * @Description 高级别（取敏感数据logAop）
 * @date: 2017/11/7 18:42
 */
@Aspect
@Configuration
public class SuperLoggerAop {
    private static final Logger LOGGER = LoggerFactory.getLogger(SuperLoggerAop.class);

    /**
     * [大小写不敏感]日志拦截字段名黑名单 不打印
     */
    private final String[] blackArray={"password"};
    /**
     *##[大小写不敏感]日志拦截卡号级别字段 前6后四
     */
    private final String[] cardNoArray={"cardNo","cardNum"};
    /**
     *[大小写不敏感]日志拦截手机号级别字段 前三后4
     */
    private final String[] phoneNumArray={"phonenum","phoneno","tel"};

    //匹配org.egg.controller包及其子包下的所有类的所有方法
    @Pointcut("execution(* org.egg.controller..*.*(..)) || execution(* org.egg.service..*.*(..))")
    public void executeService(){}

    /**
     * 前置通知，方法调用前被调用
     * @param joinPoint
     */
    @Before("executeService()")
    public void doBeforeAdvice(JoinPoint joinPoint) throws IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder("Integration Method:[");
        stringBuilder.append(joinPoint.getSignature().getDeclaringTypeName()).append(".");
        stringBuilder.append(joinPoint.getSignature().getName());
        stringBuilder.append("],Parameters:{");
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            convertLog(stringBuilder, joinPoint, arg);
        }
        stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "}");
        LOGGER.info(stringBuilder.toString());
    }

    @AfterReturning (value = "executeService()",returning = "returnValue")
    public void doAfterAdvice(JoinPoint joinPoint, Object returnValue) throws IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder("Integration result:[");
        stringBuilder.append(joinPoint.getSignature().getDeclaringTypeName()).append(".");
        stringBuilder.append(joinPoint.getSignature().getName());
        stringBuilder.append("],Parameters:{");
        convertLog(stringBuilder, joinPoint, returnValue);
        stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "}");
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

    private void convertLog(StringBuilder stringBuilder,JoinPoint joinPoint,Object arg) throws IllegalAccessException {
        //            判断是否是自定义类(buys)还是java自带类（Interger,String）
        if (arg.getClass().getClassLoader() != null) {
            //自定义类 superlog handler
            Field[] declaredFields = arg.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                Object target = declaredField.get(arg);
                String targetStr = "";
                if (target != null) {
                    if (target.getClass().getClassLoader() != null) {
                        targetStr = JsonUtil.logObjToString(target);
                    } else {
                        targetStr = target.toString();
                    }
                }
                stringBuilder.append(declaredField.getName()).append(":");
                if (ArrayUtils.contains(blackArray, declaredField.getName())) {
                    stringBuilder.append("***").append(",");
                } else if (ArrayUtils.contains(cardNoArray, declaredField.getName())) {
                    stringBuilder.append(HideDataUtil.hideCardNo(targetStr)).append(",");
                } else if (ArrayUtils.contains(phoneNumArray, declaredField.getName())) {
                    stringBuilder.append(HideDataUtil.hidePhoneNo(targetStr)).append(",");
                } else {
                    stringBuilder.append(targetStr).append(",");
                }
            }
        } else {
            // FIXME: 2017/11/7  java自带类型是否可以支持super log
        }
    }

}
