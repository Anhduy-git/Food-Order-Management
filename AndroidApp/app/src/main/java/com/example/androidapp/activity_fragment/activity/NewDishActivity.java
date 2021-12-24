package com.example.androidapp.activity_fragment.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.androidapp.R;
import com.example.androidapp.data.ImageConverter;

public class NewDishActivity extends AppCompatActivity {

    public static final String EXTRA_DISH_NAME =
            "com.example.androidapp.EXTRA_DISH_NAME";
    public static final String EXTRA_DISH_PRICE =
            "com.example.androidapp.EXTRA_DISH_PRICE";
    public static final String EXTRA_DISH_IMAGE =
            "com.example.androidapp.EXTRA_DISH_IMAGE";

    private final int GALLERY_REQUEST = 1;
    private final int CAMERA_REQUEST = 2;
    private final int IMAGE_SIZE = 800;


    private ImageView imageView;
    private EditText edtDishName;
    private EditText edtDishPrice;
    private Button btnAddDish;
    private Button btnBack;
    private Button btnAddImage;
    private boolean changeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish);

        //Set default changeImg is false
        changeImg = false;

        initUi();

        btnAddDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkRequestPermission()) {
                    takePictureFromCamera();
                }
            }
        });

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureFromGallery();
            }
        });
    }

    private void initUi () {
        imageView = findViewById(R.id.dish_pic_view);
        edtDishName = findViewById(R.id.add_dish_name_text);
        edtDishPrice = findViewById(R.id.add_dish_price_text);
        btnAddDish = findViewById(R.id.add_dish_info_button);
        btnBack = findViewById(R.id.btn_back);
        btnAddImage = findViewById(R.id.btn_add_dish_image);
    }

    //Add dish to database
    private void addDish() {
        String strDishName = edtDishName.getText().toString().trim();
        String strDishPrice = edtDishPrice.getText().toString().trim();

        //Check if fields are empty, if so then don't add to database
        if (TextUtils.isEmpty(strDishName) || TextUtils.isEmpty(strDishPrice)) {
            Toast.makeText(this, "Xin hãy điền tên và giá", Toast.LENGTH_SHORT).show();
            return;
        }
        //confirm sound
        final MediaPlayer sound = MediaPlayer.create(this, R.raw.confirm_sound);
        //release resource when completed
        sound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                sound.release();
            }
        });
        sound.start();
        Intent data = new Intent();
        data.putExtra(EXTRA_DISH_NAME, strDishName);
        data.putExtra(EXTRA_DISH_PRICE, Integer.valueOf(strDishPrice));

        if (changeImg) {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            Bitmap image = ImageConverter.getResizedBitmap(bitmap, IMAGE_SIZE);
            data.putExtra(EXTRA_DISH_IMAGE, ImageConverter.convertImage2ByteArray(image));
        }

        setResult(RESULT_OK, data);
        finish();
    }

    private boolean checkRequestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int cameraPermission = ActivityCompat.checkSelfPermission(NewDishActivity.this,
                    Manifest.permission.CAMERA);
            if (cameraPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                        NewDishActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        CAMERA_REQUEST);
                return false;
            }
        }
        return true;
    }

    private void takePictureFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, GALLERY_REQUEST);
    }

    private void takePictureFromCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicture, CAMERA_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            try {
                //set changed Img
                changeImg = true;
                Uri selectedImage = data.getData();
                imageView.setImageURI(selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            try {
                //set changed Img
                changeImg = true;
                Bundle bundle = data.getExtras();
                Bitmap bitmapImage = (Bitmap) bundle.get("data");
                imageView.setImageBitmap(bitmapImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                takePictureFromCamera();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(NewDishActivity.this, "Chưa được cấp quyền", Toast.LENGTH_SHORT).show();
        }
    }

}
