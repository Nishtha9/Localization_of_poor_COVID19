package com.example.covid_19helplineforpoor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    public void btnClicked(View view){
        if(view.getId()== R.id.getInfo)
        {
            //Edit the code to redirect to interface 2
            //Intent mass = new Intent(this,giveInfo.class);
            //startActivity(mass);
            Log.i("get info", " button was clicked");
        }
        else if (view.getId()== R.id.giveInfo)
        {
            Intent goToGetInfo = new Intent(this,getInfo.class);
            startActivity(goToGetInfo);
            Log.i("give info", " button was clicked");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
