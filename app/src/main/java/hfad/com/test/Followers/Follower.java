package hfad.com.test.Followers;

import android.support.annotation.NonNull;

import hfad.com.test.Users.User;
import io.realm.RealmObject;

public class Follower extends RealmObject implements Comparable<User> {
    private String id;
    private String name;
    private int likes;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLikes() {
        return likes;
    }

    void setLikes(int likes) {
        this.likes = likes;
    }

    @Override
    public int compareTo(@NonNull User o) {
        return o.getId().compareTo(id);
    }

}
