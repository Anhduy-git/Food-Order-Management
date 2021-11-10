package com.example.androidapp.activity_fragment.activity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidapp.R;
public class UpdateClientActivity extends AppCompatActivity {
    public static final String EXTRA_CLIENT_ID =
            "com.example.androidapp.EXTRA_CLIENT_ID";
    public static final String EXTRA_CLIENT_NAME =
            "com.example.androidapp.EXTRA_CLIENT_NAME";
    public static final String EXTRA_CLIENT_NUMBER =
            "com.example.androidapp.EXTRA_CLIENT_NUMBER";
    public static final String EXTRA_CLIENT_ADDRESS =
            "com.example.androidapp.EXTRA_CLIENT_ADDRESS";

    private EditText editClientName;
    private EditText editClientNumber;
    private EditText editClientAddress;
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
        }
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
    }
    private void initUi () {
        editClientName = findViewById(R.id.update_client_name);
        editClientNumber = findViewById(R.id.update_client_number);
        editClientAddress = findViewById(R.id.update_client_address);
        btnUpdateClient = findViewById(R.id.update_client_info_button);
        btnBack = findViewById(R.id.back_btn);
    }
    private void updateClient() {
        String strClientName = editClientName.getText().toString().trim();
        String strClientNumber = editClientNumber.getText().toString().trim();
        String strClientAddress = editClientAddress.getText().toString().trim();

        //Check if fields are empty, if so then don't add to database
        if (TextUtils.isEmpty(strClientName) || TextUtils.isEmpty(strClientNumber) || TextUtils.isEmpty(strClientAddress)) {
            Toast.makeText(this, "Please insert name, number and address", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_CLIENT_NAME, strClientName);
        data.putExtra(EXTRA_CLIENT_NUMBER, strClientNumber);
        data.putExtra(EXTRA_CLIENT_ADDRESS, strClientAddress);

        int id = getIntent().getIntExtra(EXTRA_CLIENT_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_CLIENT_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }
}
