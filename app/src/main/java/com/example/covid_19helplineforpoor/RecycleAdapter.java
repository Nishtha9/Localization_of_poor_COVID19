package com.example.covid_19helplineforpoor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.app.Activity;

import java.util.HashMap;
import java.util.Locale;

public class RecycleAdapter  extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    private String[] keys ;

    private String[] titles ;

    private String[] details ;

    private double[] lat;

    private double[] lng;

    RecycleAdapter(String[] keys,String[] title, String[] details,double lat[],double lng[])
    {
        this.keys=keys;
        this.titles=title;
        this.details=details;
        this.lat=lat;
        this.lng=lng;
    }

class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemDetail;
        public Button mapBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage =
                    (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle =
                    (TextView)itemView.findViewById(R.id.item_title);
            itemDetail =
                    (TextView)itemView.findViewById(R.id.item_detail);
            mapBtn=
                    (Button)itemView.findViewById(R.id.button4);
        }
    }

    @NonNull
    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.itemTitle.setText(titles[position]);
        holder.itemDetail.setText(details[position]);
        final double latitude=lat[position], longitude=lng[position];
        if (latitude==-1 || longitude==-1)
        {
            holder.mapBtn.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.mapBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude ;
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    v.getContext().startActivity(intent);

                }
            });
        }
        //Put image
        String imgPath = keys[position];
        FirebaseStorage mStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = mStorage.getReference(imgPath);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(holder.itemImage.getContext())
                        .load(uri)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)         //ALL or NONE as your requirement
                        .thumbnail(Glide.with(holder.itemView.getContext()).load(R.drawable.noimg))
                        .error(R.drawable.noimg)
                        .into(holder.itemImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                System.out.println("Image failed");
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
