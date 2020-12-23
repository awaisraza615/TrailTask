package com.awaisraza.app.Api;

import com.awaisraza.app.Models.Post;
import com.awaisraza.app.Models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonPlaceholderApi {

    @GET("users")
    Call<List<User>> getUsers();

    @GET("/users/{id}")
    Call<User> getUserById(@Path("id") int id);

    @GET("posts")
    Call<List<Post>> getPosts();

}