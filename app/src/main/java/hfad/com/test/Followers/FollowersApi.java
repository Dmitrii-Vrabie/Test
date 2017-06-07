package hfad.com.test.Followers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
interface FollowersApi {
    @GET("/appstore/test.php")
    Call<FollowersListGSON> getFollowers(@Query("mode") String mode);
}
