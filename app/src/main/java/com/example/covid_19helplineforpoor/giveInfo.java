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
                System.out.println("trigger 1");
                ref = FirebaseDatabase.getInstance().getReference().child("Details");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        System.out.println("trigger 2");
                        String state= ((EditText) findViewById(R.id.state)).getText().toString();
                        final String district= ((EditText) findViewById(R.id.district)).getText().toString();
                        Query q_State=ref.orderByChild("state").equalTo(state.toLowerCase());
                        q_State.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                System.out.println("trigger 3");
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                    System.out.println("trigger 4");
                                    if ((!district.isEmpty() && ((HashMap<String, String>) ds.getValue()).get("district").equals(district.toLowerCase())) || (district.isEmpty()))
                                    {
                                        //Zoom, horiz scroll
                                        System.out.println("trigger 5");
                                        TextView target=(TextView)findViewById(R.id.info_text);
                                        HashMap hm=(HashMap)ds.getValue();
                                        target.setTextSize(16);
                                        target.append("\n");
                                        target.append("Name: "+hm.get("name")+"\n"+"Number of family members:"+hm.get("num")+"\n"
                                        + "Address:" + hm.get("address")+"\n"+"Pin Code:"+hm.get("pin"));
                                        target.append("\n");
                                        target.invalidate();
                                        target.requestLayout();

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
