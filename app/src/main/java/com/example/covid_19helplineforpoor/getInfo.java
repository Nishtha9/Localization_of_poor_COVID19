package com.example.covid_19helplineforpoor;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.security.PrivilegedExceptionAction;

import pub.devrel.easypermissions.EasyPermissions;

public class getInfo extends AppCompatActivity {

    Details details;
    DatabaseReference ref;
    Double lat,lng;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private StorageReference mStorageRef;

    public void submit(View view){
        String name=((EditText)findViewById(R.id.Name)).getText().toString();
        int num=Integer.parseInt(((EditText)findViewById(R.id.number)).getText().toString());
        String address=((EditText)findViewById(R.id.address)).getText().toString();
        String area=((EditText)findViewById(R.id.area)).getText().toString();
        int pin=Integer.parseInt(((EditText)findViewById(R.id.pin)).getText().toString());
        String district=((EditText)findViewById(R.id.district)).getText().toString();
        String state=((EditText)findViewById(R.id.state)).getText().toString();
        if (name.equals("")||address.equals("")||area.equals("")||district.equals("")||state.equals("")||num==0||pin==0)
            Toast.makeText(getApplicationContext() , "Please fill up all required fields", Toast.LENGTH_LONG).show();
        //Add image and GPS
        else
        {
            details.setName(name.trim());
            details.setNum(num);
            details.setAddress(address.trim());
            //Area, District and State are saved in lowercase to avoid faulty comparisons
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
            DatabaseReference pushRef=ref.push();
            pushRef.setValue(details);
            String pushId=pushRef.getKey();
           //ref.push().setValue(details);
            //Storing Image*/
            ImageView imageView=(ImageView)findViewById(R.id.imageView);
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            StorageReference imageRef = mStorageRef.child(pushId);
            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    System.out.println("Image upload NOT successful");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //System.out.println(taskSnapshot.getMetadata()); //contains file metadata such as size, content-type, etc.
                    System.out.println("Image upload successful");
                    // ...
                }
            });
            Toast.makeText(getApplicationContext() , "data insertion was successful", Toast.LENGTH_LONG).show();
        }

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
        if (requestCode == 2000 && resultCode == RESULT_OK && null != data) {
            if ((ActivityCompat.checkSelfPermission(getInfo.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            || ActivityCompat.checkSelfPermission(getInfo.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getInfo.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 2000);
            }

            if (EasyPermissions.hasPermissions(this, galleryPermissions)) {

            } else {
                EasyPermissions.requestPermissions(this, "Access for storage",
                        101, galleryPermissions);
            }

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            try {
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                System.out.println(picturePath);
            }catch(Exception e)
            {
               e.printStackTrace();
            }

        }
    else if (requestCode==1000) {
            lat = data.getDoubleExtra("latitude", 28.632429);
            lng = data.getDoubleExtra("longitude", 77.218788);
            System.out.println("3lat: " + lat + ",lng:" + lng);
        }
    }

    public void openMap(View view){
            Intent MAP = new Intent(this,MapsActivity.class);
        String area=((EditText)findViewById(R.id.area)).getText().toString();
        String pin=((EditText)findViewById(R.id.pin)).getText().toString();
        String district=((EditText)findViewById(R.id.district)).getText().toString();
        String address=((EditText)findViewById(R.id.address)).getText().toString();
            MAP.putExtra("address values",address+" "+area+" "+district+" "+pin);
            startActivityForResult(MAP,1000);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_info);
        details = new Details();
        ref= FirebaseDatabase.getInstance().getReference().child("Details");//table name=Details
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void chooseImg(View view){
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 2000);
    }
}
