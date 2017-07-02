package com.pairtodopremium.db;

import com.pairtodopremium.data.response.chat.Message;
import io.realm.Realm;
import java.util.ArrayList;
import java.util.List;

public class PairToDoCacheDao {
  public void addMessagesToCache(final List<Message> messages) {
    final List<MessageDB> messagesDB = new ArrayList<>();
    for (int i = 0; i < messages.size(); i++) {
      messagesDB.add(
          new MessageDB(messages.get(i).isRead(), messages.get(i).fromId(), messages.get(i).toId(),
              messages.get(i).id(), messages.get(i).message(), messages.get(i).createDate(),
              messages.get(i).jobId(), messages.get(i).getUserMessage()));
    }
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransaction(new Realm.Transaction() {
      @Override public void execute(Realm realm) {
        realm.copyToRealmOrUpdate(messagesDB);
      }
    });
    realm.close();
  }

  public List<Message> getMessagesArchive() {
    Realm realm = Realm.getDefaultInstance();
    List<MessageDB> messageDBList =
        realm.copyFromRealm(realm.where(MessageDB.class).findAll().sort("createDate"));
    realm.close();
    List<Message> messages = new ArrayList<>();
    for (int i = 0; i < messageDBList.size(); i++) {
      messages.add(
          Message.create(messageDBList.get(i).getIsRead(), messageDBList.get(i).getFromId(),
              messageDBList.get(i).getToId(), messageDBList.get(i).getJobId(),
              messageDBList.get(i).getId(), messageDBList.get(i).getMessage(),
              messageDBList.get(i).getCreateDate()));
    }
    return messages;
  }

  public void clearDatabase() {
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransaction(new Realm.Transaction() {
      @Override public void execute(Realm realm) {
        realm.delete(MessageDB.class);
      }
    });
    realm.close();
  }
}
