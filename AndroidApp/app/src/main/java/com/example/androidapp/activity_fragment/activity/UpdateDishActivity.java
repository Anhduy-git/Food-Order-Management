package com.example.androidapp.activity_fragment.activity;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.androidapp.R;
import com.example.androidapp.data.AppDatabase;
import com.example.androidapp.data.ImageConverter;
import com.example.androidapp.data.menudata.Dish;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class UpdateDishActivity extends AppCompatActivity {
    public static final String EXTRA_DISH_ID =
            "com.example.androidapp.EXTRA_DISH_ID";
    public static final String EXTRA_DISH_NAME =
            "com.example.androidapp.EXTRA_DISH_NAME";
    public static final String EXTRA_DISH_PRICE =
            "com.example.androidapp.EXTRA_DISH_PRICE";
    public static final String EXTRA_NEW_IMAGE =
            "com.example.androidapp.EXTRA_NEW_IMAGE";
    public static final String EXTRA_OLD_IMAGE =
            "com.example.androidapp.EXTRA_OLD_IMAGE";

    private final int GALLERY_REQUEST = 1;
    private final int CAMERA_REQUEST = 2;
    private final int IMAGE_SIZE = 500;

    private EditText editDishName;
    private EditText editDishPrice;
    private ImageView imageView;
    private Button btnBack;
    private Button btnUpdate;
    private Button btnAddImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_menu);
        initUi();
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_DISH_ID)) {

            editDishName.setText(intent.getStringExtra(EXTRA_DISH_NAME));
            editDishPrice.setText(String.valueOf(intent.getIntExtra(EXTRA_DISH_PRICE, 0)));
            try {
                //read path image from intent and display
                File f=new File(intent.getStringExtra(EXTRA_OLD_IMAGE));
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                imageView.setImageBitmap(b);
            }
            catch (FileNotFoundException e) {
                //display default image for dish
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rec_ava_dish_default);
                imageView.setImageBitmap(bitmap);
            }
        }
        //Check for update to show btn update
        checkUpdate();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureFromGallery();
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
    }

    private void initUi() {
        editDishName = findViewById(R.id.dish_name_info);
        editDishPrice = findViewById(R.id.dish_price_info);
        imageView = findViewById(R.id.dish_pic_view);
        btnBack = findViewById(R.id.btn_back);
        btnUpdate = findViewById(R.id.dish_update_button);
        btnAddImage = findViewById(R.id.button2);
    }

    private void updateDish() {
        String strDishName = editDishName.getText().toString().trim();
        String strDishPrice = editDishPrice.getText().toString().trim();

        //Check if fields are empty, if so then don't add to database
        if (TextUtils.isEmpty(strDishName) || TextUtils.isEmpty(strDishPrice)) {
            Toast.makeText(this, "Xin hãy nhập tên và ", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_DISH_NAME, strDishName);
        data.putExtra(EXTRA_DISH_PRICE, Integer.parseInt(strDishPrice));
        int id = getIntent().getIntExtra(EXTRA_DISH_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_DISH_ID, id);
        } else {
            Toast.makeText(UpdateDishActivity.this, "Cập nhật không hợp lệ !", Toast.LENGTH_SHORT).show();
            return;
        }
        Dish dishUpdate = new Dish(strDishName, Integer.parseInt(strDishPrice), "");
        dishUpdate.setDishID(id);
        if (checkDishAvailableForUpdate(dishUpdate)) {
            //delete the old image when update
            File oldImage = new File(getIntent().getStringExtra(EXTRA_OLD_IMAGE));
            boolean deleted = oldImage.delete();
            //save new image to file and get path
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            Bitmap image = ImageConverter.getResizedBitmap(bitmap, IMAGE_SIZE);
            String imageDir = saveToInternalStorage(image, strDishName + "-" +
                    strDishPrice);
            //release memory
            bitmap.recycle();
            image.recycle();

            //put new image path to intent
            data.putExtra(EXTRA_NEW_IMAGE, imageDir);

            setResult(RESULT_OK, data);

            //confirm sound
            final MediaPlayer sound = MediaPlayer.create(this, R.raw.confirm_sound);
            //release resource when completed
            sound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    sound.release();
                }
            });
            sound.start();

            finish();
        } else {
            Toast.makeText(UpdateDishActivity.this, "Món ăn đã tồn tại !", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean checkRequestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int cameraPermission = ActivityCompat.checkSelfPermission(UpdateDishActivity.this,
                    Manifest.permission.CAMERA);
            if (cameraPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                        UpdateDishActivity.this,
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
                //update image
                btnUpdate.setVisibility(View.VISIBLE);

                Uri selectedImage = data.getData();
                imageView.setImageURI(selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            try {
                //update image
                btnUpdate.setVisibility(View.VISIBLE);

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
            Toast.makeText(UpdateDishActivity.this, "Chưa được cấp quyền", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUpdate() {
        editDishName.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                btnUpdate.setVisibility(View.VISIBLE);
            }
        });

        editDishPrice.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                btnUpdate.setVisibility(View.VISIBLE);
            }
        });

    }
    private String saveToInternalStorage(Bitmap bitmapImage, String fileName){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDishDir
        File directory = cw.getDir("imageDishDir", Context.MODE_PRIVATE);
        // Create imageDir
        File myPath = new File(directory,fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath() + "/" + fileName;
    }

    private boolean checkDishAvailableForUpdate(@NonNull Dish dish) {
        List<Dish> list  = AppDatabase.getInstance(UpdateDishActivity.this).dishDao().checkDishExist(dish.getName(), dish.getPrice());
        return (list == null) || (list.size() == 0) || (list.size() == 1 && list.get(0).getDishID() == dish.getDishID());
    }
}