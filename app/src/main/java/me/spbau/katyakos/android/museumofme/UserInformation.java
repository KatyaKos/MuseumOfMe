package me.spbau.katyakos.android.museumofme;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.TreeMap;

public class UserInformation {

    public UserInformation(SQLiteDatabase dataBase, Integer userId, String userNickname) {
        this.dataBase = dataBase;
        this.userId = userId;
        userIdString = userId.toString();
        this.userNickname = "@" + userNickname;
        this.userName = userNickname;
        userPhoto = "user_photo_default";
        userHeader = "user_header_default";
    }

    private SQLiteDatabase dataBase;

    private Integer userId;
    private String userIdString;
    private String userNickname;
    private String userPhoto;
    private String userHeader;
    private String userBio;

    private String userName;
    private String userBirth;
    private String userAbout;

    private TreeMap<String, UserInformation> friends = new TreeMap<>();

    private TreeMap<Integer, Trip> trips = new TreeMap<>();

    private TreeMap<Integer, Note> notes = new TreeMap<>();

    private TreeMap<Integer, Interest> movies = new TreeMap<>();

    private TreeMap<Integer, Interest> books = new TreeMap<>();

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

    TreeMap<Integer, Trip> getUserMap() {
        return trips;
    }

    TreeMap<Integer, Note> getUserNotes() {
        return notes;
    }

    TreeMap<Integer, Interest> getUserMovies() {
        return movies;
    }

    TreeMap<Integer, Interest> getUserBooks() {
        return books;
    }

    private void updateDataBaseColumn(String tableName, String columnName, String value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(columnName, value);
        dataBase.update(tableName, contentValues, "id = ?", new String[]{userIdString});
    }

    boolean setUserPhoto(String photo) {
        userPhoto = photo;
        updateDataBaseColumn("userInfo", "photo", photo);
        return true;
    }

    boolean setUserHeader(String photo) {
        userHeader = photo;
        updateDataBaseColumn("userInfo", "header", photo);
        return true;
    }

    boolean setUserBio(String text) {
        userBio = text;
        updateDataBaseColumn("userInfo", "bio", text);
        return true;
    }

    boolean setUserName(String name) {
        if (name == null || name.length() < 3) {
            return false;
        }
        userName = name;
        updateDataBaseColumn("userInfo", "name", name);
        return true;
    }

    boolean setUserBirth(String birth) {
        userBirth = birth;
        updateDataBaseColumn("userInfo", "birth", birth);
        return true;
    }

    boolean setUserAbout(String text) {
        userAbout = text;
        updateDataBaseColumn("userInfo", "about", text);
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

    private <T> boolean addToMuseum(TreeMap<Integer, T> museumSection, Integer id, T argument) {
        if (!museumSection.isEmpty() && museumSection.containsKey(id)) {
            return false;
        }
        museumSection.put(id, argument);
        return true;
    }

    private <T> boolean removeFromMuseum(TreeMap<Integer, T> museumSection, Integer id) {
        if (museumSection.isEmpty() || !museumSection.containsKey(id)) {
            return false;
        }
        museumSection.remove(id);
        return true;
    }

    void loadGroup(Integer groupId, String groupName) {
        trips.put(groupId, new Trip(groupId, groupName));
    }

    boolean addGroup(String groupName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("groupName", groupName);
        Integer groupId = (int) dataBase.insertOrThrow("userTripsGroups", null, contentValues);
        return addToMuseum(trips, groupId, new Trip(groupId, groupName));
    }

    boolean removeGroup(Integer id) {
        return removeFromMuseum(trips, id);
    }

    void loadPlace(Integer groupId, Integer placeId, String placeName) {
        Trip trip = trips.get(groupId);
        trip.loadPlace(placeId, placeName);
    }

    boolean addPlace(Integer groupId, String placeName) {
        if (!trips.isEmpty() && !trips.containsKey(groupId)) {
            return false;
        }
        Trip trip = trips.get(groupId);
        int placeId = trip.addPlace(placeName);
        if (placeId == -1) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("placeId", placeId);
        contentValues.put("placeName", placeName);
        contentValues.put("groupId", groupId);
        dataBase.insertOrThrow("userTripsPlaces", null, contentValues);
        trips.put(groupId, trip);
        return true;
    }

    boolean removePlace(Integer groupId, Integer placeId) {
        if (trips.isEmpty() || !trips.containsKey(groupId)) {
            return false;
        }
        Trip trip = trips.get(groupId);
        if (!trip.removePlace(placeId)) {
            return false;
        }
        trips.put(groupId, trip);
        return true;
    }

    boolean addNote(TreeMap<String, String> note) {
        Integer noteId = 1;
        if (!notes.isEmpty()) {
            noteId = notes.lastKey() + 1;
        }
        return addToMuseum(notes, noteId, new Note(noteId, note));
    }

    boolean removeNote(Integer noteId) {
        return removeFromMuseum(notes, noteId);
    }

    boolean addMovie(ArrayList<String> content, Integer rating, ArrayList<String> characters) {
        Integer movieId = 1;
        if (!movies.isEmpty()) {
            movieId = movies.lastKey() + 1;
        }
        return addToMuseum(movies, movieId, new Interest(movieId, content, rating, characters));
    }

    boolean removeMovie(Integer movieId) {
        return removeFromMuseum(movies, movieId);
    }

    boolean addBook(ArrayList<String> content, Integer rating, ArrayList<String> characters) {
        Integer bookId = 1;
        if (!books.isEmpty()) {
            bookId = books.lastKey() + 1;
        }
        return addToMuseum(books, bookId, new Interest(bookId, content, rating, characters));
    }

    boolean removeBook(Integer bookId) {
        return removeFromMuseum(books, bookId);
    }

    class Trip {
        private Integer groupId;
        private String groupName;
        private TreeMap<Integer, String> places;

        private Trip(Integer groupId, String groupName) {
            this.groupId = groupId;
            this.groupName = groupName;
            places = new TreeMap<>();
        }

        TreeMap<Integer, String> getPlaces() {
            return places;
        }

        Integer getGroupId() {
            return groupId;
        }

        String getGroupName() {
            return groupName;
        }

        private void loadPlace(Integer placeId, String placeName) {
            places.put(placeId, placeName);
        }

        private int addPlace(String name) {
            Integer id = 1;
            if (!places.isEmpty()) {
                id = places.lastKey() + 1;
            }
            if (places.containsKey(id)) {
                return -1;
            }
            places.put(id, name);
            return id;
        }

        private boolean removePlace(Integer id) {
            if (places.isEmpty() || !places.containsKey(id)) {
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
        private TreeMap<String, String> note = new TreeMap<>();

        private Note(Integer id, TreeMap<String, String> note) {
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

        TreeMap<String, String> getNote() {
            return note;
        }
    }

    class Interest {
        private Integer id;
        private String name;
        private String authorName; //director for movies
        private String photo = "interest_photo_default";
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

        String getName() {
            return name;
        }

        String getAuthor() {
            return authorName;
        }

        String getPhoto() {
            return photo;
        }

        Integer getId() {
            return id;
        }
    }
}
