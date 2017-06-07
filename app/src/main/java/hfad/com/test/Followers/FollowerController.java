package hfad.com.test.Followers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import hfad.com.test.Followers.FollowersApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FollowerController {
    private static final String BASE_URL = "https://www.nomadroot.com";
    public static FollowersApi getApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(FollowersApi.class);
    }
}
