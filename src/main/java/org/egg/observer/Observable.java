package org.egg.observer;

/**
 * @author dataochen
 * @Description 被观察者抽象类
 * @date: 2017/11/8 21:58
 */
public interface Observable {
    /**
     * 新增观察者
     * @param observer
     */
    void addObserver(Observer observer);
    /**
     * 删除观察者
     * @param observer
     */
    void removeObserver(Observer observer);
    /**
     * 通知观察者
     * @param obj
     */
    void notifyObserver(Object obj);
    /**
     * 通知观察者
     */
    void notifyObserver();
}
