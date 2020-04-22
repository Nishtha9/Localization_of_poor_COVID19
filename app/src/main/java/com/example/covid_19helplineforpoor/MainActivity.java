package com.example.covid_19helplineforpoor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    public void btnClicked(View view){
        String button_text;
        button_text =((Button)view).getText().toString();
        if(button_text.equals("GET INFORMATION ABOUT POOR FAMILIES"))
        {
            Intent goToGetInfo = new Intent(this,getInfo.class);
            startActivity(goToGetInfo);
        }
        else if (button_text.equals("Give Information about poor family"))
        {
            //Intent mass = new Intent(this,giveInfo.class);
            //startActivity(mass);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
