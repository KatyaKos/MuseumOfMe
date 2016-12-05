package me.spbau.katyakos.android.museumofme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ListIterator;

/**
 * Created by KatyaKos on 27.11.2016.
 */

public class UserInformation {
    private static String userNickname = "@garyOldman";
    private static String userPhoto = "user_photo_default";
    private static String userHeader = "user_header_default";
    private static String userBio = "nananana BATMAN";

    private static String userName = "Gary Oldman";
    private static String userBirth = "21.05.1958";
    private static String userAbout = " English actor, filmmaker, musician and author who has performed in theatre," +
            " film and television.";

    private static ArrayList<String> DUMMY_FRIENDS = new ArrayList<>(Arrays.asList(
            "@jMcAvoy::James McAvoy::user_photo_default",
            "@fassy::Michael Fassbender::user_photo_default"
    ));

    private static ArrayList<String> DUMMY_TRIPS = new ArrayList<>(Arrays.asList(
            "1110.11.11 at 11.11.11::4.07 - 01.08.17 — Thailand::Bangkok::Phuket",
            "1100.11.11 at 11.11.11::04.02 - 11.09.16::Amsterdam::Paris::Stockholm::Tallinn::Berlin::Barcelona::Madrid::Zürich",
            "1000.11.11 at 11.11.11::01.07 - 22.02.16 — America::Chicago::San Diego"
    ));

    private static ArrayList<String> DUMMY_NOTES = new ArrayList<>(Arrays.asList(
            "16.10.28 at 23:44::Demo::Plz help me::#demo #problems #help",
            "16.10.28 at 23:44::Harry Potter::I solemnly swear that I am up to no good!\nMischief managed.\nHarry Potter is deaaaaaaaaaaaaaad\nIt doesn't matter that Harry's gone. People die every day.::#demo #HarryPotter #Movie",
            "15.11.29 at 16:25::Peter Pan::Second star to the right and straight on till morning\nAll children, except one, grow up.::#movie #PeterPan"
    ));

    private static ArrayList<String> DUMMY_MOVIES = new ArrayList<>(Arrays.asList(
            "1000.11.11 at 11.11.11::Doctor Strange::interest_photo_default",
            "1100.11.11 at 11.11.11::Finding Neverland::interest_photo_default",
            "1110.11.11 at 11.11.11::Macbeth::interest_photo_default",
            "1111.11.11 at 11.11.11::The Fall::interest_photo_default",
            "1112.11.11 at 11.11.11::Watchmen::interest_photo_default"
    ));

    private static ArrayList<String> DUMMY_BOOKS = new ArrayList<>(Arrays.asList(
            "1000.11.11 at 11.11.11::Neverwhere::Neil Gaiman::interest_photo_default",
            "1100.11.11 at 11.11.11::Peter Pan::J.M.Barrie::interest_photo_default",
            "1110.11.11 at 11.11.11::Sandman::Neil Gaiman::interest_photo_default",
            "1111.11.11 at 11.11.11::Sherlock Holmes::Arthur Conan Doyle::interest_photo_default",
            "1112.11.11 at 11.11.11::The Master and Margarita::Mikhail Bulgakov::interest_photo_default"
    ));

    public static String getUser() {
        return userNickname + "::" + userName + "::" + userPhoto;
    }

    public static String getUserNickname() {
        return userNickname;
    }

    public static String getUserPhoto() {
        return userPhoto;
    }

    public static String getUserHeader() {
        return userHeader;
    }

    public static String getUserBio() {
        return userBio;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getUserBirth() {
        return userBirth;
    }

    public static String getUserAbout() {
        return userAbout;
    }

    public static boolean setUserNickname(String name) {
        if (name.length() < 3 || name.contains("@") || name.contains(" ")) {
            return false;
        }
        AllUsersInformation.changeUserNickname(userNickname, "@" + name);
        userNickname = "@" + name;

        return true;
    }

    public static boolean setUserPhoto(String photo) {
        userPhoto = photo;
        return true;
    }

    public static boolean setUserHeader(String photo) {
        userHeader = photo;
        return true;
    }

    public static boolean setUserBio(String text) {
        userBio = text;
        return true;
    }

    public static boolean setUserName(String name) {
        AllUsersInformation.changeUserName(userNickname, name);
        userName = name;
        return true;
    }

    public static boolean setUserBirth(String birth) {
        userBirth = birth;
        return true;
    }

    public static boolean setUserAbout(String text) {
        userAbout = text;
        return true;
    }

    public static ArrayList<String> getUserFriends() {
        return DUMMY_FRIENDS;
    }

    public static boolean addFriend(String friend) {
        if (userNickname.equals(friend) || DUMMY_FRIENDS.contains(friend)) {
            return false;
        }
        DUMMY_FRIENDS.add(friend);
        Collections.sort(DUMMY_FRIENDS);
        return true;
    }

    public static boolean removeFriend(String friend) {
        if (userNickname.equals(friend) || !DUMMY_FRIENDS.contains(friend)) {
            return false;
        }
        DUMMY_FRIENDS.remove(friend);
        Collections.sort(DUMMY_FRIENDS);
        return true;
    }

    public static ArrayList<String> getUserMap() {
        return DUMMY_TRIPS;
    }

    public static boolean addGroup(String group) {
        DUMMY_TRIPS.add(0, group);
        return true;
    }

    public static boolean removeGroup(String date) {
        for (int i = 0; i < DUMMY_TRIPS.size(); i++) {
            String string = DUMMY_TRIPS.get(i);
            String[] pieces = string.split("::");
            if (pieces[0].equals(date)) {
                DUMMY_TRIPS.remove(i);
                return true;
            }
        }
        return false;
    }

    public static boolean addPlace(String date, String place) {
        ListIterator<String> iterator = DUMMY_TRIPS.listIterator();
        while (iterator.hasNext()) {
            String string = iterator.next();
            String[] pieces = string.split("::");
            if (pieces[0].equals(date)) {
                iterator.set(string + "::" + place);
                return true;
            }
        }
        return false;
    }

    public static boolean removePlace(String date, String place) {
        ListIterator<String> iterator = DUMMY_TRIPS.listIterator();
        while (iterator.hasNext()) {
            String string = iterator.next();
            String[] pieces = string.split("::");
            if (pieces[0].equals(date)) {
                String newGroup = pieces[0] + "::" + pieces[1];
                for (int i = 2; i < pieces.length; i++) {
                    if (!pieces[i].equals(place)) {
                        newGroup += "::" + pieces[i];
                    }
                }
                iterator.set(newGroup);
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> getUserNotes() {
        return DUMMY_NOTES;
    }

    public static boolean addNote(String note) {
        DUMMY_NOTES.add(0, note);
        return true;
    }

    public static boolean removeNote(String date) {
        for (int i = 0; i < DUMMY_NOTES.size(); i++) {
            String string = DUMMY_NOTES.get(i);
            String[] pieces = string.split("::");
            if (pieces[0].equals(date)) {
                DUMMY_NOTES.remove(i);
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> getUserMovies() {
        return DUMMY_MOVIES;
    }

    public static boolean addMovie(String movie) {
        DUMMY_MOVIES.add(0, movie);
        return true;
    }

    public static boolean removeMovie(String date) {
        for (int i = 0; i < DUMMY_MOVIES.size(); i++) {
            String string = DUMMY_MOVIES.get(i);
            String[] pieces = string.split("::");
            if (pieces[0].equals(date)) {
                DUMMY_MOVIES.remove(i);
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> getUserBooks() {
        return DUMMY_BOOKS;
    }

    public static boolean addBook(String book) {
        DUMMY_BOOKS.add(0, book);
        return true;
    }

    public static boolean removeBook(String date) {
        for (int i = 0; i < DUMMY_BOOKS.size(); i++) {
            String string = DUMMY_BOOKS.get(i);
            String[] pieces = string.split("::");
            if (pieces[0].equals(date)) {
                DUMMY_BOOKS.remove(i);
                return true;
            }
        }
        return false;
    }
}
