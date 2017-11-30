package org.egg.aop;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.egg.utils.HideDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author dataochen
 * @Description 高级别（取敏感数据logAop）
 * 支持实体嵌套实体，支持实体包含list,map，支持无实体多参数方法
 * 不支持含有重载方法的类
 * 如果含有重载的方法（isOverWirte=true），不支持入参的参数为空，因为入参参数为空，导致拿不到为空参数的参数类型，从而找不到对应的方法
 *
 * @date: 2017/11/7 18:42
 */
@Aspect
@Configuration
public class SuperLoggerAop {
    private static final Logger LOGGER = LoggerFactory.getLogger(org.egg.aop.SuperLoggerAop.class);

    /**
     * [大小写不敏感]日志拦截字段名黑名单 不打印
     */
    private final String[] blackArray = {"password"};
    /**
     * ##[大小写不敏感]日志拦截卡号级别字段 前6后四
     */
    private final String[] cardNoArray = {"cardNo", "cardNum"};
    /**
     * [大小写不敏感]日志拦截手机号级别字段 前三后4
     */
    private final String[] phoneNumArray = {"phonenum", "phoneno", "tel"};
    /**
     * 是否显示null字段
     */
    private final Boolean isDisplayNull = true;
    /**
     * 查找方法是否含有重载方法
     */
    private final Boolean isOverWirte = false;

    //匹配org.egg.controller包及其子包下的所有类的所有方法
    @Pointcut("execution(* org.egg.controller..*.*(..)) || execution(* org.egg.service..*.*(..))")
    public void executeService() {
    }

    /**
     * 前置通知，方法调用前被调用
     *
     * @param joinPoint
     */
    @Before("executeService()")
    public void doBeforeAdvice(JoinPoint joinPoint) throws IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder("Integration Method:[");
        stringBuilder.append(joinPoint.getSignature().getDeclaringTypeName()).append(".");
        stringBuilder.append(joinPoint.getSignature().getName());
        stringBuilder.append("],Parameters:");
        Method declaredMethod = null;
        if (isOverWirte) {
            declaredMethod = getDeclaredMethod(joinPoint);
        } else {
            declaredMethod = getDeclaredMethodForName(joinPoint);
        }
        if (declaredMethod != null) {
//        参数值
            Object[] args = joinPoint.getArgs();
            Parameter[] parameters = declaredMethod.getParameters();
            if (parameters != null) {
                stringBuilder.append("[");
                for (int i = 0; i < parameters.length; i++) {
                    String s = convertLog(stringBuilder, parameters[i], args[i], false, parameters[i].getName());
                    if (StringUtils.isNotBlank(s)) {
                        stringBuilder.append(",");
                    }
                }
                stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "]");
            }
        }
        LOGGER.info(stringBuilder.toString());
    }

    @AfterReturning(value = "executeService()", returning = "returnValue")
    public void doAfterAdvice(JoinPoint joinPoint, Object returnValue) throws IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder("Integration result:[");
        stringBuilder.append(joinPoint.getSignature().getDeclaringTypeName()).append(".");
        stringBuilder.append(joinPoint.getSignature().getName());
        stringBuilder.append("],Parameters:");
        convertLog(stringBuilder, null, returnValue, true, "result");
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

    private String convertLog(StringBuilder stringBuilder, Parameter parameter, Object arg, Boolean isCustomClass, String fileName) {
        if (parameter != null) {
            if (arg == null || parameter.getType().equals(HttpServletRequest.class)
                    || parameter.getType().equals(HttpServletResponse.class)) {
                return "";
            }
        } else {
            if (arg == null || arg instanceof HttpServletRequest
                    || arg instanceof HttpServletResponse) {
                return "";
            }
        }
//                判断是否是基础类
//        parameter.getClass().getClassLoader()不好使
        if (arg.getClass().getClassLoader() != null) {
            if (!isCustomClass) {
                stringBuilder.append(parameter.getName()).append(":[");
            } else {
                stringBuilder.append("[");

            }
//            父类
            ArrayList<Field> fields = new ArrayList<Field>();
            getFields(fields, arg);
            for (Field declaredField : fields) {
                declaredField.setAccessible(true);
                Object target = null;
                try {
                    target = declaredField.get(arg);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return "";
                }
                String targetStr = "";
                if (isDisplayNull && target == null) {
                    continue;
                }
                if (target != null) {
                    if (target.getClass().getClassLoader() != null) {
                        targetStr = convertLog(new StringBuilder(), null, target, true, declaredField.getName());
                    } else {// list map
                        StringBuilder stringBuilder1 = new StringBuilder();
                        if (target instanceof List) {
                            List argList = (List) target;
                            stringBuilder1.append("[");
                            argList.forEach(argItem -> {
                                String s = convertLog(new StringBuilder(), null, argItem, true, "");
                                stringBuilder1.append(s);
                                stringBuilder1.append(",");

                            });
                            if (CollectionUtils.isEmpty(argList)) {
                                stringBuilder1.append("]");
                            } else {
                                stringBuilder1.replace(stringBuilder1.length() - 1, stringBuilder1.length(), "]");
                            }
                            targetStr=stringBuilder1.toString();
                        } else if (target instanceof Map) {
                            Map<Object, Object> argMap = (Map) target;
                            stringBuilder1.append("[");
                            argMap.entrySet().forEach(Item -> {
                                String s = convertLog(new StringBuilder(), null, Item.getValue(), true, Item.getKey().toString());
                                stringBuilder1.append(s);
                                stringBuilder1.append(",");

                            });
                            if (CollectionUtils.isEmpty(argMap)) {
                                stringBuilder1.append("]");
                            } else {
                                stringBuilder1.replace(stringBuilder1.length() - 1, stringBuilder1.length(), "]");
                            }
                            targetStr=stringBuilder1.toString();
                        } else {
                            targetStr = target.toString();
                        }
                    }
                } else {
                    targetStr = "null";
                }
                invokeRule(stringBuilder, declaredField.getName(), targetStr);
                stringBuilder.append(",");
            }


            stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "]");

        } else {
//                    基础类
            if (arg instanceof List) {
                List argList = (List) arg;
                stringBuilder.append("[");
                argList.forEach(argItem -> {
                    String s = convertLog(new StringBuilder(), null, argItem, true, "");
                    stringBuilder.append(s);
                    stringBuilder.append(",");

                });
                if (CollectionUtils.isEmpty(argList)) {
                    stringBuilder.append("]");
                } else {
                    stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "]");
                }
            } else if (arg instanceof Map) {
                Map<Object, Object> argMap = (Map) arg;
                stringBuilder.append("[");
                argMap.entrySet().forEach(Item -> {
                    String s = convertLog(new StringBuilder(), null, Item.getValue(), true, Item.getKey().toString());
                    stringBuilder.append(s);
                    stringBuilder.append(",");

                });
                if (CollectionUtils.isEmpty(argMap)) {
                    stringBuilder.append("]");
                } else {
                    stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "]");
                }
            } else {
                invokeRule(stringBuilder, fileName, arg);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 获取增强方法
     *
     * @param joinPoint
     * @return
     */
    private Method getDeclaredMethod(JoinPoint joinPoint) {
        ArrayList<Class> classes = new ArrayList<>();
        if (joinPoint.getArgs() != null) {
            for (Object o : joinPoint.getArgs()) {
                if (o instanceof HttpServletRequest) {
                    classes.add(HttpServletRequest.class);
                } else if (o instanceof HttpServletResponse) {
                    classes.add(HttpServletResponse.class);
                } else if (o instanceof List) {
                    classes.add(List.class);
                } else if (o instanceof Map) {
                    classes.add(Map.class);
                } else {
                    // FIXME: 2017/11/29  请求中的参数a如果为空，就无法获取a的类名了，导致全不能用了
                    classes.add(o.getClass());
                }
            }
        }

        try {
            Method declaredMethod = joinPoint.getTarget().getClass().getDeclaredMethod(joinPoint.getSignature().getName(), classes.toArray(new Class[classes.size()]));
            return declaredMethod;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取增强方法
     * 不用参数
     *
     * @param joinPoint
     * @return
     */
    private Method getDeclaredMethodForName(JoinPoint joinPoint) {
        try {
            Method[] methods = joinPoint.getTarget().getClass().getMethods();
            Method declaredMethod = null;
            if (methods != null) {
                for (Method method : methods) {
                    if (method.getName().equals(joinPoint.getSignature().getName())) {
                        declaredMethod = method;
                    }
                }
            }
            return declaredMethod;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 执行规则
     *
     * @param stringBuilder
     * @param parameterName
     * @param arg
     */
    private void invokeRule(StringBuilder stringBuilder, String parameterName, Object arg) {
        if (arg == null) {
            return;
        }
        stringBuilder.append(parameterName).append(":");
        if (ArrayUtils.contains(blackArray, parameterName)) {
            stringBuilder.append("***");
        } else if (ArrayUtils.contains(cardNoArray, parameterName)) {
            stringBuilder.append(HideDataUtil.hideCardNo(arg.toString()));
        } else if (ArrayUtils.contains(phoneNumArray, parameterName)) {
            stringBuilder.append(HideDataUtil.hidePhoneNo(arg.toString()));
        } else {
            stringBuilder.append(arg);
        }
    }

    /**
     * 递归获取arg对的所有属性
     *
     * @param fields
     * @param arg
     */
//    private void getFields(ArrayList<Field> fields, Class arg) {
//        if (!arg.getSuperclass().equals(Object.class)) {
//            getFields(fields,arg.getSuperclass());
//            List<Field> fields1 = Arrays.asList(arg.getDeclaredFields());
//            fields.addAll(fields1);
//        } else {
//            List<Field> fields1 = Arrays.asList(arg.getDeclaredFields());
//            fields.addAll(fields1);
//        }
//    }
    private void getFields(ArrayList<Field> fields, Object arg) {
        Class tempClass = arg.getClass();
        while (!tempClass.equals(Object.class)) {
            List<Field> fields1 = Arrays.asList(tempClass.getDeclaredFields());
            fields.addAll(fields1);
            tempClass = tempClass.getSuperclass();
        }
    }

}
