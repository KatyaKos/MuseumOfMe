package me.spbau.katyakos.android.museumofme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class UserDataBaseHelper extends SQLiteOpenHelper {
    UserDataBaseHelper(Context context) {
        super(context, "userDataBase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table userInfo ("
                + "id integer primary key autoincrement," + "email text," + "password text,"
                + "nickname text," + "name text," + "photo text," + "header text,"
                + "bio text," + "birth text," + "about text" + ");");

        db.execSQL("create table userTripsGroups ("
                + "id integer primary key autoincrement," + "groupName text" + ");");
        db.execSQL("create table userTripsPlaces ("
                + "id integer primary key autoincrement," + "placeId integer," + "groupId integer,"
                + "placeName text" + ");");

        db.execSQL("create table userNotes ("
                + "id integer primary key autoincrement," + "date text," + "name text,"
                + "content text," + "tags text" + ");");

        db.execSQL("create table userInterests ("
                + "id integer primary key autoincrement," + "authorName text," + "name text,"
                + "photo text," + "review text," + "rating real," + "characters blob," + "type text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
