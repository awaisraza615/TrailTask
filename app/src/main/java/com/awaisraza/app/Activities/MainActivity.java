package com.awaisraza.app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.awaisraza.app.Api.JsonPlaceholderApi;
import com.awaisraza.app.Models.User;
import com.awaisraza.app.Adapter.MyRecyclerAdapter;
import com.awaisraza.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String id;
    String Name;
    String Username;
    String Email;
    String Address;
    String Phone;
    String Website;
    String Company;

    ImageView menuback,menuback2;
    RecyclerView recyclerView;

    ArrayList<User> vehtypes;
    User vehtypess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        vehtypes = new ArrayList<>();
        menuback=findViewById(R.id.menuback);
        menuback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MapsActivity.class));

            }
        });
        menuback2=findViewById(R.id.menuback2);
        menuback2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,PostActivity.class));

            }
        });
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Sucess";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                        Log.d("msg", msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceholderApi jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi.class);

        Call<List<User>> call = jsonPlaceholderApi.getUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                List<User> users = response.body();
                Log.d("responsebody", "onResponse: " + response);
                for (User user : users) {
                    Log.d("users", "onResponse: " + user);
                    String content = "";

                    id = user.getId().toString();
                    Name = user.getName();
                    Username = user.getUsername();
                    Email = user.getEmail();
                    Address = user.getAddress().toString();
                    Phone = user.getPhone();
                    Website = user.getWebsite();
                    Company = user.getCompany().toString();


                    vehtypess = new User(Integer.parseInt(id),Name,Username,Email,user.getAddress(),Phone,Website,user.getCompany());
                    vehtypes.add(vehtypess);

                    recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                    MyRecyclerAdapter recyclerViewAdapter = new MyRecyclerAdapter(vehtypes,MainActivity.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(recyclerViewAdapter);


                    Log.d("Datais", "onResponse: " + id + "" + Name + "" + Username + "" + Email + "" + Address + "" + Phone + "" + "" + Website + "" + Company + "");



                }


            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
            }
        });

    }

}