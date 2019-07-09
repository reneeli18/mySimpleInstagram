package com.example.mysimpleinstagram;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysimpleinstagram.model.Post;

import org.parceler.Parcels;

public class PostDetailsActivity extends AppCompatActivity {

    Post post;

    //the view objects
    TextView tvUsername;
    TextView tvDescription;
    TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        tvUsername = (TextView) findViewById(R.id.tvUserName);
        tvDescription = (TextView) findViewById(R.id.tvBody);
        tvDate = (TextView) findViewById(R.id.tvTimestamp);
        // unwrap the movie passed in via intent, using its simple name as a key
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        Log.d("PostDetailsActivity", String.format("Showing details for '%s'", post.getUser()));

        //set title and overview
        tvUsername.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());

        tvDate.setText(post.getCreatedAt().toString());
    }
}
