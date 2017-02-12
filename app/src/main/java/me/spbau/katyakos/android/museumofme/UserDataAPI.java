package me.spbau.katyakos.android.museumofme;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

interface UserDataAPI {

    //Login and Signup step
    @GET("usersAuth")
    Call<List<AllUsersInformation.UsersAuth>> getUsersAuth();

    @POST("usersAuth")
    Call<AllUsersInformation.UsersAuth> postUserAuth(@Body AllUsersInformation.UsersAuth user);

    //Basic information about users, for friends' list etc.
    @GET("usersBasic")
    Call<List<AllUsersInformation.UserInfo>> getUsersBasic();

    @FormUrlEncoded
    @PUT("usersBasic/{userid}")
    Call<AllUsersInformation.UserInfo> putUserBasic(@Path("userid") String userid, @Field("nickname") String nickname, @Field("name") String name);

    //Full information about user
    @PUT("users/{userid}")
    Call<AllUsersInformation.UserInfo> putUser(@Path("userid") String userid, @Body AllUsersInformation.UserInfo user);

    @GET("users/{user}")
    Call<AllUsersInformation.UserInfo> getUser(@Path("user") String user);

}
