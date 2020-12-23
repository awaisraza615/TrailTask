package com.awaisraza.app.Activities;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.awaisraza.app.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mGoogleMap;
    private FrameLayout frameLayout, circleFrameLayout;
    private ProgressBar progress;
    private TextView textView;
    private int circleRadius;
    private boolean isMoving = false;
    private SupportMapFragment mapFragment;
    private static final int REQUEST_LOCATION = 1;
    String latitude="25.381929";
    String longitude="68.373039";
    LocationManager locationManager;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "Latlong";
    TextView yourlocation;
    ImageView menuback,menuback2;

    String prefrencedata;
    TextView edittxtDropoff;

    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFrgmnt);
        mapFragment.getMapAsync(this);


        confirm = findViewById(R.id.btn_setpickup);

        menuback=findViewById(R.id.menuback);menuback2=findViewById(R.id.menuback2);
        menuback2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        yourlocation = findViewById(R.id.yourlocation);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(prefrencedata);
                Toast.makeText(MapsActivity.this, "Successfully Saved Data", Toast.LENGTH_SHORT).show();
            }
        });
        menuback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {
                    getLocation();
                }
            }
        });

        initViews();
    }
    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                yourlocation.setText("Your Location: Latitude: " + latitude + "Longitude: " + longitude);

                LatLng latLng = new LatLng(lat, longi);
                CameraPosition position= new  CameraPosition.Builder().
                        target(latLng).zoom(17).bearing(19).tilt(30).build();
                //_googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));

                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
                mGoogleMap.addMarker(new
                        MarkerOptions().position(latLng).title("start"));

            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void initViews() {
        frameLayout = findViewById(R.id.map_container);
        edittxtDropoff = findViewById(R.id.edittxt);
        circleFrameLayout = frameLayout.findViewById(R.id.pin_view_circle);
        textView = circleFrameLayout.findViewById(R.id.textView);
        progress = circleFrameLayout.findViewById(R.id.profile_loader);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFrgmnt);
        mapFragment.getMapAsync(this);

        try {
            loadData();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "no data "+ ex.getMessage(), Toast.LENGTH_SHORT).show();

        }


    }
    private void moveMapCamera() {
        if (mGoogleMap == null) {
            return;
        }



        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);


        mGoogleMap.moveCamera(center);
        mGoogleMap.animateCamera(zoom);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mGoogleMap.setOnCameraMoveStartedListener(i -> {
            isMoving = true;
            textView.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
            Drawable mDrawable;
            if (Build.VERSION.SDK_INT >= 21)
                mDrawable = getApplicationContext().getResources().getDrawable(R.drawable.circle_background_moving, null);
            else
                mDrawable = getApplicationContext().getResources().getDrawable(R.drawable.circle_background_moving);

            circleFrameLayout.setBackground(mDrawable);
            resizeLayout(false);

        });

        mGoogleMap.setOnCameraIdleListener(() -> {

            isMoving = false;
            textView.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
            resizeLayout(true);




            prefrencedata="lat is"+mGoogleMap.getCameraPosition().target.latitude+" lng is"+mGoogleMap.getCameraPosition().target.longitude;
            Toast.makeText(this, "lat is"+mGoogleMap.getCameraPosition().target.latitude+" lng is"+mGoogleMap.getCameraPosition().target.longitude, Toast.LENGTH_SHORT).show();
            Geocoder geocoder;
            List<Address> addresses;

            geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
            try {

                addresses = geocoder.getFromLocation(mGoogleMap.getCameraPosition().target.latitude, mGoogleMap.getCameraPosition().target.longitude, 1);

                String addressLine1 = addresses.get(0).getAddressLine(0);
                Log.e("line1", addressLine1);

                String city = addresses.get(0).getLocality();
                Log.e("city", city);

                String state = addresses.get(0).getAdminArea();
                Log.e("state", state);

                //  String pinCode = addresses.get(0).getPostalCode();
                //  Log.e("pinCode", pinCode);

                String fullAddress = addressLine1 + ",  " + city + ",  " + state + ",  ";
                Toast.makeText(this, "Address is : "+fullAddress, Toast.LENGTH_SHORT).show();
                prefrencedata+="Address is : "+fullAddress;
                edittxtDropoff.setText(fullAddress);




            } catch (IOException e) {
                e.printStackTrace();
                Log.e("MainActivity", e.getMessage());
            }

            new Handler().postDelayed(() -> {

                Drawable mDrawable;
                if (Build.VERSION.SDK_INT >= 21)
                    mDrawable = getApplicationContext().getResources().getDrawable(R.drawable.circle_background, null);
                else
                    mDrawable = getApplicationContext().getResources().getDrawable(R.drawable.circle_background);

                if (!isMoving) {
                    circleFrameLayout.setBackground(mDrawable);
                    textView.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);

                }
            }, 1500);
        });

        MapsInitializer.initialize(this);
        moveMapCamera();
    }
    public void saveData(String Latlng) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT,Latlng );
        editor.apply();
    }
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(TEXT, "");
        Toast.makeText(this, "Your shared preference data is "+"\n"+text, Toast.LENGTH_LONG).show();
    }
    private void resizeLayout(boolean backToNormalSize) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) circleFrameLayout.getLayoutParams();

        ViewTreeObserver vto = circleFrameLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                circleFrameLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                circleRadius = circleFrameLayout.getMeasuredWidth();
            }
        });

        if (backToNormalSize) {
            params.width = WRAP_CONTENT;
            params.height = WRAP_CONTENT;
            params.topMargin = 0;

        } else {
            params.topMargin = (int) (circleRadius * 0.3);
            params.height = circleRadius - circleRadius / 3;
            params.width = circleRadius - circleRadius / 3;
        }

        circleFrameLayout.setLayoutParams(params);
    }

}