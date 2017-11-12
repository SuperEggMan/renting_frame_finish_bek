package org.egg.observer.subjects;

import org.egg.observer.Observable;
import org.egg.observer.Observer;

import java.util.Vector;

/**
 * @author dataochen
 * @Description
 * @date: 2017/11/8 22:02
 */
public abstract class DemoObservable implements Observable {
//    vector 保证数据及时可见性
    private Vector<Observer> obs;

    public DemoObservable() {
        obs = new Vector<>();
    }

    @Override
    public synchronized void addObserver(Observer observer) {
        if (observer == null) {
            throw new NullPointerException();
        }
        obs.add(observer);
    }

    @Override
    public synchronized void removeObserver(Observer observer) {
        obs.removeElement(observer);
    }

    @Override
    public synchronized void notifyObserver(Object obj) {
        System.out.println("===start notify Observer====");
        obs.forEach(observer->{observer.update(this,obj);});
    }

    @Override
    public void notifyObserver() {
        obs.forEach(observer->{observer.update();});
    }

    /**
     * 调用此方法通知
     * @param observers
     */
    public void notifyThing(Observer... observers) {
        for (Observer observer : observers) {
            this.addObserver(observer);
        }
//        通知
        this.notifyObserver();
    }

//    abstract void sendNotify();

}
