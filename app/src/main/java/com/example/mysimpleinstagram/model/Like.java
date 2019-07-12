package com.example.mysimpleinstagram.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Like")
public class Like extends ParseObject {
    public static final String KEY_USER = "user";

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    //ParseQuery<Like> p = new ParseQuery<Like>(Like.class).include("user");

    public static class Query extends ParseQuery<Like> {
        public Query() {
            super(Like.class);
        }

        public Like.Query withUser() {
            include("user");
            return this;
        }
    }
}
