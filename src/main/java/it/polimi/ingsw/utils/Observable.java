package it.polimi.ingsw.utils;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {

    private final List<Observer<T>> observers = new ArrayList<>();

    public void register(Observer<T> observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void deregister(Observer<T> observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    public void notify(T message){
        synchronized (observers) {
            for(Observer<T> observer : observers){
                observer.update(message);
            }
        }
    }

}