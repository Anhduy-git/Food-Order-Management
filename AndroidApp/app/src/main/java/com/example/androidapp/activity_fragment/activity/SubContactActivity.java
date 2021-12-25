package com.example.androidapp.activity_fragment.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidapp.R;
import com.example.androidapp.data.clientdata.Client;
import com.example.androidapp.data.clientdata.ClientSelectAdapter;
import com.example.androidapp.data.clientdata.ClientViewModel;

import java.util.List;

public class SubContactActivity extends AppCompatActivity {

    public static final String EXTRA_NAME =
            "com.example.androidapp.EXTRA_NAME";
    public static final String EXTRA_PHONE_NUMBER =
            "com.example.androidapp.EXTRA_PHONE_NUMBER";
    public static final String EXTRA_ADDRESS =
            "com.example.androidapp.EXTRA_ADDRESS";
    public static final String EXTRA_IMAGE =
            "com.example.androidapp.EXTRA_IMAGE";

    private Button btnBack;

    private List<Client> mListClient;
    private ClientViewModel clientViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_contact);

        initUi();

        //Create Recycler View
        RecyclerView rcvData = findViewById(R.id.client_recycler);
        rcvData.setLayoutManager(new LinearLayoutManager(this));

        final ClientSelectAdapter clientSelectAdapter = new ClientSelectAdapter(mListClient);
        rcvData.setAdapter(clientSelectAdapter);

        //Create client view model
        clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);
        clientViewModel.getAllClients().observe(this, new Observer<List<Client>>() {
            @Override
            public void onChanged(List<Client> clients) {
                clientSelectAdapter.setClient(clients);
            }
        });

        clientSelectAdapter.setOnItemClickListener(new ClientSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Client client) {
                Intent data = new Intent();
                data.putExtra(EXTRA_NAME, client.getClientName());
                data.putExtra(EXTRA_PHONE_NUMBER, client.getPhoneNumber());
                data.putExtra(EXTRA_ADDRESS, client.getAddress());
                data.putExtra(EXTRA_IMAGE, client.getImageDir());

                setResult(RESULT_OK, data);
                finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    void initUi(){
        btnBack = findViewById(R.id.btn_back_sub_contact);
    }
}