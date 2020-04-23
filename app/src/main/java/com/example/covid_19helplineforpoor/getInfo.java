package com.example.covid_19helplineforpoor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class getInfo extends AppCompatActivity {

    Details details;
    DatabaseReference ref;

    public void submit(View view){
        String name=((EditText)findViewById(R.id.Name)).getText().toString();
        int num=Integer.parseInt(((EditText)findViewById(R.id.number)).getText().toString());
        String address=((EditText)findViewById(R.id.address)).getText().toString();
        String area=((EditText)findViewById(R.id.area)).getText().toString();
        int pin=Integer.parseInt(((EditText)findViewById(R.id.pin)).getText().toString());
        String district=((EditText)findViewById(R.id.district)).getText().toString();
        String state=((EditText)findViewById(R.id.state)).getText().toString();
        //Add image and GPS
        details.setName(name.trim());
        details.setNum(num);
        details.setAddress(address.trim());

        //Area, District and State are saved in lowercase to avoid faulty comparisons
        //When retrieving data, compare after converting to lower case

        details.setArea(area.toLowerCase().trim());
        details.setPin(pin);
        details.setDistrict(district.toLowerCase().trim());
        details.setState(state.toLowerCase().trim());
        ref.push().setValue(details);
        Toast.makeText(getApplicationContext() , "data insertion was successful", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_info);
        details = new Details();
        ref= FirebaseDatabase.getInstance().getReference().child("Details");//table name=Details
    }
}
