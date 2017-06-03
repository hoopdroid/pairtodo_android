package com.pairtodobeta.db;

import com.pairtodobeta.ui.main.chat.models.DefaultUser;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class MessageDB extends RealmObject {
    private String isRead;
    private String fromId;
    private String toId;
    @PrimaryKey
    private String id;
    private String message;
    private String createDate;
    private String jobId;
    public DefaultUser userMessage;

    public MessageDB() {
    }

    public MessageDB(String isRead, String fromId, String toId, String id, String message,
                     String createDate, String jobId, DefaultUser user) {
        this.isRead = isRead;
        this.fromId = fromId;
        this.toId = toId;
        this.id = id;
        this.message = message;
        this.createDate = createDate;
        this.userMessage = user;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
