package hfad.com.test.Users;

import android.support.annotation.NonNull;

import java.util.List;

import hfad.com.test.Followers.Follower;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class User extends RealmObject implements Comparable<User> {
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

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @Override
    public int compareTo(@NonNull User o) {
        return o.getId().compareTo(id);
    }
}
