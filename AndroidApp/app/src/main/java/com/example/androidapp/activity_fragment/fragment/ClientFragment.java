package com.example.androidapp.activity_fragment.fragment;

import static android.app.Activity.RESULT_OK;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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


import com.example.androidapp.activity_fragment.activity.NewDishActivity;
import com.example.androidapp.activity_fragment.activity.UpdateClientActivity;
import com.example.androidapp.activity_fragment.activity.NewClientActivity;
import com.example.androidapp.R;

import com.example.androidapp.data.AppDatabase;
import com.example.androidapp.data.clientdata.Client;
import com.example.androidapp.data.clientdata.ClientAdapter;
import com.example.androidapp.data.clientdata.ClientViewModel;
import com.example.androidapp.data.orderdata.Order;
import com.example.androidapp.data.orderdata.OrderAdapter;


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
                intent.putExtra(UpdateClientActivity.EXTRA_CLIENT_ID, client.getClientId());
                intent.putExtra(UpdateClientActivity.EXTRA_CLIENT_NAME, client.getClientName());
                intent.putExtra(UpdateClientActivity.EXTRA_CLIENT_NUMBER, client.getPhoneNumber());
                intent.putExtra(UpdateClientActivity.EXTRA_CLIENT_ADDRESS, client.getAddress());
                intent.putExtra(UpdateClientActivity.EXTRA_CLIENT_IMAGE, client.getImageDir());

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
            //get bitmap image from intent
            byte[] imageArray = data.getByteArrayExtra(NewClientActivity.EXTRA_CLIENT_IMAGE);
            Client client = new Client(name, number, address, "NULL");
            //Check if exist client
            if (checkClientExistForInsert(client)) {
                if (imageArray != null) {
                    Bitmap image = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
                    String imageDir = saveToInternalStorage(image, client.getClientName() + "-" +
                            client.getAddress() + "-" + client.getPhoneNumber());
                    client.setImageDir(imageDir);
                }
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

            byte[] imageArray = data.getByteArrayExtra(UpdateClientActivity.EXTRA_CLIENT_IMAGE);
            Client client = new Client(name, number, address, "NULL");
            client.setClientId(id);
            //Check if exist client
            if (checkClientExistForUpdate(client)) {
                if (imageArray != null) {
                    Bitmap image = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
                    String imageDir = saveToInternalStorage(image, client.getClientName() + "-" +
                            client.getAddress() + "-" + client.getPhoneNumber());
                    client.setImageDir(imageDir);
                }
                clientViewModel.updateClient(client);
            }
        } else {
            //Do nothing
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
    private String saveToInternalStorage(Bitmap bitmapImage, String fileName){
        ContextWrapper cw = new ContextWrapper(getContext());
        // path to /data/data/yourapp/app_data/imageClientDir
        File directory = cw.getDir("imageClientDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory,fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
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

}
