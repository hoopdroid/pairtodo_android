package com.pairtodopremium.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class TaskDB extends RealmObject {
	private String isImportant;
	private String image;
	private String lastModify;
	private String termDate;
	private String description;
	private String isFinish;
	@PrimaryKey
	private String id;
	private String authorId;
	private String createDate;
	private String title;
	private String list;
	private String executorId;

	public TaskDB() {
	}

	public TaskDB(String isImportant, String image, String lastModify,
				  String termDate, String description, String isFinish,
				  String id, String authorId, String createDate, String title,
				  String list, String executorId) {
		this.isImportant = isImportant;
		this.image = image;
		this.lastModify = lastModify;
		this.termDate = termDate;
		this.description = description;
		this.isFinish = isFinish;
		this.id = id;
		this.authorId = authorId;
		this.createDate = createDate;
		this.title = title;
		this.list = list;
		this.executorId = executorId;
	}

	public void setIsImportant(String isImportant){
		this.isImportant = isImportant;
	}

	public String getIsImportant(){
		return isImportant;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setLastModify(String lastModify){
		this.lastModify = lastModify;
	}

	public String getLastModify(){
		return lastModify;
	}

	public void setTermDate(String termDate){
		this.termDate = termDate;
	}

	public String getTermDate(){
		return termDate;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setIsFinish(String isFinish){
		this.isFinish = isFinish;
	}

	public String getIsFinish(){
		return isFinish;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setAuthorId(String authorId){
		this.authorId = authorId;
	}

	public String getAuthorId(){
		return authorId;
	}

	public void setCreateDate(String createDate){
		this.createDate = createDate;
	}

	public String getCreateDate(){
		return createDate;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setList(String list){
		this.list = list;
	}

	public String getList(){
		return list;
	}

	public void setExecutorId(String executorId){
		this.executorId = executorId;
	}

	public String getExecutorId(){
		return executorId;
	}
}
