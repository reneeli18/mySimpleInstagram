package com.example.mysimpleinstagram.fragments;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysimpleinstagram.MainActivity;
import com.example.mysimpleinstagram.R;
import com.example.mysimpleinstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment {

    @Override
    protected void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.whereLessThan(Post.KEY_CREATED_AT, maxDate);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.action_logout) {
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();
            final Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            AppCompatActivity appAct = (AppCompatActivity) getActivity();
            appAct.finish();
//        } else if (id == R.id.change_profile_picture) {
//            Intent intent=new Intent(getApplicationContext(), ChangeProfilePicture.class);
//            startActivity(intent);
//            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
