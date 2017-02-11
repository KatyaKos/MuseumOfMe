package me.spbau.katyakos.android.museumofme;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

interface UserDataAPI {

    @GET("usersAuth")
    Call<List<AllUsersInformation.UsersAuth>> getUsersAuth();

    @GET("usersBasic")
    Call<List<AllUsersInformation.UsersBasicInfo>> getUsersBasic();

    //update and create user
    @PUT("users/{userid}")
    Call<AllUsersInformation.UserInfo> putUser(@Path("userid") String userid, @Body AllUsersInformation.UserInfo user);

    //get user
    @GET("users/{user}")
    Call<AllUsersInformation.UserInfo> getUser(@Path("user") String user);

}
