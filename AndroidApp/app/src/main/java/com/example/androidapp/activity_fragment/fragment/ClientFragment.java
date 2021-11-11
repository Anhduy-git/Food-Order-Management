package com.example.androidapp.activity_fragment.fragment;

import static android.app.Activity.RESULT_OK;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.androidapp.activity_fragment.activity.UpdateClientActivity;
import com.example.androidapp.activity_fragment.activity.NewClientActivity;
import com.example.androidapp.R;

import com.example.androidapp.data.clientdata.Client;
import com.example.androidapp.data.clientdata.ClientAdapter;
import com.example.androidapp.data.clientdata.ClientViewModel;
import com.example.androidapp.data.orderdata.Order;
import com.example.androidapp.data.orderdata.OrderAdapter;


import java.util.ArrayList;
import java.util.List;
public class ClientFragment extends Fragment {
    public static final int ADD_CLIENT_REQUEST = 1;
    public static final int EDIT_CLIENT_REQUEST = 2;

    private List<Client> mListClient;
    private ClientViewModel clientViewModel;
    private Button btnAddClient;
    private EditText edtSearchBar;

    public ClientFragment() {
        //Empty on purpose
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client, container, false);
        mListClient = new ArrayList<>();
        initUi(view);

        //Create Recycler View
        RecyclerView rcvData = view.findViewById(R.id.client_recycler);;
        //rcvData.setHasFixedSize(true);
        rcvData.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //Create Client ADAPTER
        final ClientAdapter clientAdapter = new ClientAdapter(mListClient);
        rcvData.setAdapter(clientAdapter);

        //Create Client VIEW MODEL
        clientViewModel = new ViewModelProvider(getActivity()).get(ClientViewModel.class);
        clientViewModel.getAllClients().observe(getActivity(), new Observer<List<Client>>() {
            @Override

            //Method DISPLAY the list on screen
            public void onChanged(List<Client> clients) {
                clientAdapter.setClient(clients);

            }
        });

        //Method DELETE an item by swiping it
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
//                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                //Not implemented on purpose
//                return false;
//            }
//            //This is the swiping to delete method
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                clientViewModel.deleteClient(clientAdapter.getClientAt(viewHolder.getAdapterPosition()));
//                Toast.makeText(getActivity(), "Client deleted", Toast.LENGTH_SHORT).show();
//            }
//        }).attachToRecyclerView(rcvData);

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
                intent.putExtra(UpdateClientActivity.EXTRA_CLIENT_ID, client.getId());
                intent.putExtra(UpdateClientActivity.EXTRA_CLIENT_NAME, client.getClientName());
                intent.putExtra(UpdateClientActivity.EXTRA_CLIENT_NUMBER, client.getPhoneNumber());
                intent.putExtra(UpdateClientActivity.EXTRA_CLIENT_ADDRESS, client.getAddress());


                startActivityForResult(intent, EDIT_CLIENT_REQUEST);
            }
        });

        //Delete item
        clientAdapter.setOnItemClickDelListener(new ClientAdapter.OnItemClickDelListener() {
            @Override
            public void onItemClickDel(Client client) {
                clientViewModel.deleteClient(client);
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

            Client client = new Client(name, number, address);
            clientViewModel.insertClient(client);
            Toast.makeText(getActivity(), "Client added successfully", Toast.LENGTH_SHORT).show();
        }
        //EDIT CLIENT REQUEST (Update an existing client)

        else if (requestCode == EDIT_CLIENT_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(UpdateClientActivity.EXTRA_CLIENT_ID, -1);
            if (id == -1) {
                Toast.makeText(getActivity(), "Client can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = data.getStringExtra(UpdateClientActivity.EXTRA_CLIENT_NAME);
            String number = data.getStringExtra(UpdateClientActivity.EXTRA_CLIENT_NUMBER);
            String address = data.getStringExtra(UpdateClientActivity.EXTRA_CLIENT_ADDRESS);

            Client client = new Client(name, number, address);
            client.setId(id);
            clientViewModel.updateClient(client);
            Toast.makeText(getActivity(), "Client updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Client not added", Toast.LENGTH_SHORT).show();
        }

    }

}
