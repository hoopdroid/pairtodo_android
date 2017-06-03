package com.pairtodobeta.ui.main.chat.models;

import com.stfalcon.chatkit.commons.models.IUser;

import io.realm.RealmObject;


public class DefaultUser extends RealmObject implements IUser {
    private String id;
    private String name;
    private String avatar;
    private boolean online;

    public DefaultUser() {
    }

    public DefaultUser(String id, String name, String avatar, boolean online) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.online = online;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isOnline() {
        return online;
    }
}
