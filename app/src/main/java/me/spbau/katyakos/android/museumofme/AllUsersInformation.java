package me.spbau.katyakos.android.museumofme;

import android.util.Pair;

import java.util.HashMap;
import java.util.TreeMap;

class AllUsersInformation {
    private static TreeMap<Integer, UserInformation> usersListById = new TreeMap<Integer, UserInformation>() {{
        put(0, new UserInformation(0, "@katyakos_", "me@test.com", "go"));
        put(1, new UserInformation(1, "@user1", "user1@test.com", "hello"));
        put(2, new UserInformation(2, "@user2", "user2@test.com", "hi"));
        put(3, new UserInformation(3, "@user3", "user3@test.com", "you"));
        put(4, new UserInformation(4, "@user4", "user4@test.com", "me"));
    }};
    private static TreeMap<String, UserInformation> usersListByNickname = new TreeMap<String, UserInformation>() {{
        put("@katyakos_", new UserInformation(0, "@katyakos_", "me@test.com", "go"));
        put("@user3", new UserInformation(3, "@user3", "user3@test.com", "you"));
        put("@user2", new UserInformation(2, "@user2", "user2@test.com", "hi"));
        put("@user1", new UserInformation(1, "@user1", "user1@test.com", "hello"));
        put("@user4", new UserInformation(4, "@user4", "user4@test.com", "me"));
    }};

    private static HashMap<String, Pair<String, Integer>> credentials = new HashMap<String, Pair<String, Integer>>() {{
        put("me@test.com", new Pair<>("go", 0));
        put("user1@test.com", new Pair<>("hello", 1));
        put("user2@test.com", new Pair<>("hi", 2));
        put("user3@test.com", new Pair<>("you", 3));
        put("user4@test.com", new Pair<>("me", 4));
    }};

    static TreeMap<String, UserInformation> getUsersListByNickname() {
        return usersListByNickname;
    }

    static TreeMap<Integer, UserInformation> getUsersListById() {
        return usersListById;
    }

    static Integer getIdByEmail(String email) {
        return getCredential(email).second;
    }

    static String getNicknameById(Integer id) {
        return getUserById(id).getUserNickname();
    }

    static boolean containsByNickname(String nickname) {
        return usersListByNickname.containsKey(nickname);
    }

    static boolean containsByEmail(String email) {
        return credentials.containsKey(email);
    }

    static boolean checkEmailPasswordPair(String email, String password) {
        return credentials.containsKey(email) && getCredential(email).first.equals(password);
    }

    static void addUser(String userNickname, String userEmail, String userPassword) {
        Integer userId = usersListById.lastKey() + 1;
        UserInformation user = new UserInformation(userId, userNickname, userEmail, userPassword);
        credentials.put(userEmail, new Pair<>(userPassword, userId));
        usersListByNickname.put(userNickname, user);
        usersListById.put(userId, user);
    }

    static boolean changeUserName(Integer userId, String newName) {
        UserInformation user = getUserById(userId);
        String userNickname = user.getUserNickname();
        if (!user.setUserName(newName)) {
            return false;
        }
        usersListByNickname.put(userNickname, user);
        usersListById.put(userId, user);
        return true;
    }

    static UserInformation getUserById(Integer id) {
        return usersListById.get(id);
    }

    private static Pair<String, Integer> getCredential(String email) {
        return credentials.get(email);
    }
}
