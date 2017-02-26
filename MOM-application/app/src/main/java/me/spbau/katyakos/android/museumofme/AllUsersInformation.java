package me.spbau.katyakos.android.museumofme;

import android.content.Context;
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

    static void downloadBasicInfo(Context context) {
        callForMultipleObjects(userDataAPI.getUsersBasic());
        List<UserInfo> usersBasicList = (List<UserInfo>) listRetrofit;
        for (UserInfo userBasic : usersBasicList) {
            UserInformation user = new UserInformation(userBasic.id, userBasic.nickname, userBasic.name, userBasic.photo, context);
            usersListById.put(userBasic.id, user);
            usersListByNickname.put(userBasic.nickname, user);
        }
    }

    static void downloadUserById(Context context, String id) {
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

        UserInformation user = new UserInformation(userInfo, context);
        usersListById.put(id, user);
        usersListByNickname.put(userInfo.nickname, user);
    }

    static void addUser(Context context, String userNickname, String userEmail, String userPassword) {
        UsersAuth userAuth = new UsersAuth(userEmail, userPassword);
        callForSingleObject(userDataAPI.postUserAuth(userAuth));
        userAuth = (UsersAuth) userRetrofit;
        String userId = userAuth.id;
        String userName = userNickname;
        userNickname = "@" + userName;
        UserInfo userInfo = new UserInfo(userNickname, userName, userId);
        callForSingleObject(userDataAPI.putUserBasic(userId, userNickname, userName));
        callForSingleObject(userDataAPI.putUser(userId, userInfo));

        UserInformation user = new UserInformation(userId, userNickname, userName, null, context);
        credentials.put(userEmail, new Pair<>(userPassword, userId));
        usersListByNickname.put(userNickname, user);
        usersListById.put(userId, user);
    }

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
