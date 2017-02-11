package me.spbau.katyakos.android.museumofme;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface UserDataAPI {

    @GET("usersAuth")
    Call<List<AllUsersInformation.UsersAuth>> getUsersAuth();

    @GET("usersBasic")
    Call<List<AllUsersInformation.UsersBasicInfo>> getUsersBasic();

    @POST("usersBasic/{userid}")
    Call<AllUsersInformation.UsersBasicInfo> putUserBasic(@Path("userid") String userid);

    @GET("users/{user}")
    Call<AllUsersInformation.UserInfo> getUser(@Path("user") String user);

    @POST("users/{userid}")
    Call<UserInformation> putUser(@Path("userid") String userid);

    @POST("notes")
    Call<UserInformation.Note> createNote(@Body String name, String date, String content, String tag);

}
