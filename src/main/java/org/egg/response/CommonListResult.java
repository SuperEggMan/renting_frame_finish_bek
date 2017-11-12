package org.egg.response;

import java.io.Serializable;
import java.util.List;

/**
 * @author dataochen
 * @Description
 * @date: 2017/11/7 15:28
 */
public class CommonListResult<T> extends BaseResult implements Serializable {
    private static final long serialVersionUID = -5476525617553733788L;
    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
