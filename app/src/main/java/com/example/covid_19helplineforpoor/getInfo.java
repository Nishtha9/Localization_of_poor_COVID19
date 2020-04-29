package com.example.covid_19helplineforpoor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class getInfo extends AppCompatActivity {

    Details details;
    DatabaseReference ref;
    Double lat,lng;

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


        if (lat!=null && lng!=null)
        {
            System.out.println("4lat:"+lat+"lng:"+lng);
            details.setLatitude(lat);
            details.setLongitude(lng);
        }
        details.setArea(area.toLowerCase().trim());
        details.setPin(pin);
        details.setDistrict(district.toLowerCase().trim());
        details.setState(state.toLowerCase().trim());
        ref.push().setValue(details);
        Toast.makeText(getApplicationContext() , "data insertion was successful", Toast.LENGTH_LONG).show();

    }

    public void reset(View view)
    {
        Log.i("reset", " reset should be performed");
        ((EditText)findViewById(R.id.Name)).setText("");
        ((EditText)findViewById(R.id.number)).setText("");
        ((EditText)findViewById(R.id.address)).setText("");
        ((EditText)findViewById(R.id.pin)).setText("");
        ((EditText)findViewById(R.id.district)).setText("");
        ((EditText)findViewById(R.id.state)).setText("");
        ((EditText)findViewById(R.id.area)).setText("");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        lat = data.getDoubleExtra("latitude",28.632429);
        lng = data.getDoubleExtra("longitude",77.218788);
        System.out.println("3lat: "+lat+",lng:"+lng);
    }

    public void openMap(View view){
            Intent MAP = new Intent(this,MapsActivity.class);
        String area=((EditText)findViewById(R.id.area)).getText().toString();
        String pin=((EditText)findViewById(R.id.pin)).getText().toString();
        String district=((EditText)findViewById(R.id.district)).getText().toString();
            MAP.putExtra("address values",area+" "+district+" "+pin);
            startActivityForResult(MAP,1000);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_info);
        details = new Details();
        ref= FirebaseDatabase.getInstance().getReference().child("Details");//table name=Details

    }
}
