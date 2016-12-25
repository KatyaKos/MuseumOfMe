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

    static void addUser(SQLiteDatabase dataBase, String userNickname, String userEmail, String userPassword) {
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

    private static Cursor cursor;

    static void loadDataBase(SQLiteDatabase dataBase) {
        cursor = dataBase.query("userInfo", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Integer userId = getIntegerFromColumn("id");
                String userNickname = getStringFromColumn("nickname");
                credentials.put(getStringFromColumn("email"), new Pair<>(getStringFromColumn("password"), userId));
                UserInformation user = new UserInformation(dataBase, userId, userNickname);
                setUserFields(user);
                usersListById.put(userId, user);
                usersListByNickname.put(userNickname, user);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private static void setUserFields(UserInformation user) {
        user.setUserName(getStringFromColumn("name"));
        user.setUserPhoto(getStringFromColumn("photo"));
        user.setUserHeader(getStringFromColumn("header"));
        user.setUserBirth(getStringFromColumn("birth"));
        user.setUserBio(getStringFromColumn("bio"));
        user.setUserAbout(getStringFromColumn("about"));
    }

    private static Integer getIntegerFromColumn(String columnName) {
        return cursor.getInt(getColumnId(columnName));
    }

    private static String getStringFromColumn(String columnName) {
        return cursor.getString(getColumnId(columnName));
    }

    private static int getColumnId(String columnName) {
        return cursor.getColumnIndex(columnName);
    }
}
