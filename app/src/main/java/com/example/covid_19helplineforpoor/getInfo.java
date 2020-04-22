package com.example.covid_19helplineforpoor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class getInfo extends AppCompatActivity {

    public void submit(View view){
        String name=((EditText)findViewById(R.id.Name)).getText().toString();
        int num=Integer.parseInt(((EditText)findViewById(R.id.number)).getText().toString());
        String address=((EditText)findViewById(R.id.address)).getText().toString();
        String area=((EditText)findViewById(R.id.area)).getText().toString();
        int pin=Integer.parseInt(((EditText)findViewById(R.id.pin)).getText().toString());
        String district=((EditText)findViewById(R.id.district)).getText().toString();
        String state=((EditText)findViewById(R.id.state)).getText().toString();
        //Add image and GPS

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_info);
    }
}
