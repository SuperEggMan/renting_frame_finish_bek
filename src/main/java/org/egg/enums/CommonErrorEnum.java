package org.egg.enums;

/**
 * @author dataochen
 * @Description
 * RTG1100001 renting-参数-参数为空【RTG-11-00001】
 * RTG2200001 renting-RPC-rpc调用失败【RTG-22-00001】
 * RTG3300001 renting-通用-连接超限【RTG-33-00001】
 * RTG9900001 renting-系统-系统错误【RTG-99-00001】
 * @date: 2017/11/7 15:32
 */
public enum CommonErrorEnum {
    NULL("", ""),

    //成功
    SUCCESS("000000", "success"),
    /**
     * 入参为空
     */
    PARAM_NULL("PRO1100001", "param is null"),
    /**
     * 入参错误
     */
    PARAM_ERROR("PRO1100002", "param is error"),

    /**
     * 系统异常
     */
    SYSTEM_EXCEPTION("PRO9900001", "system error"),
    ;
    /**
     * 代码
     */
    private String code = null;
    /**
     * 描述
     */
    private String description = null;

    /**
     * 构造方法
     *
     * @param code
     * @param description
     */
    CommonErrorEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 根据业务码获得业务类型
     *
     * @param code 业务码
     * @return 业务类型
     */
    public static CommonErrorEnum getEnumByCode(String code) {
        CommonErrorEnum[] values = CommonErrorEnum.values();
        for (CommonErrorEnum operate : values) {
            if (operate.getCode().equals(code)) {
                return operate;
            }
        }
        return NULL;
    }
}
