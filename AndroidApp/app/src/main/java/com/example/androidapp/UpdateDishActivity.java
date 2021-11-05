package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateDishActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "com.example.androidapp.EXTRA_ID";
    public static final String EXTRA_NAME =
            "com.example.androidapp.EXTRA_NAME";
    public static final String EXTRA_PRICE =
            "com.example.androidapp.EXTRA_PRICE";

    private EditText editDishName;
    private EditText editDishPrice;
    private Button btnBack;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_menu);

        initUi();

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            editDishName.setText(intent.getStringExtra(EXTRA_NAME));
            String price = String.valueOf(intent.getIntExtra(EXTRA_PRICE, 0));
            editDishPrice.setText(price);
        }

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
    }

    private void initUi() {
        editDishName = findViewById(R.id.dish_name_info);
        editDishPrice = findViewById(R.id.dish_price_info);
        btnBack = findViewById(R.id.confirm_dish_back_btn);
        btnUpdate = findViewById(R.id.dish_update_button);
    }

    private void updateDish() {
        String strDishName = editDishName.getText().toString().trim();
        String strDishPrice = editDishPrice.getText().toString().trim();

        //Check if fields are empty, if so then don't add to database
        if (TextUtils.isEmpty(strDishName) || TextUtils.isEmpty(strDishPrice)) {
            Toast.makeText(this, "Please insert name and price", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, strDishName);
        data.putExtra(EXTRA_PRICE, Integer.valueOf(strDishPrice));

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }
}