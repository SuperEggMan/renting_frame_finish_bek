package org.egg.response;

import java.io.Serializable;

/**
 * @author dataochen
 * @Description
 * @date: 2017/11/7 15:24
 */
public class CommonSingleResult<T> extends BaseResult implements Serializable {
    private static final long serialVersionUID = 1753904427357581087L;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
