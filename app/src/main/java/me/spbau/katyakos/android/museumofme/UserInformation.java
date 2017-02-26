package me.spbau.katyakos.android.museumofme;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

import retrofit2.Call;

public class UserInformation {


    //Server
    private static UserDataAPI userDataAPI = RetrofitInitializer.getInstance().getAPI();
    private AllUsersInformation.UserInfo currentUser;

    //private SQLiteDatabase dataBase;
    private Resources resources;
    private String userId;
    private String userNickname;
    private Bitmap userPhoto;
    private Bitmap userHeader;
    private String userBio;
    private String userName;
    private String userBirth;
    private String userAbout;
    private TreeMap<String, UserInformation> friends = new TreeMap<>();
    private TreeMap<Integer, Trip> trips = new TreeMap<>();
    private TreeMap<Integer, Note> notes = new TreeMap<>();
    private TreeMap<Integer, Interest> movies = new TreeMap<>();
    private TreeMap<Integer, Interest> books = new TreeMap<>();

    /*public UserInformation(SQLiteDatabase dataBase, String userId, String userNickname) {
        this.dataBase = dataBase;
        this.userId = userId;
        this.userNickname = "@" + userNickname;
        this.userName = userNickname;
        userPhoto = "user_photo_default";
        userHeader = "user_header_default";
    }*/

    public UserInformation(String id, String nickname, String name, String photo, Context context) {
        resources = context.getResources();
        userId = id;
        userNickname = nickname;
        userName = name;
        if (photo == null) {
            userPhoto = BitmapFactory.decodeResource(resources, R.drawable.user_photo_default);
        } else {
            userPhoto = decodeSampledBitmapFromFile(photo);
        }
    }

    public UserInformation(AllUsersInformation.UserInfo user, Context context) {
        resources = context.getResources();
        currentUser = user;
        userId = user.id;
        userName = user.name;
        userNickname = user.nickname;
        userBio = user.bio;
        userBirth = user.birth;
        userAbout = user.about;
        if (user.photo == null) {
            userPhoto = BitmapFactory.decodeResource(resources, R.drawable.user_photo_default);
        } else {
            userPhoto = decodeSampledBitmapFromFile(user.photo);
        }
        if (user.header == null) {
            userHeader = BitmapFactory.decodeResource(resources, R.drawable.user_header_default);
        } else {
            userHeader = decodeSampledBitmapFromFile(user.header);
        }
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

    public static Bitmap decodeSampledBitmapFromFile(String file) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, options);

        int reqWidth = 140;
        int reqHeight = 140;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    String getUserId() {
        return userId;
    }

    String getUserNickname() {
        return userNickname;
    }

    Bitmap getUserPhoto() {
        return userPhoto;
    }

    Bitmap getUserHeader() {
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

    /*private void updateDataBaseColumn(String tableName, String columnName, String value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(columnName, value);
        dataBase.update(tableName, contentValues, "id = ?", new String[]{userId});
    }*/

    boolean setUserPhoto(String photo) {
        userPhoto = decodeSampledBitmapFromFile(photo);
        //updateDataBaseColumn("userInfo", "photo", photo);
        currentUser.photo = photo;
        updateUserOnServer();
        return true;
    }

    boolean setUserHeader(String photo) {
        userHeader = decodeSampledBitmapFromFile(photo);
        //updateDataBaseColumn("userInfo", "header", photo);
        currentUser.header = photo;
        updateUserOnServer();
        return true;
    }

    boolean setUserBio(String text) {
        userBio = text;
        //updateDataBaseColumn("userInfo", "bio", text);
        currentUser.bio = text;
        updateUserOnServer();
        return true;
    }

    boolean setUserName(String name) {
        if (name == null || name.length() < 3) {
            return false;
        }
        userName = name;
        //updateDataBaseColumn("userInfo", "name", name);
        currentUser.name = name;
        updateUserOnServer();
        return true;
    }

    boolean setUserBirth(String birth) {
        userBirth = birth;
        //updateDataBaseColumn("userInfo", "birth", birth);
        currentUser.birth = birth;
        updateUserOnServer();
        return true;
    }

    boolean setUserAbout(String text) {
        userAbout = text;
        //updateDataBaseColumn("userInfo", "about", text);
        currentUser.about = text;
        updateUserOnServer();
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
        /*ContentValues contentValues = new ContentValues();
        contentValues.put("groupName", groupName);
        Integer groupId = (int) dataBase.insert("userTripsGroups", null, contentValues);*/
        Integer groupId = 0;
        if (!trips.isEmpty()) {
            groupId = trips.firstKey() - 1;
        }
        HashMap<String, ArrayList<String>> group = new HashMap<>();
        ArrayList<String> array = new ArrayList<>();
        array.add(groupName);
        group.put("name", array);
        group.put("places", new ArrayList<String>());
        currentUser.trips.put(groupId.toString(), group);
        return addToMuseum(trips, groupId, new Trip(groupId, groupName));
    }

    boolean removeGroup(Integer id) {
        if (trips.isEmpty() || !trips.containsKey(id)) {
            return false;
        }
        /*TreeMap<Integer, String> placesIds = trips.get(id).getPlaces();
        Set<Map.Entry<Integer, String>> usersEntry = placesIds.entrySet();
        for (Map.Entry<Integer, String> entry : usersEntry) {
            dataBase.delete("userTripsPlaces", "placeId = ? AND groupId = ?", new String[]{entry.getKey().toString(), id.toString()});
        }*/
        currentUser.trips.remove(id.toString());
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
        /*ContentValues contentValues = new ContentValues();
        contentValues.put("placeId", placeId);
        contentValues.put("placeName", placeName);
        contentValues.put("groupId", groupId);
        dataBase.insert("userTripsPlaces", null, contentValues);*/
        trips.put(groupId, trip);
        String id = groupId.toString();
        HashMap<String, ArrayList<String>> hashMap = currentUser.trips.get(id);
        ArrayList<String> array = hashMap.get("places");
        array.add(placeName);
        hashMap.put("places", array);
        currentUser.trips.put(id, hashMap);
        updateUserOnServer();
        return true;
    }

    boolean removePlace(Integer groupId, Integer placeId) {
        if (trips.isEmpty() || !trips.containsKey(groupId)) {
            return false;
        }
        Trip trip = trips.get(groupId);
        String placeName = trip.getPlaceName(placeId);
        if (!trip.removePlace(placeId)) {
            return false;
        }
        //dataBase.delete("userTripsPlaces", "placeId = ? AND groupId = ?", new String[]{placeId.toString(), groupId.toString()});
        trips.put(groupId, trip);
        String id = groupId.toString();
        HashMap<String, ArrayList<String>> hashMap = currentUser.trips.get(id);
        ArrayList<String> array = hashMap.get("places");
        array.remove(placeName);
        hashMap.put("places", array);
        currentUser.trips.put(id, hashMap);
        updateUserOnServer();
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

    /*private byte[] serializeArrayList(ArrayList<String> array) {
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
    }*/

    void loadMovie(Integer id, TreeMap<String, String> content, Float rating, ArrayList<String> characters) {
        //movies.put(id, new Interest(id, content, rating, characters));
    }

    boolean addInterest(String type, TreeMap<String, String> content, ArrayList<String> characters) {
        HashMap<String, ArrayList<String>> interest = new HashMap<>();
        Set<String> keys = content.keySet();
        for (String key : keys) {
            ArrayList<String> array = new ArrayList<>();
            array.add(content.get(key));
            interest.put(key, array);
        }
        interest.put("characters", characters);
        if (type.equals("movie")) {
            return addMovie(interest);
        } else {
            return addBook(interest);
        }
    }

    private boolean addMovie(HashMap<String, ArrayList<String>> interest) {
        //Integer movieId = addInterestToTable(content, rating, characters, "movie");
        Integer movieId = 0;
        if (!movies.isEmpty()) {
            movieId = movies.firstKey() - 1;
        }
        currentUser.movies.put(movieId.toString(), interest);
        return addToMuseum(movies, movieId, new Interest(movieId, interest));
    }

    boolean removeMovie(Integer movieId) {
        currentUser.movies.remove(movieId.toString());
        return removeFromMuseum(movies, movieId);
    }

    void loadBook(Integer id, TreeMap<String, String> content, Float rating, ArrayList<String> characters) {
        //books.put(id, new Interest(id, content, rating, characters));
    }

    private boolean addBook(HashMap<String, ArrayList<String>> interest) {
        //Integer bookId = addInterestToTable(content, rating, characters, "book");
        Integer bookId = 0;
        if (!books.isEmpty()) {
            bookId = books.firstKey() - 1;
        }
        currentUser.books.put(bookId.toString(), interest);
        return addToMuseum(books, bookId, new Interest(bookId, interest));
    }

    boolean removeBook(Integer bookId) {
        currentUser.books.remove(bookId.toString());
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

        String getPlaceName(Integer id) {
            return places.get(id);
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
        private Bitmap photo;
        private String review = "";
        private Float rating = 0F;
        private ArrayList<String> characters = new ArrayList<>(); //actors for movies

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
            field = interest.get("photo");
            if (field == null) {
                photo = BitmapFactory.decodeResource(resources, R.drawable.interest_photo_default);
            } else {
                photo = decodeSampledBitmapFromFile(field.get(0));
            }
        }

        String getName() {
            return name;
        }

        String getAuthor() {
            return authorName;
        }

        Bitmap getPhoto() {
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
