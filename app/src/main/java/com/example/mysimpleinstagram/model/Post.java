package com.example.mysimpleinstagram.model;

import android.text.format.DateUtils;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_MEDIA = "media";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_PROFILE_IMAGE_URL = "profilePic";
    public static final String KEY_LIKED = "likes";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getMedia() { return getParseFile(KEY_MEDIA); }

    public void setMedia(ParseFile media) {
        put(KEY_MEDIA, media);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public ParseFile getProfileImageUrl() { return getUser().getParseFile(KEY_PROFILE_IMAGE_URL); }

    public void setProfileImageUrl(ParseFile proPic) { put(KEY_PROFILE_IMAGE_URL, proPic);}

    public boolean getLikeStatus(ParseUser user) throws ParseException { return !getRelation("likes").getQuery().whereEqualTo("user", user).find().isEmpty(); }

    public static class Query extends ParseQuery<Post> {
        public Query() {
            super(Post.class);
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

        public Query withUser() {
            include("user");

            return this;
        }

    }

    public String getRelativeTimeAgo(Date date) {
        String relativeDate = "";
        long dateMillis = date.getTime();
        relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

        return relativeDate;
    }
}
