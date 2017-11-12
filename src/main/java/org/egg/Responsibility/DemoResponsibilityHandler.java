package org.egg.Responsibility;

/**
 * @author dataochen
 * @Description
 * @date: 2017/11/9 21:00
 */
public class DemoResponsibilityHandler extends Responsibility {

    @Override
    void handle(CommonContext commonContext) {
        System.out.println(commonContext.getRequest());
        System.out.println(commonContext.getResponse());
        System.out.println(commonContext.isContinueProcess());
        commonContext.setContinueProcess(true);
        System.out.println("DemoResponsibility execute");
    }
}
