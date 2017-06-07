package hfad.com.test.Users;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface UserAPI {
    @GET("/appstore/test.php")
    Call<UsersListGSON> getUsers(@Query("mode") String mode, @Query("page")int page);
}
