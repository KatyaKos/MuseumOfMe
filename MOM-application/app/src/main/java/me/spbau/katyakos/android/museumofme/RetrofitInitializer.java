package me.spbau.katyakos.android.museumofme;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitInitializer {

    private static RetrofitInitializer instance = null;
    private UserDataAPI API;

    private RetrofitInitializer() {
        API = new Retrofit.Builder()
                .baseUrl("http://172.23.8.52:8080/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserDataAPI.class);
    }

    static RetrofitInitializer getInstance() {
        if (instance == null) {
            instance = new RetrofitInitializer();
        }
        return instance;
    }

    UserDataAPI getAPI() {
        return API;
    }

}
