package me.spbau.katyakos.android.museumofme;

import android.util.SparseArray;

import java.util.ArrayList;
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

    private TreeMap<String, Integer> friends = new TreeMap<>();

    private SparseArray<Trip> trips = new SparseArray<>();

    private SparseArray<Note> notes = new SparseArray<>();

    private SparseArray<Interest> movies = new SparseArray<>();

    private SparseArray<Interest> books = new SparseArray<>();

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

    TreeMap<String, Integer> getUserFriends() {
        return friends;
    }

    SparseArray<Trip> getUserMap() {
        return trips;
    }

    SparseArray<Note> getUserNotes() {
        return notes;
    }

    SparseArray<Interest> getUserMovies() {
        return movies;
    }

    SparseArray<Interest> getUserBooks() {
        return books;
    }

    boolean setUserNickname(String nickname) {
        if (nickname.length() < 3 || nickname.contains("@") || nickname.contains(" ")) {
            return false;
        }
        userNickname = "@" + nickname;
        return true;
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

    boolean addFriend(Integer friendId) {
        String friendNickname = AllUsersInformation.getNicknameById(friendId);
        if (userId.equals(friendId) || friends.containsValue(friendId)) {
            return false;
        }
        friends.put(friendNickname, friendId);
        return true;
    }

    boolean removeFriend(Integer friendId) {
        String friendNickname = AllUsersInformation.getNicknameById(friendId);
        if (userId.equals(friendId) || !friends.containsValue(friendId)) {
            return false;
        }
        friends.remove(friendNickname);
        return true;
    }

    private <T> boolean addToMuseum(SparseArray<T> museumSection, Integer id, T argument) {
        if (museumSection.indexOfKey(id) >= 0) {
            return false;
        }
        museumSection.put(id, argument);
        return true;
    }

    private <T> boolean removeFromMuseum(SparseArray<T> museumSection, Integer id) {
        if (museumSection.indexOfKey(id) < 0) {
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
        if (trips.indexOfKey(groupId) < 0) {
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
        if (trips.indexOfKey(groupId) < 0) {
            return false;
        }
        Trip trip = trips.get(groupId);
        if (!trip.removePlace(placeId)) {
            return false;
        }
        trips.put(groupId, trip);
        return true;
    }

    boolean addNote(Integer noteId, ArrayList<String> note) {
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

    private static class Trip {
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

    private static class Note {
        private Integer id;
        private String date;
        private String content;
        private String tags;

        private Note(Integer id, ArrayList<String> note) {
            this.id = id;
            date = note.get(0);
            content = note.get(1);
            tags = note.get(2);
        }
    }

    private static class Interest {
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
