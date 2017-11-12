package org.egg.observer.observers;

import org.egg.observer.Observer;
import org.egg.observer.subjects.DemoObservable;

/**
 * @author dataochen
 * @Description
 * @date: 2017/11/8 21:57
 */
public class DemoTwoObserver implements Observer {
    @Override
    public void update() {

    }

    @Override
    public void update(DemoObservable demoObservable, Object obj) {
        System.out.println("demoTwoObserver execute");
    }
}
