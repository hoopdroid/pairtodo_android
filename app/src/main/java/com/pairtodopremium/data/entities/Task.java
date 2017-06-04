package com.pairtodopremium.data.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ilyasavin on 2/13/17.
 */

public class Task implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("author_id")
    @Expose
    private String authorId;
    @SerializedName("executor_id")
    @Expose
    private String executorId;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("last_modify")
    @Expose
    private String lastModify;
    @SerializedName("term_date")
    @Expose
    private String termDate;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("list")
    @Expose
    private String list;
    @SerializedName("is_finish")
    @Expose
    private String isFinish;
    @SerializedName("is_important")
    @Expose
    private String isImportant;
    @SerializedName("image")
    @Expose
    private String image;


    public Task(String title, String term_date, String description,
                String list, String image, String important, String executor) {
        this.title = title;
        this.termDate = term_date;
        this.description = description;
        this.list = list;
        this.image = image;
        this.isImportant = important;
        this.executorId = executor;
    }

    public Task(String id, String authorId, String executorId,
                String createDate, String lastModify, String termDate,
                String title, String description, String list, String isFinish,
                String isImportant, String image) {
        this.id = id;
        this.authorId = authorId;
        this.executorId = executorId;
        this.createDate = createDate;
        this.lastModify = lastModify;
        this.termDate = termDate;
        this.title = title;
        this.description = description;
        this.list = list;
        this.isFinish = isFinish;
        this.isImportant = isImportant;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastModify() {
        return lastModify;
    }

    public void setLastModify(String lastModify) {
        this.lastModify = lastModify;
    }

    public String getTermDate() {
        return termDate;
    }

    public void setTermDate(String termDate) {
        this.termDate = termDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(String isFinish) {
        this.isFinish = isFinish;
    }

    public String getIsImportant() {
        return isImportant;
    }

    public void setIsImportant(String isImportant) {
        this.isImportant = isImportant;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.authorId);
        dest.writeString(this.executorId);
        dest.writeString(this.createDate);
        dest.writeString(this.lastModify);
        dest.writeString(this.termDate);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.list);
        dest.writeString(this.isFinish);
        dest.writeString(this.isImportant);
        dest.writeString(this.image);
    }

    protected Task(Parcel in) {
        this.id = in.readString();
        this.authorId = in.readString();
        this.executorId = in.readString();
        this.createDate = in.readString();
        this.lastModify = in.readString();
        this.termDate = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.list = in.readString();
        this.isFinish = in.readString();
        this.isImportant = in.readString();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
