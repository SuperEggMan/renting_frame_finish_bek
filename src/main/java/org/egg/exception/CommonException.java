package org.egg.exception;

import org.egg.enums.CommonErrorEnum;
import org.egg.response.BaseResult;

/**
 * @author dataochen
 * @Description
 * @date: 2017/11/7 15:38
 */
public class CommonException extends RuntimeException {
    private String errorMessage;
    private String errorCode;

    public CommonException(CommonErrorEnum commonErrorEnum) {
        super(commonErrorEnum.getDescription());
        this.errorMessage = commonErrorEnum.getDescription();
        this.errorCode = commonErrorEnum.getCode();
    }

    /**
     * 显示异常字段的构建方法
     * @param commonErrorEnum
     * @param fileName
     */
    public CommonException(CommonErrorEnum commonErrorEnum, String fileName,String fileValue) {
        super(commonErrorEnum.getDescription());
        String s = new StringBuilder(commonErrorEnum.getDescription()).append("===fileName is ").append(fileName)
                .append("===value is ").append(fileValue).toString();
        this.errorMessage = s;
        this.errorCode = commonErrorEnum.getCode();
    }

    public CommonException(BaseResult baseResult) {
        super(baseResult.getRespDesc());
        this.errorMessage = baseResult.getRespDesc();
        this.errorCode = baseResult.getRespCode();
    }

    /**
     * Constructs a new runtime exception with <code>null</code> as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     *
     * @param errorCode
     * @param message
     */
    public CommonException(String errorCode, String message) {
        super(message);
        this.errorMessage = message;
        this.errorCode = errorCode;
    }
    /**
     * Constructs a new runtime exception with <code>null</code> as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     *
     * @param message
     */
    public CommonException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
