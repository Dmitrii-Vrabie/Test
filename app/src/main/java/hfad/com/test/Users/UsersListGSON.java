package hfad.com.test.Users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UsersListGSON {
    @SerializedName("page")
    @Expose
    private String page;
    @SerializedName("response")
    @Expose
    private List<UserGSON> response = null;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<UserGSON> getResponse() {
        return response;
    }

    public void setResponse(List<UserGSON> response) {
        this.response = response;
    }

}
