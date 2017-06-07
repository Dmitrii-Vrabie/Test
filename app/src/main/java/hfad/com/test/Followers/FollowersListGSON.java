package hfad.com.test.Followers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

class FollowersListGSON {

    @SerializedName("page")
    @Expose
    private Object page;
    @SerializedName("response")
    @Expose
    private List<FollowerGSON> response = null;

    public Object getPage() {
        return page;
    }

    public void setPage(Object page) {
        this.page = page;
    }

    public List<FollowerGSON> getResponse() {
        return response;
    }

    public void setResponse(List<FollowerGSON> response) {
        this.response = response;
    }

}
