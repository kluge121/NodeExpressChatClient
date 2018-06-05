package com.example.baeminsu.nodechat.Model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by baeminsu on 2018. 6. 3..
 */

public class Modify extends RealmObject {

    private Date modify;

    public Date getModify() {
        return modify;
    }

    public void setModify(Date modify) {
        this.modify = modify;
    }
}
