package org.egg.template;

import org.egg.enums.CommonErrorEnum;
import org.egg.exception.CommonException;
import org.egg.response.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionOperations;

/**
 * @author dataochen
 * @Description
 * @date: 2017/11/7 15:11
 */
@Component
public class ServiceTemplate {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTemplate.class);
    /**
     * 事务模板
     */
    @Autowired
    private TransactionOperations transactionTemplate;

    /**
     * 模板处理方法,对业务处理统一封装异常和返回结果
     *
     * @param result
     * @param templateCallBack
     */
    public void process(BaseResult result, TemplateCallBack templateCallBack) {
        try {
            templateCallBack.doCheck();
            templateCallBack.doAction();
            result.setSuccess(true);
            result.setRespCode(CommonErrorEnum.SUCCESS.getCode());
            result.setRespDesc(CommonErrorEnum.SUCCESS.getDescription());
        } catch (CommonException ce) {
            LOGGER.error("process throw commonException,e={},desc={}", ce.getErrorCode(), ce.getErrorMessage());
            result.setRespCode(ce.getErrorCode());
            result.setRespDesc(ce.getErrorMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            LOGGER.error("system error!", e);
            result.setRespCode(CommonErrorEnum.SYSTEM_EXCEPTION.getCode());
            result.setRespDesc(CommonErrorEnum.SYSTEM_EXCEPTION.getDescription());
            result.setSuccess(false);
        }
    }

    /**
     * 带事务模板处理方法
     *
     * @param result
     * @param templateCallBack
     */
    public void processTX(BaseResult result,  TemplateCallBack templateCallBack) {
        try {
            templateCallBack.doCheck();
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {

                    try {
                        templateCallBack.doAction();
                    } catch (Exception e) {
                        LOGGER.error("org.egg.template.ServiceTemplate.processTX e={}",e);
                        if (e instanceof CommonException) {
                            throw (CommonException)e;
                        }
                        throw new CommonException(CommonErrorEnum.SYSTEM_EXCEPTION.getCode(),CommonErrorEnum.SYSTEM_EXCEPTION.getDescription());
                    }
                }
            });
            result.setRespCode(CommonErrorEnum.SUCCESS.getCode());
            result.setRespDesc(CommonErrorEnum.SUCCESS.getDescription());
            result.setSuccess(true);
        } catch (CommonException ce) {
            LOGGER.error("processTX throw commonException,e={},desc={}", ce.getErrorCode(), ce.getErrorMessage());
            result.setRespCode(ce.getErrorCode());
            result.setRespDesc(ce.getErrorMessage());
            result.setSuccess(false);
        }
        catch (Exception e)
        {
            LOGGER.error("processTX throw Exception,e={}", e);
            result.setRespCode(CommonErrorEnum.SYSTEM_EXCEPTION.getCode());
            result.setRespDesc(CommonErrorEnum.SYSTEM_EXCEPTION.getDescription());
            result.setSuccess(false);
        }
    }
}
