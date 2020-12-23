package com.awaisraza.app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.awaisraza.app.Activities.MapsActivityPost;
import com.awaisraza.app.Models.Post;
import com.awaisraza.app.R;

import java.util.List;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.MyPostAdapterHolder> {

    private List<Post> posts;
    private Context context;

    public MyPostAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public MyPostAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new MyPostAdapter.MyPostAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostAdapterHolder holder, int position) {

            holder.txtuserid.setText("User id : "+posts.get(position).getUserId());
            String id = posts.get(position).getUserId();
            holder.txtpostid.setText("Post id : "+posts.get(position).getId());
            holder.txtposttitle.setText("Post Title : "+posts.get(position).getTitle());
            holder.txtpostbody.setText("Post Body : "+posts.get(position).getBody());
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MapsActivityPost.class);
                    intent.putExtra("uid",posts.get(position).getUserId());
                    context.startActivity(intent);
                }
            });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class MyPostAdapterHolder extends RecyclerView.ViewHolder {

        TextView txtuserid,txtpostid,txtposttitle,txtpostbody;

        LinearLayout linearLayout;

        public MyPostAdapterHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout=itemView.findViewById(R.id.layoutlay);
            txtuserid=itemView.findViewById(R.id.txtuser1d);
            txtpostid=itemView.findViewById(R.id.txtpostid);
            txtposttitle=itemView.findViewById(R.id.txtposttitle);
            txtpostbody=itemView.findViewById(R.id.txtpostbody);

        }
    }
}
