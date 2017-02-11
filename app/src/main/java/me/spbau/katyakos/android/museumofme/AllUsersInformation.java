package me.spbau.katyakos.android.museumofme;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import retrofit2.Call;

class AllUsersInformation {
    private static TreeMap<String, UserInformation> usersListById = new TreeMap<>();
    private static TreeMap<String, UserInformation> usersListByNickname = new TreeMap<>();

    private static TreeMap<String, Pair<String, String>> credentials = new TreeMap<>();
    //Server
    private static UserDataAPI userDataAPI = RetrofitInitializer.getInstance().getAPI();
    private static List<?> listRetrofit = new ArrayList<>();
    private static UserInfo userRetrofit;
    //DataBase
    private static SQLiteDatabase dataBase;

    static TreeMap<String, UserInformation> getUsersListByNickname() {
        return usersListByNickname;
    }

    static String getIdByEmail(String email) {
        return getCredential(email).second;
    }

    static boolean containsByEmail(String email) {
        return credentials.containsKey(email);
    }

    static boolean checkEmailPasswordPair(String email, String password) {
        return credentials.containsKey(email) && getCredential(email).first.equals(password);
    }

    static UserInformation getUserById(String id) {
        return usersListById.get(id);
    }

    private static Pair<String, String> getCredential(String email) {
        return credentials.get(email);
    }

    private static <E> void downloadAllInfo(final Call<List<E>> call) {
        try {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        listRetrofit = call.execute().body();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void downloadAuthInfo() {
        downloadAllInfo(userDataAPI.getUsersAuth());
        List<UsersAuth> usersAuthList = (List<UsersAuth>) listRetrofit;
        for (UsersAuth user : usersAuthList) {
            credentials.put(user.email, new Pair<>(user.password, user.id));
        }
    }

    static void downloadBasicInfo() {
        downloadAllInfo(userDataAPI.getUsersBasic());
        List<UsersBasicInfo> usersBasicList = (List<UsersBasicInfo>) listRetrofit;
        for (UsersBasicInfo userBasic : usersBasicList) {
            UserInformation user = new UserInformation(userBasic.id, userBasic.nickname, userBasic.name);
            usersListById.put(userBasic.id, user);
            usersListByNickname.put(userBasic.nickname, user);
        }
    }

    static void downloadUserById(String id) {
        try {
            final Call<UserInfo> call = userDataAPI.getUser(id);
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        userRetrofit = call.execute().body();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UserInformation user = new UserInformation(userRetrofit);
        usersListById.put(userRetrofit.id, user);
        usersListByNickname.put(userRetrofit.nickname, user);
    }

    static void addUser(String userNickname, String userEmail, String userPassword) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", userEmail);
        contentValues.put("password", userPassword);
        contentValues.put("nickname", userNickname);
        contentValues.put("photo", "user_photo_default");
        contentValues.put("header", "user_header_default");
        String userId = String.valueOf(dataBase.insertOrThrow("userInfo", null, contentValues));


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
                String userId = getStringFromColumn(cursor, "id");
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
        readNotes(user);
        readInterests(user);
    }

    private static void readTrips(UserInformation user) {
        Cursor cursorTrips = dataBase.query("userTripsGroups", null, null, null, null, null, null);
        if (cursorTrips.moveToFirst()) {
            do {
                Integer groupId = getIntegerFromColumn(cursorTrips, "id");
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

    private static void readNotes(UserInformation user) {
        Cursor cursorNotes = dataBase.query("userNotes", null, null, null, null, null, null);
        if (cursorNotes.moveToFirst()) {
            do {
                TreeMap<String, String> note = new TreeMap<>();
                Integer id = getIntegerFromColumn(cursorNotes, "id");
                note.put("name", getStringFromColumn(cursorNotes, "name"));
                note.put("date", getStringFromColumn(cursorNotes, "date"));
                note.put("text", getStringFromColumn(cursorNotes, "content"));
                note.put("tags", getStringFromColumn(cursorNotes, "tags"));
                user.loadNote(id, note);
            } while (cursorNotes.moveToNext());
        }
        cursorNotes.close();
    }

    private static ArrayList<String> deserializeBytesArray(byte[] bytes) {
        ArrayList<String> result = new ArrayList<>();
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInput in = new ObjectInputStream(bis);
            Object object = in.readObject();
            result = (ArrayList<String>) object;
            in.close();
        } catch (Exception e) {
            Log.d("myLogs", "deserialization error");
        }
        return result;
    }

    private static void readInterests(UserInformation user) {
        Cursor cursorInterests = dataBase.query("userInterests", null, null, null, null, null, null);
        if (cursorInterests.moveToFirst()) {
            do {
                Integer id = getIntegerFromColumn(cursorInterests, "id");
                Float rating = cursorInterests.getFloat(getColumnId(cursorInterests, "rating"));
                String type = getStringFromColumn(cursorInterests, "type");

                TreeMap<String, String> content = new TreeMap<>();
                content.put("name", getStringFromColumn(cursorInterests, "name"));
                content.put("authorName", getStringFromColumn(cursorInterests, "authorName"));
                content.put("photo", getStringFromColumn(cursorInterests, "photo"));
                content.put("review", getStringFromColumn(cursorInterests, "review"));

                byte[] bytes = cursorInterests.getBlob(getColumnId(cursorInterests, "characters"));
                ArrayList<String> characters = deserializeBytesArray(bytes);

                if (type.equals("movie")) {
                    user.loadMovie(id, content, rating, characters);
                } else {
                    user.loadBook(id, content, rating, characters);
                }
            } while (cursorInterests.moveToNext());
        }
        cursorInterests.close();
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

    static class UsersAuth {
        private String id;
        private String email;
        private String password;
    }

    static class UsersBasicInfo {
        private String id;
        private String name;
        private String nickname;
    }

    static class UserInfo {
        String id;
        String nickname;
        String photo;
        String header;
        String bio;
        String name;
        String birth;
        String about;
        HashMap<String, TreeMap<String, String>> notes;
        HashMap<String, HashMap<String, ArrayList<String>>> trips;
        HashMap<String, HashMap<String, ArrayList<String>>> movies;
        HashMap<String, HashMap<String, ArrayList<String>>> books;
        ArrayList<String> friends;
    }
}
