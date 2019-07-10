package com.example.mysimpleinstagram.fragments;

import android.util.Log;

import com.example.mysimpleinstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment {

    @Override
    protected void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();
        postQuery.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        postQuery.addDescendingOrder(Post.KEY_CREATED_AT);
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); ++i) {
                        Log.d("HomeActivity", "Post[" + i + "] = "
                                + objects.get(i).getDescription()
                                + "\nusername = " + objects.get(i).getUser().getUsername());
                    }
                    posts.addAll(objects);
                    postAdapter.notifyItemInserted(0);
                    //rvPosts.scrollToPosition(0);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}
