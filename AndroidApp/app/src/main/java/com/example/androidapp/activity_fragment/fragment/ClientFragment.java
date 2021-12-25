package com.example.androidapp.activity_fragment.fragment;

import static android.app.Activity.RESULT_OK;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import com.example.androidapp.activity_fragment.activity.UpdateClientActivity;
import com.example.androidapp.activity_fragment.activity.NewClientActivity;
import com.example.androidapp.R;

import com.example.androidapp.data.AppDatabase;
import com.example.androidapp.data.clientdata.Client;
import com.example.androidapp.data.clientdata.ClientAdapter;
import com.example.androidapp.data.clientdata.ClientViewModel;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class ClientFragment extends Fragment {
    public static final int ADD_CLIENT_REQUEST = 1;
    public static final int EDIT_CLIENT_REQUEST = 2;

    private List<Client> mListClient;
    private ClientViewModel clientViewModel;
    private Button btnAddClient;
    private EditText edtSearchBar;
    private ClientAdapter clientAdapter;
    //confirm sound
    private MediaPlayer sound = null;

    public ClientFragment() {
        //Empty on purpose
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client, container, false);
        mListClient = new ArrayList<>();
        initUi(view);
        //Sound
        sound = MediaPlayer.create(getActivity(), R.raw.confirm_sound);

        //Create Recycler View
        RecyclerView rcvData = view.findViewById(R.id.client_recycler);;
        //rcvData.setHasFixedSize(true);
        rcvData.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //Create Client ADAPTER
        clientAdapter = new ClientAdapter(mListClient);
        rcvData.setAdapter(clientAdapter);

        //Create Client VIEW MODEL
        clientViewModel = new ViewModelProvider(getActivity()).get(ClientViewModel.class);
        clientViewModel.getAllClients().observe(getActivity(), new Observer<List<Client>>() {
            @Override

            //Method DISPLAY the list on screen
            public void onChanged(List<Client> clients) {
                //use for filter
                clientAdapter.setClient(clients);
                //use for animation
                clientAdapter.submitList(clients);

            }
        });


        //Create search bar listener for SEARCH METHOD
        edtSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Empty on purpose
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Empty on purpose
            }

            @Override
            public void afterTextChanged(Editable s) {
                clientAdapter.getFilter().filter(s.toString());
            }
        });

        //Method CLICK TO VIEW an item in Recycler View
        clientAdapter.setOnItemClickListener(new ClientAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Client client) {
                Intent intent = new Intent(getActivity(), UpdateClientActivity.class);
                intent.putExtra(UpdateClientActivity.EXTRA_CLIENT_ID, client.getClientId());
                intent.putExtra(UpdateClientActivity.EXTRA_CLIENT_NAME, client.getClientName());
                intent.putExtra(UpdateClientActivity.EXTRA_CLIENT_NUMBER, client.getPhoneNumber());
                intent.putExtra(UpdateClientActivity.EXTRA_CLIENT_ADDRESS, client.getAddress());
                intent.putExtra(UpdateClientActivity.EXTRA_OLD_IMAGE, client.getImageDir());

                startActivityForResult(intent, EDIT_CLIENT_REQUEST);
            }
        });

        //Delete item
        clientAdapter.setOnItemClickDelListener(new ClientAdapter.OnItemClickDelListener() {
            @Override
            public void onItemClickDel(Client client) {
                confirmDelDialog(client);
            }
        });

        //Method CLICK the add button
        btnAddClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewClientActivity.class);
                startActivityForResult(intent, ADD_CLIENT_REQUEST);
            }
        });

        return view;
    }

    //Method to init UI components
    private void initUi (View view) {
        btnAddClient = view.findViewById(R.id.add_client_button);
        edtSearchBar = view.findViewById(R.id.client_search_bar);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //ADD_CLIENT_REQUEST (Add a Client to database)
        if (requestCode == ADD_CLIENT_REQUEST && resultCode == RESULT_OK) {

            String name = data.getStringExtra(NewClientActivity.EXTRA_CLIENT_NAME);
            String number = data.getStringExtra(NewClientActivity.EXTRA_CLIENT_NUMBER);
            String address = data.getStringExtra(NewClientActivity.EXTRA_CLIENT_ADDRESS);
            String imageDir = data.getStringExtra(NewClientActivity.EXTRA_CLIENT_IMAGE);
            Client client = new Client(name, number, address, imageDir);

            //Check if exist client
            if (checkClientExistForInsert(client)) {
                clientViewModel.insertClient(client);
            }

        }
        //EDIT CLIENT REQUEST (Update an existing client)
        else if (requestCode == EDIT_CLIENT_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(UpdateClientActivity.EXTRA_CLIENT_ID, -1);
            if (id == -1) {
                Toast.makeText(getActivity(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = data.getStringExtra(UpdateClientActivity.EXTRA_CLIENT_NAME);
            String number = data.getStringExtra(UpdateClientActivity.EXTRA_CLIENT_NUMBER);
            String address = data.getStringExtra(UpdateClientActivity.EXTRA_CLIENT_ADDRESS);
            String imageDir = data.getStringExtra(UpdateClientActivity.EXTRA_NEW_IMAGE);
            Client client = new Client(name, number, address, imageDir);
            client.setClientId(id);

            //Check if exist client
            if (checkClientExistForUpdate(client)) {
                clientViewModel.updateClient(client);
            }
        }
    }

    private boolean checkClientExistForInsert(@NonNull Client client) {
        List<Client> list  = AppDatabase.getInstance(getContext()).clientDao().checkClientExist(client.getClientName(),
                client.getAddress(), client.getPhoneNumber());
        return list == null || list.size() == 0;
    }
    private boolean checkClientExistForUpdate(@NonNull Client client) {
        List<Client> list  = AppDatabase.getInstance(getContext()).clientDao().checkClientExist(client.getClientName(),
                client.getAddress(), client.getPhoneNumber());
        return (list == null) || (list.size() == 0) || (list.size() == 1 && list.get(0).getClientId() == client.getClientId());
    }


    private void confirmDelDialog(Client client) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.alert_dialog_delete, (RelativeLayout)getView().findViewById(R.id.layout_dialog)
        );
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        //confirm paid btn
        view.findViewById(R.id.confirm_dialog_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                sound.start();
                //delete the old image
                File oldImage = new File(client.getImageDir());
                boolean deleted = oldImage.delete();
                clientViewModel.deleteClient(client);
            }
        });
        //cancel btn
        view.findViewById(R.id.cancel_dialog_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}
