package com.example.baeminsu.nodechat.Observer;

/**
 * Created by baeminsu on 2018. 6. 10..
 */

public interface Observable {

    void attach(Observer o);

    void detach(Observer o);

    void notifyObservers();

}
