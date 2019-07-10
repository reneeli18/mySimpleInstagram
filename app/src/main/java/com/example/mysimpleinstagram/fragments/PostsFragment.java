package com.example.mysimpleinstagram.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mysimpleinstagram.EndlessRecyclerViewScrollListener;
import com.example.mysimpleinstagram.HomeActivity;
import com.example.mysimpleinstagram.PostAdapter;
import com.example.mysimpleinstagram.R;
import com.example.mysimpleinstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {

    protected RecyclerView rvPosts;
    protected PostAdapter postAdapter;
    ParseUser user;
    protected List<Post> posts = new ArrayList<>();
    private SwipeRefreshLayout swipeContainer;
    MenuItem miActionProgressItem;
    private final int REQUEST_CODE = 20;
    private EndlessRecyclerViewScrollListener scrollListener;
    //keeps track of the lowest max_id for the infinite scrolling
    private long maxId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvPosts = view.findViewById(R.id.rvPosts);

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        user = ParseUser.getCurrentUser();

        //find the RecyclerView
        rvPosts = (RecyclerView) view.findViewById(R.id.rvPosts);
        //init the arraylist (data source)
        posts = new ArrayList<>();
        //construct the adapter from this data source
        postAdapter = new PostAdapter(posts);
        //RecyclerView setup (layout manager, use adapter)
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        //set the adapter
        rvPosts.setAdapter(postAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore ( int page, int totalItemsCount, RecyclerView view){
                fetchTimelineAsync(page, false);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvPosts.addOnScrollListener(scrollListener);

        AppCompatActivity appAct = (AppCompatActivity) getActivity();

        appAct.getSupportActionBar().setHomeButtonEnabled(true);
        appAct.getSupportActionBar().setDisplayShowHomeEnabled(true);
        appAct.getSupportActionBar().setDisplayUseLogoEnabled(true);
        appAct.getSupportActionBar().setLogo(R.drawable.nav_logo_whiteout);
        appAct.getSupportActionBar().setDisplayShowTitleEnabled(true);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                //showProgressBar();
                fetchTimelineAsync(0, true);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        populateTimeline();
    }

    protected void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();
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
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        //TODO - check out whether it's pbProgressAction or miProgresAction
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        //ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        // Return to finish
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }

    public void onComposeAction(MenuItem mi) {
        //showProgressBar();
        Intent i = new Intent(getContext(), HomeActivity.class);
        startActivityForResult(i, REQUEST_CODE);
        //hideProgressBar();
    }

    private void populateTimeline() {
        loadTopPosts();
        postAdapter.notifyItemInserted(posts.size() - 1);
        //hideProgressBar();
    }

    public void fetchTimelineAsync(int page, boolean isRefreshed) {
        if (!isRefreshed) {
            maxId = posts.get(posts.size() - 1).hashCode();
        } else {
            postAdapter.clear();
            maxId = -1;
        }
        // ...the data has come back, add new items to your adapter...
        populateTimeline();
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);
    }

//    private void logout(String username, String password) {
//        ParseUser.logOut();
//        ParseUser currentUser = ParseUser.getCurrentUser();
//        final Intent intent = new Intent(getContext(), MainActivity.class);
//        startActivity(intent);
//        finish();
//    }
}
