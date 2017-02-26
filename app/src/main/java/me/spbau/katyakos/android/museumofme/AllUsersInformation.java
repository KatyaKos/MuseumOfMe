package me.spbau.katyakos.android.museumofme;

import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import java.io.IOException;
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
    private static Object userRetrofit;
    //DataBase
    private static SQLiteDatabase dataBase;

    static TreeMap<String, UserInformation> getUsersListByNickname() {
        return usersListByNickname;
    }

    static String getIdByEmail(String email) {
        return getCredential(email).second;
    }

    static String getIdByNickname(String nickname) {
        return usersListByNickname.get(nickname).getUserId();
    }

    static boolean containsByEmail(String email) {
        return credentials.containsKey(email);
    }

    static boolean containsByNickname(String nickname) {
        return usersListByNickname.containsKey(nickname);
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

    private static <E> void callForMultipleObjects(final Call<List<E>> call) {
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

    private static <E> void callForSingleObject(final Call<E> call) {
        try {
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
    }

    static void downloadAuthInfo() {
        callForMultipleObjects(userDataAPI.getUsersAuth());
        List<UsersAuth> usersAuthList = (List<UsersAuth>) listRetrofit;
        for (UsersAuth user : usersAuthList) {
            credentials.put(user.email, new Pair<>(user.password, user.id));
        }
    }

    static void downloadBasicInfo() {
        callForMultipleObjects(userDataAPI.getUsersBasic());
        List<UserInfo> usersBasicList = (List<UserInfo>) listRetrofit;
        for (UserInfo userBasic : usersBasicList) {
            UserInformation user = new UserInformation(userBasic.id, userBasic.nickname, userBasic.name);
            usersListById.put(userBasic.id, user);
            usersListByNickname.put(userBasic.nickname, user);
        }
    }

    static void downloadUserById(String id) {
        callForSingleObject(userDataAPI.getUser(id));
        UserInfo userInfo = (UserInfo) userRetrofit;
        if (userInfo.movies == null) {
            userInfo.movies = new HashMap<>();
        }
        if (userInfo.notes == null) {
            userInfo.notes = new HashMap<>();
        }
        if (userInfo.friends == null) {
            userInfo.friends = new ArrayList<>();
        }
        if (userInfo.books == null) {
            userInfo.books = new HashMap<>();
        }
        if (userInfo.trips == null) {
            userInfo.trips = new HashMap<>();
        }

        UserInformation user = new UserInformation(userInfo);
        usersListById.put(id, user);
        usersListByNickname.put(userInfo.nickname, user);
    }

    static void addUser(String userNickname, String userEmail, String userPassword) {
        /*ContentValues contentValues = new ContentValues();
        contentValues.put("email", userEmail);
        contentValues.put("password", userPassword);
        contentValues.put("nickname", userNickname);
        contentValues.put("photo", "user_photo_default");
        contentValues.put("header", "user_header_default");
        String userId = String.valueOf(dataBase.insertOrThrow("userInfo", null, contentValues));*/
        UsersAuth userAuth = new UsersAuth(userEmail, userPassword);
        callForSingleObject(userDataAPI.postUserAuth(userAuth));
        userAuth = (UsersAuth) userRetrofit;
        String userId = userAuth.id;
        String userName = userNickname;
        userNickname = "@" + userName;
        UserInfo userInfo = new UserInfo(userNickname, userName, userId);
        callForSingleObject(userDataAPI.putUserBasic(userId, userNickname, userName));
        callForSingleObject(userDataAPI.putUser(userId, userInfo));

        UserInformation user = new UserInformation(userId, userNickname, userName);
        credentials.put(userEmail, new Pair<>(userPassword, userId));
        usersListByNickname.put(userNickname, user);
        usersListById.put(userId, user);
    }

    /*static void loadDataBase(SQLiteDatabase db) {
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
    }*/

    static class UsersAuth {
        private String id;
        private String email;
        private String password;

        private UsersAuth(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    static class UserInfo {
        String id;
        String nickname;
        String photo;
        String header;
        String bio = "";
        String name = "";
        String birth = "";
        String about = "";
        HashMap<String, TreeMap<String, String>> notes = new HashMap<>();
        HashMap<String, HashMap<String, ArrayList<String>>> trips = new HashMap<>();
        HashMap<String, HashMap<String, ArrayList<String>>> movies = new HashMap<>();
        HashMap<String, HashMap<String, ArrayList<String>>> books = new HashMap<>();
        ArrayList<String> friends = new ArrayList<>();

        private UserInfo(String nickname, String name, String id) {
            this.id = id;
            this.nickname = nickname;
            this.name = name;
        }
    }
}
