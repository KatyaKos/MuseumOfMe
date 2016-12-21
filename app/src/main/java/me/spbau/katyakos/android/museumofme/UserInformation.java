package me.spbau.katyakos.android.museumofme;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

public class UserInformation {

    public UserInformation(Integer userId, String userNickname, String userEmail, String userPassword) {
        this.userId = userId;
        this.userNickname = "@" + userNickname;
        this.userName = userNickname;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    private Integer userId;
    private String userEmail;
    private String userPassword;
    private String userNickname;
    private String userPhoto = "user_photo_default";
    private String userHeader = "user_header_default";
    private String userBio = "";

    private String userName;
    private String userBirth = "";
    private String userAbout = "";

    private TreeMap<String, UserInformation> friends = new TreeMap<>();

    private HashMap<Integer, Trip> trips = new HashMap<>();

    private HashMap<Integer, Note> notes = new HashMap<>();

    private HashMap<Integer, Interest> movies = new HashMap<>();

    private HashMap<Integer, Interest> books = new HashMap<>();

    Integer getUserId() {
        return userId;
    }

    String getUserNickname() {
        return userNickname;
    }

    String getUserPhoto() {
        return userPhoto;
    }

    String getUserHeader() {
        return userHeader;
    }

    String getUserBio() {
        return userBio;
    }

    String getUserName() {
        return userName;
    }

    String getUserBirth() {
        return userBirth;
    }

    String getUserAbout() {
        return userAbout;
    }

    TreeMap<String, UserInformation> getUserFriends() {
        return friends;
    }

    HashMap<Integer, Trip> getUserMap() {
        return trips;
    }

    HashMap<Integer, Note> getUserNotes() {
        return notes;
    }

    HashMap<Integer, Interest> getUserMovies() {
        return movies;
    }

    HashMap<Integer, Interest> getUserBooks() {
        return books;
    }

    boolean setUserPhoto(String photo) {
        userPhoto = photo;
        return true;
    }

    boolean setUserHeader(String photo) {
        userHeader = photo;
        return true;
    }

    boolean setUserBio(String text) {
        userBio = text;
        return true;
    }

    boolean setUserName(String name) {
        if (name.length() < 4) {
            return false;
        }
        userName = name;
        return true;
    }

    boolean setUserBirth(String birth) {
        userBirth = birth;
        return true;
    }

    boolean setUserAbout(String text) {
        userAbout = text;
        return true;
    }

    boolean hasFriend(String friendNickname) {
        return friends.containsKey(friendNickname);
    }

    boolean addFriend(Integer friendId) {
        UserInformation friend = AllUsersInformation.getUserById(friendId);
        String friendNickname = friend.getUserNickname();
        if (userId.equals(friendId) || friends.containsKey(friendNickname)) {
            return false;
        }
        friends.put(friendNickname, friend);
        return true;
    }

    boolean removeFriend(Integer friendId) {
        UserInformation friend = AllUsersInformation.getUserById(friendId);
        String friendNickname = friend.getUserNickname();
        if (userId.equals(friendId) || !friends.containsKey(friendNickname)) {
            return false;
        }
        friends.remove(friendNickname);
        return true;
    }

    private <T> boolean addToMuseum(HashMap<Integer, T> museumSection, Integer id, T argument) {
        if (museumSection.containsKey(id)) {
            return false;
        }
        museumSection.put(id, argument);
        return true;
    }

    private <T> boolean removeFromMuseum(HashMap<Integer, T> museumSection, Integer id) {
        if (!museumSection.containsKey(id)) {
            return false;
        }
        museumSection.remove(id);
        return true;
    }

    boolean addGroupToMap(Integer groupId, String groupName) {
        return addToMuseum(trips, groupId, new Trip(groupId, groupName));
    }

    boolean removeGroup(Integer id) {
        return removeFromMuseum(trips, id);
    }

    boolean addPlace(Integer groupId, Integer placeId, String placeName) {
        if (!trips.containsKey(groupId)) {
            return false;
        }
        Trip trip = trips.get(groupId);
        if (!trip.addPlace(placeId, placeName)) {
            return false;
        }
        trips.put(groupId, trip);
        return true;
    }

    boolean removePlace(Integer groupId, Integer placeId) {
        if (!trips.containsKey(groupId)) {
            return false;
        }
        Trip trip = trips.get(groupId);
        if (!trip.removePlace(placeId)) {
            return false;
        }
        trips.put(groupId, trip);
        return true;
    }

    boolean addNote(HashMap<String, String> note) {
        Integer noteId = Collections.max(notes.keySet()) + 1;
        return addToMuseum(notes, noteId, new Note(noteId, note));
    }

    boolean removeNote(Integer noteId) {
        return removeFromMuseum(notes, noteId);
    }

    boolean addMovie(Integer movieId, ArrayList<String> content, Integer rating, ArrayList<String> characters) {
        return addToMuseum(movies, movieId, new Interest(movieId, content, rating, characters));
    }

    boolean removeMovie(Integer movieId) {
        return removeFromMuseum(movies, movieId);
    }

    boolean addBook(Integer bookId, ArrayList<String> content, Integer rating, ArrayList<String> characters) {
        return addToMuseum(books, bookId, new Interest(bookId, content, rating, characters));
    }

    boolean removeBook(Integer bookId) {
        return removeFromMuseum(books, bookId);
    }

    class Trip {
        private Integer groupId;
        private String groupName;
        private SparseArray<String> places;

        private Trip(Integer groupId, String groupName) {
            this.groupId = groupId;
            this.groupName = groupName;
            places = new SparseArray<>();
        }

        private boolean addPlace(Integer id, String name) {
            if (places.indexOfKey(id) >= 0) {
                return false;
            }
            places.put(id, name);
            return true;
        }

        private boolean removePlace(Integer id) {
            if (places.indexOfKey(id) < 0) {
                return false;
            }
            places.remove(id);
            return true;
        }
    }

    class Note {
        private Integer id;
        private String date;
        private String name;
        private String content;
        private String tags;
        private HashMap<String, String> note = new HashMap<>();

        private Note(Integer id, HashMap<String, String> note) {
            this.id = id;
            note.put("id", id.toString());
            this.note = note;
            date = note.get("date");
            name = note.get("name");
            content = note.get("text");
            tags = note.get("tags");
        }

        String getTags() {
            return tags;
        }

        Integer getId() {
            return id;
        }

        HashMap<String, String> getNote() {
            return note;
        }
    }

    class Interest {
        private Integer id;
        private String name;
        private String authorName;
        private String photo;
        private String review;
        private Integer rating;
        private ArrayList<String> characters; //actors for movies

        private Interest(Integer id, ArrayList<String> content, Integer rating, ArrayList<String> characters) {
            this.id = id;
            this.name = content.get(0);
            this.authorName = content.get(1);
            this.photo = content.get(2);
            this.review = content.get(3);
            this.characters = characters;
            this.rating = rating;
        }
    }
}
