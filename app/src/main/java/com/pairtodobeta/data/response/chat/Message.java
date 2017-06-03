package com.pairtodobeta.data.response.chat;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.pairtodobeta.ui.main.chat.fixtures.FixturesData;
import com.pairtodobeta.ui.main.chat.models.DefaultUser;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

@AutoValue
public abstract class Message extends FixturesData implements IMessage {

	@SerializedName("is_read")
	public abstract String isRead();

	@SerializedName("from_id")
	public abstract String fromId();

	@SerializedName("to_id")
	public abstract String toId();

	@SerializedName("job_id")
	@Nullable
	public abstract String jobId();

	@SerializedName("id")
	public abstract String id();

	@SerializedName("message")
	public abstract String message();

	@SerializedName("create_date")
	public abstract String createDate();

	public DefaultUser userMessage;

	public DefaultUser getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(DefaultUser userMessage) {
		this.userMessage = userMessage;
	}

	public static TypeAdapter<Message> typeAdapter(Gson gson) {
		return new AutoValue_Message.GsonTypeAdapter(gson);
	}

	public static Message create(String isRead, String fromId, String toId, String jobId, String id,
								 String message, String createDate){
		return new AutoValue_Message(isRead, fromId, toId, jobId, id, message, createDate);
	}

	public abstract Message withMessage(String message);

	@Override
	public Date getCreatedAt() {
		long time = System.currentTimeMillis();
		return createDate().equals("") ? new Date(time): new Date(Long.parseLong(createDate())*1000);
	}

	@Override
	public String getId() {
		return fromId();
	}

	@Override
	public IUser getUser() {
		return userMessage;
	}

	@Override
	public String getText() {
		return message();
	}

	public String getStatus() {
		return "Sent";
	}
}