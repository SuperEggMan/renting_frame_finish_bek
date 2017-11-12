package org.egg.Responsibility;

import java.io.Serializable;

/**
 * @author dataochen
 * @Description
 * @date: 2017/11/9 21:16
 */
public abstract class CommonContext implements Serializable{
    private static final long serialVersionUID = -562557872742956300L;
    //    默认true
    private boolean continueProcess = true;

    boolean isContinueProcess() {
        return continueProcess;
    }

    void setContinueProcess(boolean var1) {
        this.continueProcess = var1;
    }

    abstract Object getRequest();

    abstract Object getResponse();
}
