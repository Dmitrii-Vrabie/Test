package hfad.com.test;

import io.realm.RealmObject;



public class Users extends RealmObject{
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

    int getLikes() {
        return likes;
    }

    void setLikes(int likes) {
        this.likes = likes;
    }
}
