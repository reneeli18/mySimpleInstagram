package com.example.mysimpleinstagram;

import android.app.Application;

import com.example.mysimpleinstagram.model.Post;
import com.parse.Parse;
import com.parse.ParseObject;


public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("mySimpleInstagram")
                .clientKey("myMasterKey_ReneeLi68")
                .server("http://reneeli-fbu-instagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
