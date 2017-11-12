package org.egg.utils;

import org.egg.enums.CommonErrorEnum;
import org.egg.exception.CommonException;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dataochen
 * @Description bean工具类
 * @date: 2017/11/7 17:30
 */
public class BeanUtil {

    /**
     * 拷贝实体，source,target不允许为空
     *
     * @param source
     * @param target
     */
    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }

    /**
     * 拷贝实体集合，sourceList，targetList不允许为空
     *
     * @param sourceList
     * @param targetList
     */
    public static void copyPropertiesList(List sourceList, List targetList) {
        if (CollectionUtils.isEmpty(sourceList) || CollectionUtils.isEmpty(targetList)) {
            throw new CommonException(CommonErrorEnum.PARAM_ERROR);
        }
        sourceList.forEach(items -> {
            Object target = new Object();
            BeanUtils.copyProperties(items, target);
            targetList.add(target);
        });
    }


    /**
     * Map --> Bean 2: 利用org.apache.commons.beanutils 工具类实现 Map --> Bean
     *
     * @param map
     * @param obj
     */
    public static void transMap2Bean2(Map<String, Object> map, Object obj) throws InvocationTargetException, IllegalAccessException {
        if (map == null || obj == null) {
            return;
        }
        org.apache.commons.beanutils.BeanUtils.populate(obj, map);
    }

    /**
     * Map --> Bean 1: 利用Introspector,PropertyDescriptor实现 Map --> Bean
     *
     * @param map
     * @param obj
     */
    public static void transMap2Bean(Map<String, Object> map, Object obj) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (map.containsKey(key)) {
                Object value = map.get(key);
                // 得到property对应的setter方法
                Method setter = property.getWriteMethod();
                setter.invoke(obj, value);
            }
        }
    }

    /**
     * Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
     *
     * @param obj
     */
    public static Map<String, Object> transBean2Map(Object obj) throws IntrospectionException, InvocationTargetException, IllegalAccessException {

        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();

            // 过滤class属性
            if (!key.equals("class")) {
                // 得到property对应的getter方法
                Method getter = property.getReadMethod();
                Object value = getter.invoke(obj);

                map.put(key, value);
            }

        }
        return map;
    }
}
