package org.egg.observer;

import org.egg.observer.observers.DemoObserver;
import org.egg.observer.observers.DemoTwoObserver;
import org.egg.observer.subjects.DemoObservable;

/**
 * @author dataochen
 * @Description 观察者模式测试
 * @date: 2017/11/8 22:26
 */
public class TestObserver extends DemoObservable{
    public void test() {
        DemoObserver demoObserver = new DemoObserver();
        DemoTwoObserver demoTwoObserver = new DemoTwoObserver();
        super.notifyThing(demoObserver,demoTwoObserver);
    }

}
