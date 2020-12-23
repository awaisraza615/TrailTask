package com.awaisraza.app.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.awaisraza.app.Activities.PostActivity;
import com.awaisraza.app.Models.User;
import com.awaisraza.app.R;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyRecyclerAdapterHolder> {


    private List<User> users;
    private Context context;

    public MyRecyclerAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public MyRecyclerAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);

        return new MyRecyclerAdapter.MyRecyclerAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerAdapterHolder holder, int position) {

        holder.Name.setText("Name : "+users.get(position).getName());
        holder.phone.setText("Phone : "+users.get(position).getPhone());
        holder.company.setText(( "Company : "+users.get(position).getCompany().getName()));
        holder.imageViewPhotoFavr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog("Address : ",users.get(position).getAddress().getStreet()+"\n"+users.get(position).getAddress().getSuite()+"\n"+users.get(position).getAddress().getCity()+"\n"+users.get(position).getAddress().getZipcode());
            }
        });

        holder.btndirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lat=users.get(position).getAddress().getGeo().getLat();
                String lng=users.get(position).getAddress().getGeo().getLng();

                Log.d("maps", "onClick: "+lat+lng);

                Uri navigationIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lng);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);

            }
        });
        holder.btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id=users.get(position).getId().toString();
                                Intent mapIntent = new Intent(context, PostActivity.class);
                                mapIntent.putExtra("uid",id);
                context.startActivity(mapIntent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    void confirmDialog(String title,String body){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(body);
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
       /* builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });*/
        builder.create().show();
    }


    public class MyRecyclerAdapterHolder extends RecyclerView.ViewHolder {

        TextView Name, phone, company;
        ImageView imageViewPhotoFavr;
        ImageView btndirection,btnpost;


        public MyRecyclerAdapterHolder(@NonNull View itemView) {

            super(itemView);
            Name = itemView.findViewById(R.id.textName);
            phone = itemView.findViewById(R.id.textphone);
            company = itemView.findViewById(R.id.textCompany);
            btndirection = itemView.findViewById(R.id.btndirection);
            imageViewPhotoFavr=itemView.findViewById(R.id.imageViewPhotoFavr);
            btnpost=itemView.findViewById(R.id.btnpost);


        }
    }
}
