package me.spbau.katyakos.android.museumofme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

/**
 * Created by KatyaKos on 27.11.2016.
 */

public class AllUsersInformation {
    private static ArrayList<String> DUMMY_USERS = new ArrayList<>(Arrays.asList(
            "@garyOldman::Gary Oldman::user_photo_default",
            "@thehughjackman::Hugh Jackman::user_photo_default",
            "@jMcAvoy::James McAvoy::user_photo_default",
            "@fassy::Michael Fassbender::user_photo_default",
            "@jude_law::Jude Law::user_photo_default"
    ));

    public static ArrayList<String> getUsers() {
        return DUMMY_USERS;
    }

    public static boolean usersContain(String nickname) {
        for (String user : DUMMY_USERS) {
            String[] pieces = user.split("::");
            if (pieces[0].equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    public static void changeUserNickname(String oldNick, String newNick) {
        ListIterator<String> iterator = DUMMY_USERS.listIterator();
        while (iterator.hasNext()) {
            String user = iterator.next();
            String[] pieces = user.split("::");
            if (pieces[0].equals(oldNick)) {
                String userPart = user.substring(oldNick.length());
                iterator.set(newNick + userPart);
            }
        }
    }

    public static void changeUserName(String nickname, String newName) {
        ListIterator<String> iterator = DUMMY_USERS.listIterator();
        while (iterator.hasNext()) {
            String user = iterator.next();
            String[] pieces = user.split("::");
            if (pieces[0].equals(nickname)) {
                iterator.set(pieces[0] + "::" + newName + "::" + pieces[2]);
            }
        }
    }
}
