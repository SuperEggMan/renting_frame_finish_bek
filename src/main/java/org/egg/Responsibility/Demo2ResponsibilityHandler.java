package org.egg.Responsibility;

/**
 * @author dataochen
 * @Description
 * @date: 2017/11/9 21:02
 */
public class Demo2ResponsibilityHandler extends Responsibility {


    @Override
    void handle(CommonContext commonContext) {
        System.out.println(commonContext.getRequest());
        System.out.println(commonContext.getResponse());
        System.out.println(commonContext.isContinueProcess());
        commonContext.setContinueProcess(false);
        System.out.println("Demo2Responsibility execute");
    }
}
