package com.example.covid_19helplineforpoor;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class giveInfo extends AppCompatActivity {

    DatabaseReference ref;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_info);

        Button btn = (Button) findViewById(R.id.search);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // System.out.println("trigger 1");
                ref = FirebaseDatabase.getInstance().getReference().child("Details");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //System.out.println("trigger 2");
                        String state= ((EditText) findViewById(R.id.state)).getText().toString();
                        final String district= ((EditText) findViewById(R.id.district)).getText().toString();
                        Query q_State=ref.orderByChild("state").equalTo(state.toLowerCase());
                        q_State.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //System.out.println("trigger 3");
                                int count=(int)dataSnapshot.getChildrenCount();
                                String title[]=new String[count];
                                String details[]=new String[count];
                                int x=0;
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                   // System.out.println("trigger 4");
                                    if (x<count&& (!district.isEmpty() && ((HashMap<String, String>) ds.getValue()).get("district").equals(district.toLowerCase())) || (district.isEmpty()))
                                    {
                                        final HashMap hm=(HashMap)ds.getValue();
                                        title[x]="Name:"+hm.get("name");
                                        details[x]="Number of members:"+hm.get("num")+"\n"
                                            + "Address:" + hm.get("address")+"\n"+"Pin Code:"+hm.get("pin");
                                        x++;
                                        showData(title,details);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });


            }
        });
    }

    public void showData(String[] title, String[] details)
    {
        recyclerView = findViewById(R.id.my_recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecycleAdapter(title,details);
        recyclerView.setAdapter(adapter);

    }
}
