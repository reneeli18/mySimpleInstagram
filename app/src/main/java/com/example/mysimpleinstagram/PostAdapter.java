package com.example.mysimpleinstagram;

import android.content.Context;
import android.content.Intent;
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

import org.parceler.Parcels;

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

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTimestamp;
        public ImageView ivImage;
        public TextView tvUser;
        public ImageView ivLike;

        public ViewHolder(View itemView) {
            super(itemView);

            //perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvUser = (TextView) itemView.findViewById(R.id.tvUser);
            ivLike = (ImageView) itemView.findViewById(R.id.ivLike);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // gets item position
                    int position = getAdapterPosition();
                    // make sure the position is valid, i.e. actually exists in the view
                    if (position != RecyclerView.NO_POSITION) {
                        // get the post at the position, this won't work if the class is static
                        Post post = mPosts.get(position);
                        // create intent for the new activity
                        Intent intent = new Intent(context, PostDetailsActivity.class);
                        // serialize the post using parceler, use its short name as a key
                        intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                        // show the activity
                        context.startActivity(intent);
                    }
                }
            });
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
