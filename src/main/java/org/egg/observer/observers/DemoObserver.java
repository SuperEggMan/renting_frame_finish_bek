package org.egg.observer.observers;

import org.egg.observer.Observer;
import org.egg.observer.subjects.DemoObservable;

/**
 * @author dataochen
 * @Description
 * @date: 2017/11/8 21:55
 */
public class DemoObserver implements Observer {
    @Override
    public void update(DemoObservable demoObservable, Object obj) {
        System.out.println("demoObserver execute");
    }

    @Override
    public void update() {

    }
}
