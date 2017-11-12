package org.egg.utils;

import org.egg.enums.CommonErrorEnum;
import org.egg.exception.CommonException;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author dataochen
 * @Description 通过Validator注解验证内容是否合法
 * @date: 2017/11/7 16:14
 */
public class ValidationUtil {
    /**
     * 使用hibernate的注解来进行验证
     *
     */
    private static Validator validator = Validation
            .byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    /**
     * 功能描述: <br>
     * 〈注解验证参数〉
     *
     * @param obj
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static <T> void validate(T obj) throws CommonException {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        // 抛出检验异常
        if (constraintViolations.size() > 0) {
            throw new CommonException(CommonErrorEnum.PARAM_NULL.getCode(), String.format("parameter validate failed:%s", constraintViolations.iterator().next().getMessage()));
        }
    }
}
