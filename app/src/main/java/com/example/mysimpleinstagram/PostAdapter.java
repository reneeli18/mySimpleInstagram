package com.example.mysimpleinstagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mysimpleinstagram.model.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> mPosts;
    Context context;
    //pass in the posts array in the constructor
    public PostAdapter(List<Post> posts) {
        mPosts = posts;
    }

    //for each row, inflate the layout and cache references into ViewHolder

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    //bind the values based on the position of the element

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //get the data according to position
        Post post = mPosts.get(position);
        ParseUser user = post.getUser();

        //populate the views according to this data
        holder.tvUsername.setText(post.getUser().getUsername());
        holder.tvBody.setText(post.getDescription());

        holder.tvUser.setText(post.getUser().getUsername());

        String relativeDate = post.getRelativeTimeAgo(post.getCreatedAt());
        holder.tvTimestamp.setText(relativeDate);

        int placeholderId = R.drawable.icon;

        ParseFile proPic = post.getMedia();
        if (proPic != null) {
            Glide.with(context)
                    .load(post.getProfileImageUrl().getUrl())
                    .apply(new RequestOptions().placeholder(placeholderId)
                            .error(placeholderId))
                    .into(holder.ivProfileImage);
        }

        ParseFile media = post.getMedia();
        if (media != null) {
            Glide.with(context)
                    .load(media.getUrl())
                    .apply(new RequestOptions().placeholder(placeholderId)
                            .error(placeholderId))
                    .into(holder.ivImage);
        }
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    //create ViewHolder class

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTimestamp;
        public ImageView ivImage;
        public TextView tvUser;

        public ViewHolder(View itemView) {
            super(itemView);

            //perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvUser = (TextView) itemView.findViewById(R.id.tvUser);
        }
    }

    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }
}
