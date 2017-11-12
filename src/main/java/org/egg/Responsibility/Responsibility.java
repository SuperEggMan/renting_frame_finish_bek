package org.egg.Responsibility;

/**
 * @author dataochen
 * @Description
 * @date: 2017/11/9 20:50
 */
public abstract class Responsibility {
    private Responsibility nextHandler;

    public void setNextHandler(Responsibility nextHandler) {
        this.nextHandler = nextHandler;
    }

    public Responsibility getNextHandler() {
        return nextHandler;
    }

    abstract void handle(CommonContext commonContext);

    void handleExe(CommonContext commonContext) {
        this.handle(commonContext);
        if (commonContext.isContinueProcess()) {
        this.nextHandler.handleExe(commonContext);
        }
    }
}
