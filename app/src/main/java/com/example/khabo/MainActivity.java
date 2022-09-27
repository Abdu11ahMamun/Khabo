package com.example.khabo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button signup, login;
    ImageView profileImage;
    EditText userName, email, phone, pass;

    private  static final int PICK_IMAGE_REQUEST=99;
    private Uri imagePath;
    private Bitmap imageToStore;

    DBHelper dbHelper;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        signup=findViewById(R.id.signUpBtn);
        login= findViewById(R.id.already_have_acc);
        profileImage=findViewById(R.id.Profile_image);
        userName=findViewById(R.id.userName);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.Phone);
        pass=findViewById(R.id.password);
        dbHelper= new DBHelper(this);



        profileImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                choseImage();
            }
        });

        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                storeImage();
            }
        });
    }

    private void choseImage() {
        try{
            Intent intent= new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);


        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!= null){
                imagePath= data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                profileImage.setImageBitmap(imageToStore);

            }

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }

    private void storeImage(){
        if(!userName.getText().toString().isEmpty() && !email.getText().toString().isEmpty()
        && !phone.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()
        && profileImage.getDrawable() !=null && imageToStore != null){
            dbHelper.storeData(new ModelClass(userName.getText().toString(), email.getText().toString(),
                    phone.getText().toString(), pass.getText().toString(), imageToStore));
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"Please full all the fields",Toast.LENGTH_SHORT).show();

        }
    }


}