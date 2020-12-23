package com.awaisraza.app.Activities;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.awaisraza.app.Api.JsonPlaceholderApi;
import com.awaisraza.app.Models.User;
import com.awaisraza.app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivityPost extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap,mGoogleMap;
    ImageView imageView;


    TextView txtname,txtusername,txtemail,txtphone,txtwebsite,txtcompany,txtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_post);

        imageView = findViewById(R.id.menuback);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        String uid = bundle.getString("uid");

        txtname=findViewById(R.id.txtname);
        txtusername=findViewById(R.id.txtusername);
        txtemail=findViewById(R.id.txtemail);
        txtphone=findViewById(R.id.txtphone);
        txtwebsite=findViewById(R.id.txtwebsite);
        txtcompany=findViewById(R.id.txtcompany);
        txtAddress=findViewById(R.id.txtAddress);
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
                    Log.d("useridcheck", "onResponse: "+user.getId().toString()+"uid"+uid);
                    if (user.getId().toString().equals(uid))
                    {

                    /*    String id;
                        String Name;
                        String Username;
                        String Email;
                        String address;
                        String Phone;
                        String Website;
                        String Company;
                        id = user.getId().toString();
                        Name = user.getName();
                        Username = user.getUsername();
                        Email = user.getEmail();
                        address = user.getAddress().toString();
                        Phone = user.getPhone();
                        Website = user.getWebsite();
                        Company = user.getCompany().toString();*/


                       // Log.d("values", "onResponse: "+id+Name+Username+Email+address+Phone+Website+Company);

                        txtname.setText("Name : "+user.getName());
                        txtusername.setText("Username : "+user.getUsername());
                        txtemail.setText("Email : "+user.getEmail());
                        txtAddress.setText("Address : "+user.getAddress().getStreet()+"\n"+user.getAddress().getSuite()+" "+user.getAddress().getCity()+" "+user.getAddress().getZipcode());
                        txtphone.setText("Phone : "+user.getPhone());
                        txtwebsite.setText("Website : "+user.getWebsite());
                        txtcompany.setText("Company : "+user.getCompany().getName()+"\n"+user.getCompany().getCatchPhrase()+"\n"+user.getCompany().getBs());





                        LatLng latLng = new LatLng(Double.parseDouble(user.getAddress().getGeo().getLat()), Double.parseDouble(user.getAddress().getGeo().getLat()));
                        CameraPosition position= new  CameraPosition.Builder().
                                target(latLng).zoom(17).bearing(19).tilt(30).build();
                        //_googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));

                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
                        mMap.addMarker(new
                                MarkerOptions().position(latLng).title("start"));


                    }


                }


            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
            }
        });

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in User Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }
}