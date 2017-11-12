package org.egg.observer;

import org.egg.observer.subjects.DemoObservable;

/**
 * @author dataochen
 * @Description 观察者模式
 * @date: 2017/11/8 21:54
 */
public interface Observer {
    /**
     *观察者执行方法
     */
    void update(DemoObservable demoObservable, Object obj);

    void update();
}
