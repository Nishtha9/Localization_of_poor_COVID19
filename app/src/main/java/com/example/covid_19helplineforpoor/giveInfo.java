package com.example.covid_19helplineforpoor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_info);

        Button btn = (Button) findViewById(R.id.search);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref = FirebaseDatabase.getInstance().getReference().child("Details");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String state= ((EditText) findViewById(R.id.state)).getText().toString();
                        final String district= ((EditText) findViewById(R.id.district)).getText().toString();
                        String area= ((EditText) findViewById(R.id.area)).getText().toString();

                        Query q_State=ref.orderByChild("state").equalTo(state.toLowerCase());
                        q_State.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    if (district.isEmpty())
                                        ((TextView)findViewById(R.id.textView19)).append(ds.getValue().toString()+"\n");
                                    else {
                                        String dis = ((HashMap<String, String>) ds.getValue()).get("district");
                                        if (dis.equals(district.toLowerCase())) {
                                            ((TextView) findViewById(R.id.textView19)).append(ds.getValue().toString() + "\n");
                                        }
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
}
