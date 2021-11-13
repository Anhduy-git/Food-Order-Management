package com.example.androidapp.activity_fragment.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.androidapp.R;
import com.example.androidapp.data.clientdata.Client;
import com.example.androidapp.data.clientdata.ClientAdapter;
import com.example.androidapp.data.clientdata.ClientViewModel;
import com.example.androidapp.data.menudata.Dish;
import com.example.androidapp.data.menudata.DishViewModel;

import java.util.List;

public class SubContactActivity extends AppCompatActivity {

    public static final String EXTRA_ID =
            "com.example.androidapp.EXTRA_ID";
    public static final String EXTRA_NAME =
            "com.example.androidapp.EXTRA_NAME";
    public static final String EXTRA_PHONE_NUMBER =
            "com.example.androidapp.EXTRA_PHONE_NUMBER";
    public static final String EXTRA_ADDRESS =
            "com.example.androidapp.EXTRA_ADDRESS";

    private List<Client> mListClient;
    private ClientViewModel clientViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_contact);

        //Create Recycler View
        RecyclerView rcvData = findViewById(R.id.client_recycler);
        rcvData.setLayoutManager(new LinearLayoutManager(this));

        final ClientAdapter clientAdapter = new ClientAdapter(mListClient);
        rcvData.setAdapter(clientAdapter);

        //Create client view model
        clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);
        clientViewModel.getAllClients().observe(this, new Observer<List<Client>>() {
            @Override
            public void onChanged(List<Client> clients) {
                clientAdapter.setClient(clients);
                clientAdapter.submitList(clients);
            }
        });

        clientAdapter.setOnItemClickListener(new ClientAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Client client) {
                Intent data = new Intent();
                data.putExtra(EXTRA_NAME, client.getClientName());
                data.putExtra(EXTRA_PHONE_NUMBER, client.getPhoneNumber());
                data.putExtra(EXTRA_ADDRESS, client.getAddress());

                setResult(RESULT_OK, data);
                finish();
            }
        });
    }
}