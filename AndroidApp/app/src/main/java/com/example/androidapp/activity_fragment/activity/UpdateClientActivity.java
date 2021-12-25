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
import com.example.androidapp.data.clientdata.Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class UpdateClientActivity extends AppCompatActivity {
    public static final String EXTRA_CLIENT_ID =
            "com.example.androidapp.EXTRA_CLIENT_ID";
    public static final String EXTRA_CLIENT_NAME =
            "com.example.androidapp.EXTRA_CLIENT_NAME";
    public static final String EXTRA_CLIENT_NUMBER =
            "com.example.androidapp.EXTRA_CLIENT_NUMBER";
    public static final String EXTRA_CLIENT_ADDRESS =
            "com.example.androidapp.EXTRA_CLIENT_ADDRESS";
    public static final String EXTRA_NEW_IMAGE =
            "com.example.androidapp.EXTRA_NEW_IMAGE";
    public static final String EXTRA_OLD_IMAGE =
            "com.example.androidapp.EXTRA_OLD_IMAGE";

    private final int GALLERY_REQUEST = 1;
    private final int CAMERA_REQUEST = 2;
    private final int IMAGE_SIZE = 500;

    private EditText editClientName;
    private EditText editClientNumber;
    private EditText editClientAddress;
    private ImageView imageView;
    private Button btnAddImage;
    private Button btnUpdateClient;
    private Button btnBack;
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_client);
        initUi();
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_CLIENT_ID)) {
            editClientName.setText(intent.getStringExtra(EXTRA_CLIENT_NAME));
            editClientNumber.setText(intent.getStringExtra(EXTRA_CLIENT_NUMBER));
            editClientAddress.setText(intent.getStringExtra(EXTRA_CLIENT_ADDRESS));
            
            try {
                //read path image from intent and display
                File f=new File(intent.getStringExtra(EXTRA_OLD_IMAGE));
                Bitmap image = BitmapFactory.decodeStream(new FileInputStream(f));
                imageView.setImageBitmap(image);
            }
            catch (FileNotFoundException e) {
                //display default image for client
                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.ava_client_default);
                imageView.setImageBitmap(image);
            }
        }
        //Check for update to show btn update
        checkUpdate();

        btnUpdateClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateClient();
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

    private void initUi () {
        editClientName = findViewById(R.id.update_client_name);
        editClientNumber = findViewById(R.id.update_client_number);
        editClientAddress = findViewById(R.id.update_client_address);
        imageView = findViewById(R.id.client_avatar);
        btnUpdateClient = findViewById(R.id.update_client_info_button);
        btnBack = findViewById(R.id.btn_back);
        btnAddImage = findViewById(R.id.btn_update_client_gallery);
    }
    private void updateClient() {
        String strClientName = editClientName.getText().toString().trim();
        String strClientNumber = editClientNumber.getText().toString().trim();
        String strClientAddress = editClientAddress.getText().toString().trim();

        //Check if fields are empty, if so then don't add to database
        if (TextUtils.isEmpty(strClientName) || TextUtils.isEmpty(strClientNumber) || TextUtils.isEmpty(strClientAddress)) {
            Toast.makeText(this, "Xin hãy nhập tên, số điện thoại và địa chỉ", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_CLIENT_NAME, strClientName);
        data.putExtra(EXTRA_CLIENT_NUMBER, strClientNumber);
        data.putExtra(EXTRA_CLIENT_ADDRESS, strClientAddress);
        int id = getIntent().getIntExtra(EXTRA_CLIENT_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_CLIENT_ID, id);
        } else {
            Toast.makeText(UpdateClientActivity.this, "Cập nhật không hợp lệ !", Toast.LENGTH_SHORT).show();
            return;
        }

        Client clientUpdate = new Client(strClientName, strClientNumber, strClientAddress, "");
        clientUpdate.setClientId(id);
        if (checkClientAvailableForUpdate(clientUpdate)) {
            //delete the old image when update
            File oldImage = new File(getIntent().getStringExtra(EXTRA_OLD_IMAGE));
            boolean deleted = oldImage.delete();

            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            Bitmap image = ImageConverter.getResizedBitmap(bitmap, IMAGE_SIZE);
            String imageDir = saveToInternalStorage(image, strClientName + "-" +
                    strClientAddress + "-" + strClientNumber);

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
            Toast.makeText(UpdateClientActivity.this, "Khách hàng đã tồn tại !", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkRequestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int cameraPermission = ActivityCompat.checkSelfPermission(UpdateClientActivity.this,
                    Manifest.permission.CAMERA);
            if (cameraPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                        UpdateClientActivity.this,
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
                //set btn update to visible
                btnUpdateClient.setVisibility(View.VISIBLE);
                
                Uri selectedImage = data.getData();
                imageView.setImageURI(selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            try {
                //set btn update to visible
                btnUpdateClient.setVisibility(View.VISIBLE);
                
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
            Toast.makeText(UpdateClientActivity.this, "Chưa được cấp quyền!", Toast.LENGTH_SHORT).show();
        }
    }
    private void checkUpdate() {
        editClientName.addTextChangedListener(new TextWatcher() {

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
                btnUpdateClient.setVisibility(View.VISIBLE);
            }
        });

        editClientAddress.addTextChangedListener(new TextWatcher() {

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
                btnUpdateClient.setVisibility(View.VISIBLE);
            }
        });
        editClientNumber.addTextChangedListener(new TextWatcher() {

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
                btnUpdateClient.setVisibility(View.VISIBLE);
            }
        });
    }
    private String saveToInternalStorage(Bitmap bitmapImage, String fileName){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageClientDir
        File directory = cw.getDir("imageClientDir", Context.MODE_PRIVATE);
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
        return directory.getAbsolutePath() + '/' + fileName;
    }
    private boolean checkClientAvailableForUpdate(@NonNull Client client) {
        List<Client> list  = AppDatabase.getInstance(UpdateClientActivity.this).clientDao().checkClientExist(client.getClientName(),
                client.getAddress(), client.getPhoneNumber());
        return (list == null) || (list.size() == 0) || (list.size() == 1 && list.get(0).getClientId() == client.getClientId());
    }
}
