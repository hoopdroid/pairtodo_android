package com.pairtodobeta.data.settings;


import com.pairtodobeta.data.entities.SettingsObject;
import com.pairtodobeta.data.response.userData.UserInfo;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Contains methods for realm database
 */
public class SettingsRepository {

    public static boolean checkIfExists(Realm realm, int id) {

        RealmQuery<SettingsObject> query = realm.where(SettingsObject.class)
                .equalTo("id", id);

        return query.count() == 0 ? false : true;
    }

    public static boolean addUserSettings(Realm realm, int userId, String token, String userPhone) {

        realm.beginTransaction();
        SettingsObject postRealmModel = new SettingsObject(
                userId, token, userPhone
        );

        realm.copyToRealm(postRealmModel);
        realm.commitTransaction();
        return true;
    }

    public static boolean addUserInfo(Realm realm, UserInfo userInfo) {

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(userInfo);
        realm.commitTransaction();
        return true;
    }

    public static UserInfo getUserInfo(Realm realm) {
        RealmResults<UserInfo> realmQuery = realm.where(UserInfo.class)
                .findAll();
        if (realmQuery.size() > 0)
            return realmQuery.get(0);
        else return null;
    }

    public static boolean addUserSettings(Realm realm, int userId, String token) {

        realm.beginTransaction();
        SettingsObject postRealmModel = new SettingsObject(
                userId, token
        );

        realm.insertOrUpdate(postRealmModel);
        realm.commitTransaction();
        return true;
    }


    public static SettingsObject getUserSettings(Realm realm) {
        RealmResults<SettingsObject> realmQuery = realm.where(SettingsObject.class)
                .findAll();
        if (realmQuery.size() > 0)
            return realmQuery.get(0);
        else return new SettingsObject(123, "");
    }
}