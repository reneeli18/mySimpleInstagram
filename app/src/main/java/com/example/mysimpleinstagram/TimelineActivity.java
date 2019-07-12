package com.example.mysimpleinstagram;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mysimpleinstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends AppCompatActivity {

    ParseUser user;
    List<Post> posts = new ArrayList<>();
    RecyclerView rvPosts;
    PostAdapter postAdapter;
    private SwipeRefreshLayout swipeContainer;
    MenuItem miActionProgressItem;
    private final int REQUEST_CODE = 20;
    private EndlessRecyclerViewScrollListener scrollListener;
    //keeps track of the lowest max_id for the infinite scrolling
    private long maxId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        user = ParseUser.getCurrentUser();

        //find the RecyclerView
        rvPosts = (RecyclerView) findViewById(R.id.rvPosts);
        //init the arraylist (data source)
        posts = new ArrayList<>();
        //construct the adapter from this data source
        postAdapter = new PostAdapter(posts);
        //RecyclerView setup (layout manager, use adapter)
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        //set the adapter
        rvPosts.setAdapter(postAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPosts.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore ( int page, int totalItemsCount, RecyclerView view){
                fetchTimelineAsync(0, false);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvPosts.addOnScrollListener(scrollListener);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showProgressBar();
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

    private void loadTopPosts() {
        final Post.Query postQuery = new Post.Query();
        postQuery.getTop().withUser();

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
                    rvPosts.scrollToPosition(0);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        //TODO - check out whether it's pbProgressAction or miProgresAction
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        if (miActionProgressItem == null) {
            return;
        }
        miActionProgressItem.setVisible(false);
    }

    private void populateTimeline() {
        loadTopPosts();
        postAdapter.notifyItemInserted(posts.size() - 1);
        hideProgressBar();
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
}
