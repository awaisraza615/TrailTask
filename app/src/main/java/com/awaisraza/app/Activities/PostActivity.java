package com.awaisraza.app.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.awaisraza.app.Api.JsonPlaceholderApi;
import com.awaisraza.app.Models.Post;
import com.awaisraza.app.Adapter.MyPostAdapter;
import com.awaisraza.app.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Integer     userId;
    Integer     id;
    String      title;
    String      body;
    String content = "";
    ArrayList<Post> vehtypes;
    Post vehtypess;
    ImageView menuback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        String uid = bundle.getString("uid");
        menuback=findViewById(R.id.menuback);
        menuback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView=findViewById(R.id.recyclerview);
        vehtypes = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceholderApi jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi.class);

        Call<List<Post>> call = jsonPlaceholderApi.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                List<Post> posts = response.body();
                Log.d("responsebody", "onResponse: " + response);
                for (Post post : posts) {
                    Log.d("users", "onResponse: " + post);

                    String id= post.getUserId();

                    Log.d("asasd", "onResponse: "+id+"uid"+uid);
                   if (post.getUserId().trim().equals(uid))
                   {
                        id= post.getUserId();
                       String pid = post.getId();
                       String ptitle= post.getTitle();
                       String pbody= post.getBody();



                       vehtypess = new Post(id,pid,ptitle,pbody);
                       vehtypes.add(vehtypess);

                       recyclerView = (RecyclerView) findViewById(R.id.recyclerviewp);
                       MyPostAdapter recyclerViewAdapter = new MyPostAdapter(vehtypes,PostActivity.this);
                       recyclerView.setLayoutManager(new LinearLayoutManager(PostActivity.this));
                       recyclerView.setAdapter(recyclerViewAdapter);
                   }



                 //   Log.d("Datais", "onResponse: " + id + "" + Name + "" + Username + "" + Email + "" + Address + "" + Phone + "" + "" + Website + "" + Company + "");



                }
                Log.d("conteets", "onResponse: "+content);

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });

    }


}