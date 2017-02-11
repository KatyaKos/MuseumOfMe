package me.spbau.katyakos.android.museumofme;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import retrofit2.Call;

public class UserInformation {


    //Server
    private static UserDataAPI userDataAPI = RetrofitInitializer.getInstance().getAPI();
    private AllUsersInformation.UserInfo currentUser;

    private SQLiteDatabase dataBase;
    private String userId;
    private String userNickname;
    private String userPhoto = "user_photo_default";
    private String userHeader = "user_header_default";
    private String userBio;
    private String userName;
    private String userBirth;
    private String userAbout;
    private TreeMap<String, UserInformation> friends = new TreeMap<>();
    private TreeMap<Integer, Trip> trips = new TreeMap<>();
    private TreeMap<Integer, Note> notes = new TreeMap<>();
    private TreeMap<Integer, Interest> movies = new TreeMap<>();
    private TreeMap<Integer, Interest> books = new TreeMap<>();

    public UserInformation(SQLiteDatabase dataBase, String userId, String userNickname) {
        this.dataBase = dataBase;
        this.userId = userId;
        this.userNickname = "@" + userNickname;
        this.userName = userNickname;
        userPhoto = "user_photo_default";
        userHeader = "user_header_default";
    }

    public UserInformation(String id, String nickname, String name) {
        userId = id;
        userNickname = nickname;
        userName = name;
    }

    public UserInformation(AllUsersInformation.UserInfo user) {
        currentUser = user;

        userId = user.id;
        userName = user.name;
        userNickname = user.nickname;
        userBio = user.bio;
        userBirth = user.birth;
        userAbout = user.about;
        Set<String> keys = user.notes.keySet();
        for (String key : keys) {
            Integer id = Integer.valueOf(key);
            notes.put(id, new Note(id, user.notes.get(key)));
        }
        keys = user.trips.keySet();
        for (String key : keys) {
            Integer id = Integer.valueOf(key);
            Trip trip = new Trip(id, user.trips.get(key).get("name").get(0));
            ArrayList<String> places = user.trips.get(key).get("places");
            for (String name : places) {
                trip.addPlace(name);
            }
            trips.put(id, trip);
        }
        keys = user.movies.keySet();
        for (String key : keys) {
            Integer id = Integer.valueOf(key);
            Interest movie = new Interest(id, user.movies.get(key));
            movies.put(id, movie);
        }
        keys = user.books.keySet();
        for (String key : keys) {
            Integer id = Integer.valueOf(key);
            Interest book = new Interest(id, user.books.get(key));
            books.put(id, book);
        }

        for (String friendId : user.friends) {
            friends.put(friendId, AllUsersInformation.getUserById(friendId));
        }
    }

    String getUserId() {
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
        dataBase.update(tableName, contentValues, "id = ?", new String[]{userId});
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

    boolean addFriend(String friendId) {
        UserInformation friend = AllUsersInformation.getUserById(friendId);
        String friendNickname = friend.getUserNickname();
        if (userId.equals(friendId) || friends.containsKey(friendNickname)) {
            return false;
        }
        friends.put(friendNickname, friend);
        currentUser.friends.add(friendId);
        updateUserOnServer();
        return true;
    }

    boolean removeFriend(String friendId) {
        UserInformation friend = AllUsersInformation.getUserById(friendId);
        String friendNickname = friend.getUserNickname();
        if (userId.equals(friendId) || !friends.containsKey(friendNickname)) {
            return false;
        }
        friends.remove(friendNickname);
        currentUser.friends.remove(friendId);
        updateUserOnServer();
        return true;
    }

    private void updateUserOnServer() {
        try {
            final Call<AllUsersInformation.UserInfo> call = userDataAPI.putUser(userId, currentUser);
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        call.execute();
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

    private <T> boolean addToMuseum(TreeMap<Integer, T> museumSection, Integer id, T argument) {
        if (!museumSection.isEmpty() && museumSection.containsKey(id)) {
            return false;
        }
        museumSection.put(id, argument);
        updateUserOnServer();
        return true;
    }

    private <T> boolean removeFromMuseum(TreeMap<Integer, T> museumSection, Integer id) {
        if (museumSection.isEmpty() || !museumSection.containsKey(id)) {
            return false;
        }
        //dataBase.delete(tableName, "id = " + id, null);
        museumSection.remove(id);
        updateUserOnServer();
        return true;
    }

    void loadGroup(Integer groupId, String groupName) {
        trips.put(groupId, new Trip(groupId, groupName));
    }

    boolean addGroup(String groupName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("groupName", groupName);
        Integer groupId = (int) dataBase.insert("userTripsGroups", null, contentValues);
        return addToMuseum(trips, groupId, new Trip(groupId, groupName));
    }

    boolean removeGroup(Integer id) {
        if (trips.isEmpty() || !trips.containsKey(id)) {
            return false;
        }
        TreeMap<Integer, String> placesIds = trips.get(id).getPlaces();
        Set<Map.Entry<Integer, String>> usersEntry = placesIds.entrySet();
        for (Map.Entry<Integer, String> entry : usersEntry) {
            dataBase.delete("userTripsPlaces", "placeId = ? AND groupId = ?", new String[]{entry.getKey().toString(), id.toString()});
        }
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
        dataBase.insert("userTripsPlaces", null, contentValues);
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
        dataBase.delete("userTripsPlaces", "placeId = ? AND groupId = ?", new String[]{placeId.toString(), groupId.toString()});
        trips.put(groupId, trip);
        return true;
    }

    void loadNote(Integer id, TreeMap<String, String> note) {
        notes.put(id, new Note(id, note));
    }

    boolean addNote(TreeMap<String, String> note) {
        /*ContentValues contentValues = new ContentValues();
        contentValues.put("name", note.get("name"));
        contentValues.put("date", note.get("date"));
        contentValues.put("content", note.get("content"));
        contentValues.put("tags", note.get("tags"));
        Integer noteId = (int) dataBase.insert("userNotes", null, contentValues);*/
        Integer noteId = 0;
        if (!notes.isEmpty()) {
            noteId = notes.firstKey() - 1;
        }
        currentUser.notes.put(noteId.toString(), note);
        return addToMuseum(notes, noteId, new Note(noteId, note));
    }

    boolean removeNote(Integer noteId) {
        currentUser.notes.remove(noteId.toString());
        return removeFromMuseum(notes, noteId);
    }

    private byte[] serializeArrayList(ArrayList<String> array) {
        byte[] result = {};
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(array);
            out.flush();
            result = bos.toByteArray();
            bos.close();
            out.close();
        } catch (Exception e) {
            Log.d("myLogs", "serialization error");
        }
        return result;
    }

    private Integer addInterestToTable(TreeMap<String, String> content, Float rating, ArrayList<String> characters, String type) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("authorName", content.get("authorName"));
        contentValues.put("name", content.get("name"));
        contentValues.put("rating", rating);
        contentValues.put("review", content.get("review"));
        contentValues.put("photo", content.get("photo"));
        contentValues.put("type", type);
        byte[] charactersBytes = serializeArrayList(characters);
        contentValues.put("characters", charactersBytes);
        return (int) dataBase.insert("userInterests", null, contentValues);
    }

    void loadMovie(Integer id, TreeMap<String, String> content, Float rating, ArrayList<String> characters) {
        movies.put(id, new Interest(id, content, rating, characters));
    }

    boolean addInterest(String type, TreeMap<String, String> content, Float rating, ArrayList<String> characters) {
        if (type.equals("movie")) {
            return addMovie(content, rating, characters);
        } else {
            return addBook(content, rating, characters);
        }
    }

    private boolean addMovie(TreeMap<String, String> content, Float rating, ArrayList<String> characters) {
        Integer movieId = addInterestToTable(content, rating, characters, "movie");
        return addToMuseum(movies, movieId, new Interest(movieId, content, rating, characters));
    }

    boolean removeMovie(Integer movieId) {
        return removeFromMuseum(movies, movieId);
    }

    void loadBook(Integer id, TreeMap<String, String> content, Float rating, ArrayList<String> characters) {
        books.put(id, new Interest(id, content, rating, characters));
    }

    private boolean addBook(TreeMap<String, String> content, Float rating, ArrayList<String> characters) {
        Integer bookId = addInterestToTable(content, rating, characters, "book");
        return addToMuseum(books, bookId, new Interest(bookId, content, rating, characters));
    }

    boolean removeBook(Integer bookId) {
        return removeFromMuseum(books, bookId);
    }

    Interest getInterestById(String type, Integer id) {
        if (type.equals("movie")) {
            return movies.get(id);
        } else {
            return books.get(id);
        }
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
        private String tags;
        private TreeMap<String, String> note = new TreeMap<>();

        private Note(Integer id, TreeMap<String, String> note) {
            this.id = id;
            this.note.put("id", id.toString());
            this.note = note;
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
        private String authorName = ""; //director for movies
        private String photo = "interest_photo_default";
        private String review = "";
        private Float rating = 0F;
        private ArrayList<String> characters = new ArrayList<>(); //actors for movies

        private Interest(Integer id, TreeMap<String, String> content, Float rating, ArrayList<String> characters) {
            this.id = id;
            this.name = content.get("name");
            this.authorName = content.get("authorName");
            //this.photo = content.get("photo");
            this.review = content.get("review");
            this.characters = characters;
            this.rating = rating;
        }

        private Interest(Integer id, HashMap<String, ArrayList<String>> interest) {
            this.id = id;
            this.name = interest.get("name").get(0);
            ArrayList<String> field = interest.get("authorName");
            if (field != null) {
                this.authorName = field.get(0);
            }
            field = interest.get("review");
            if (field != null) {
                this.review = field.get(0);
            }
            field = interest.get("characters");
            if (field != null) {
                this.characters = field;
            }
            field = interest.get("rating");
            if (field != null) {
                this.rating = Float.valueOf(field.get(0));
            }
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

        ArrayList<String> getCharacters() {
            return characters;
        }

        String getReview() {
            return review;
        }

        Float getRating() {
            return rating;
        }
    }
}
