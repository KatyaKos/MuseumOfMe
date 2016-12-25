package me.spbau.katyakos.android.museumofme;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import java.util.TreeMap;

class AllUsersInformation {
    private static TreeMap<Integer, UserInformation> usersListById = new TreeMap<>();
    private static TreeMap<String, UserInformation> usersListByNickname = new TreeMap<>();

    private static TreeMap<String, Pair<String, Integer>> credentials = new TreeMap<>();

    static TreeMap<String, UserInformation> getUsersListByNickname() {
        return usersListByNickname;
    }

    static Integer getIdByEmail(String email) {
        return getCredential(email).second;
    }

    static boolean containsByEmail(String email) {
        return credentials.containsKey(email);
    }

    static boolean checkEmailPasswordPair(String email, String password) {
        return credentials.containsKey(email) && getCredential(email).first.equals(password);
    }

    static UserInformation getUserById(Integer id) {
        return usersListById.get(id);
    }

    private static Pair<String, Integer> getCredential(String email) {
        return credentials.get(email);
    }

    private static SQLiteDatabase dataBase;

    static void addUser(String userNickname, String userEmail, String userPassword) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", userEmail);
        contentValues.put("password", userPassword);
        contentValues.put("nickname", userNickname);
        contentValues.put("photo", "user_photo_default");
        contentValues.put("header", "user_header_default");
        Integer userId = (int) dataBase.insertOrThrow("userInfo", null, contentValues);
        UserInformation user = new UserInformation(dataBase, userId, userNickname);
        credentials.put(userEmail, new Pair<>(userPassword, userId));
        usersListByNickname.put("@" + userNickname, user);
        usersListById.put(userId, user);
    }

    static void loadDataBase(SQLiteDatabase db) {
        dataBase = db;
        Cursor cursor = dataBase.query("userInfo", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Integer userId = getIntegerFromColumn(cursor, "id");
                String userNickname = getStringFromColumn(cursor, "nickname");
                credentials.put(getStringFromColumn(cursor, "email"), new Pair<>(getStringFromColumn(cursor, "password"), userId));
                UserInformation user = new UserInformation(dataBase, userId, userNickname);
                setUserFields(cursor, user);
                usersListById.put(userId, user);
                usersListByNickname.put(userNickname, user);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private static void setUserFields(Cursor cursor, UserInformation user) {
        user.setUserName(getStringFromColumn(cursor, "name"));
        user.setUserPhoto(getStringFromColumn(cursor, "photo"));
        user.setUserHeader(getStringFromColumn(cursor, "header"));
        user.setUserBirth(getStringFromColumn(cursor, "birth"));
        user.setUserBio(getStringFromColumn(cursor, "bio"));
        user.setUserAbout(getStringFromColumn(cursor, "about"));

        readTrips(user);
    }

    private static void readTrips(UserInformation user) {
        Cursor cursorTrips = dataBase.query("userTripsGroups", null, null, null, null, null, null);
        if (cursorTrips.moveToFirst()) {
            do {
                Integer groupId = getIntegerFromColumn(cursorTrips, "groupId");
                String groupName = getStringFromColumn(cursorTrips, "groupName");
                user.loadGroup(groupId, groupName);
            } while (cursorTrips.moveToNext());
        }
        cursorTrips.close();
        cursorTrips = dataBase.query("userTripsPlaces", null, null, null, null, null, null);
        if (cursorTrips.moveToFirst()) {
            do {
                Integer groupId = getIntegerFromColumn(cursorTrips, "groupId");
                String placeName = getStringFromColumn(cursorTrips, "placeName");
                Integer placeId = getIntegerFromColumn(cursorTrips, "placeId");
                user.loadPlace(groupId, placeId, placeName);
            } while (cursorTrips.moveToNext());
        }
        cursorTrips.close();
    }

    private static Integer getIntegerFromColumn(Cursor cursor, String columnName) {
        return cursor.getInt(getColumnId(cursor, columnName));
    }

    private static String getStringFromColumn(Cursor cursor, String columnName) {
        return cursor.getString(getColumnId(cursor, columnName));
    }

    private static int getColumnId(Cursor cursor, String columnName) {
        return cursor.getColumnIndex(columnName);
    }
}
