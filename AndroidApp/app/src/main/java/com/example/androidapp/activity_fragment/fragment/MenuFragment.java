package com.example.androidapp.activity_fragment.fragment;

import static android.app.Activity.RESULT_OK;



import android.content.Intent;

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


import com.example.androidapp.activity_fragment.activity.UpdateDishActivity;
import com.example.androidapp.activity_fragment.activity.NewDishActivity;
import com.example.androidapp.R;

import com.example.androidapp.data.AppDatabase;
import com.example.androidapp.data.menudata.Dish;
import com.example.androidapp.data.menudata.DishAdapter;
import com.example.androidapp.data.menudata.DishViewModel;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {
    public static final int ADD_DISH_REQUEST = 1;
    public static final int EDIT_DISH_REQUEST = 2;

    private List<Dish> mListDish;
    private DishViewModel dishViewModel;
    private Button btnAddDish;
    private EditText edtSearchBar;
    private DishAdapter dishAdapter;
    //confirm sound
    private MediaPlayer sound = null;
    public MenuFragment() {
        //Empty on purpose
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        mListDish = new ArrayList<>();
        initUi(view);
        //Sound
        sound = MediaPlayer.create(getActivity(), R.raw.confirm_sound);

        //Create Recycler View
        RecyclerView rcvData = view.findViewById(R.id.menu_recycler);;
        //rcvData.setHasFixedSize(true);
        rcvData.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //Create DISH ADAPTER
        dishAdapter = new DishAdapter(mListDish);
        rcvData.setAdapter(dishAdapter);

        //Create DISH VIEW MODEL
        dishViewModel = new ViewModelProvider(getActivity()).get(DishViewModel.class);
        dishViewModel.getAllDishes().observe(getActivity(), new Observer<List<Dish>>() {
            @Override

            //Method DISPLAY the list on screen
            public void onChanged(List<Dish> dishes) {
                //use for filter
                dishAdapter.setDish(dishes);
                //use for animation
                dishAdapter.submitList(dishes);

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
                dishAdapter.getFilter().filter(s.toString());
            }
        });

        //Method CLICK TO VIEW info of an item in Recycler View
        dishAdapter.setOnItemClickListener(new DishAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Dish dish) {
                Intent intent = new Intent(getActivity(), UpdateDishActivity.class);
                intent.putExtra(UpdateDishActivity.EXTRA_DISH_ID, dish.getDishID());
                intent.putExtra(UpdateDishActivity.EXTRA_DISH_NAME, dish.getName());
                intent.putExtra(UpdateDishActivity.EXTRA_DISH_PRICE, dish.getPrice());
                intent.putExtra(UpdateDishActivity.EXTRA_OLD_IMAGE, dish.getImageDir());

                startActivityForResult(intent, EDIT_DISH_REQUEST);
            }
        });
        //Delete item
        dishAdapter.setOnItemClickDelListener(new DishAdapter.OnItemClickDelListener() {
            @Override
            public void onItemClickDel(Dish dish) {
                confirmDelDialog(dish);
            }
        });

        //Method CLICK the add button
        btnAddDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewDishActivity.class);
                startActivityForResult(intent, ADD_DISH_REQUEST);
            }
        });

        return view;
    }

    //Method to init UI components
    private void initUi (View view) {
        btnAddDish = view.findViewById(R.id.add_dish_button);
        edtSearchBar = view.findViewById(R.id.dish_search_bar);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //ADD_DISH_REQUEST (Add a dish to database)
        if (requestCode == ADD_DISH_REQUEST && resultCode == RESULT_OK) {

            String name = data.getStringExtra(NewDishActivity.EXTRA_DISH_NAME);
            int price = data.getIntExtra(NewDishActivity.EXTRA_DISH_PRICE, 0);
            String imageDir = data.getStringExtra(NewDishActivity.EXTRA_DISH_IMAGE);
            Dish dish = new Dish(name, price, imageDir);
            dishViewModel.insertDish(dish);


        }
        //EDIT DISH REQUEST (Update an existing dish)
        else if (requestCode == EDIT_DISH_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(UpdateDishActivity.EXTRA_DISH_ID, -1);
            if (id == -1) {
                Toast.makeText(getActivity(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = data.getStringExtra(UpdateDishActivity.EXTRA_DISH_NAME);
            int price = data.getIntExtra(UpdateDishActivity.EXTRA_DISH_PRICE, 0);
            String imageDir = data.getStringExtra(UpdateDishActivity.EXTRA_NEW_IMAGE);
            Dish dish = new Dish(name, price, imageDir);
            dish.setDishID(id);
            dishViewModel.updateDish(dish);

        }
    }



    private void confirmDelDialog(Dish dish) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.alert_dialog_delete, (RelativeLayout)getView().findViewById(R.id.layout_dialog)
        );
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        //confirm delete btn
        view.findViewById(R.id.confirm_dialog_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                sound.start();
                //delete the old image
                File oldImage = new File(dish.getImageDir());
                boolean deleted = oldImage.delete();
                dishViewModel.deleteDish(dish);
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
