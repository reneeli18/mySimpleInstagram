package com.example.mysimpleinstagram;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mysimpleinstagram.model.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class PostDetailsActivity extends AppCompatActivity {

    Post post;

    //the view objects
    public ImageView ivProfileImage;
    public TextView tvUsername;
    public TextView tvBody;
    public TextView tvTimestamp;
    public ImageView ivImage;
    public TextView tvUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvUsername = (TextView) findViewById(R.id.tvUserName);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvTimestamp = (TextView) findViewById(R.id.tvTimestamp);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        tvUser = (TextView) findViewById(R.id.tvUser);
        // unwrap the post passed in via intent, using its simple name as a key
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        Log.d("PostDetailsActivity", String.format("Showing details for '%s'", post.getUser()));

        tvUsername.setText(post.getUser().getUsername());
        tvBody.setText(post.getDescription());

        tvUser.setText(post.getUser().getUsername());

        String relativeDate = post.getRelativeTimeAgo(post.getCreatedAt());
        tvTimestamp.setText(relativeDate);

        int placeholderId = R.drawable.icon;

        ParseFile proPic = post.getMedia();
        if (proPic != null) {
            Glide.with(this)
                    .load(post.getProfileImageUrl().getUrl())
                    .apply(new RequestOptions().placeholder(placeholderId)
                            .error(placeholderId))
                    .into(ivProfileImage);
        }

        ParseFile media = post.getMedia();
        if (media != null) {
            Glide.with(this)
                    .load(media.getUrl())
                    .apply(new RequestOptions().placeholder(placeholderId)
                            .error(placeholderId))
                    .into(ivImage);
        }
    }
}
