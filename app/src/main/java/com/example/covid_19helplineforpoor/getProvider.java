package com.example.covid_19helplineforpoor;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import pub.devrel.easypermissions.EasyPermissions;

public class getProvider extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ProviderInfo PI;
    Calendar myCalendar = Calendar.getInstance();

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // this.editText.setText();
        System.out.println("-------------------------OnDateSet Ran----------------");
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String myFormat = "dd/mm/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        ((TextView)findViewById(R.id.editText3)).setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_provider);

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);


        findViewById(R.id.editText3).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(v.getContext(),new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        Log.d("Success", "Date changed.");
                        ((TextView)findViewById(R.id.editText3)).setText(dayOfMonth+"/"+monthOfYear+"/"+year);
                    }
                },year,month,day);
                dialog.show();

            }
        });
    }

    public void save(View view)
    {
        PI=new ProviderInfo();
        String name=((EditText)findViewById(R.id.editText)).getText().toString();
        String date=((EditText)findViewById(R.id.editText3)).getText().toString();
        if (name.equals("")|| date.equals("") || ((ImageView)findViewById(R.id.imageView2)).getTag().equals(null))
        {
            Toast.makeText(getApplicationContext() , "Please fill up all required fields", Toast.LENGTH_LONG).show();
        }
        else
            {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Providers");

            EditText d = (EditText) findViewById(R.id.editText);
            PI.setName(name);
            PI.setDate(date);
            DatabaseReference pushRef=databaseReference.push();
            pushRef.setValue(PI);
            String key=pushRef.getKey();
            Toast.makeText(getApplicationContext() , "data insertion was successful", Toast.LENGTH_LONG).show();

            StorageReference  mStorageRef = FirebaseStorage.getInstance().getReference();

            ImageView imageView=(ImageView)findViewById(R.id.imageView2);
            if (imageView.getTag()!=null)
            {
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                StorageReference imageRef = mStorageRef.child(key);
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
                    }
                    });
            }
            }
    }

    public void imgUpload(View v)
    {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 3000);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3000 && resultCode == RESULT_OK && null != data) {
            if ((ActivityCompat.checkSelfPermission(getProvider.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    || ActivityCompat.checkSelfPermission(getProvider.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getProvider.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 2000);
            }

           /* if (EasyPermissions.hasPermissions(this, galleryPermissions)) {

            } else {
                EasyPermissions.requestPermissions(this, "Access for storage",
                        101, galleryPermissions);
            }*/

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            try {
                ImageView imageView = (ImageView) findViewById(R.id.imageView2);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                System.out.println(picturePath);
            }catch(Exception e)
            {
                e.printStackTrace();
            }

        }
    }
}
