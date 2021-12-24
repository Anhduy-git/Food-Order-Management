package com.example.androidapp.activity_fragment.fragment;

import static android.app.Activity.RESULT_OK;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.activity_fragment.activity.NewClientActivity;
import com.example.androidapp.activity_fragment.activity.UpdateDishActivity;
import com.example.androidapp.activity_fragment.activity.NewDishActivity;
import com.example.androidapp.R;

import com.example.androidapp.data.AppDatabase;
import com.example.androidapp.data.ImageConverter;
import com.example.androidapp.data.clientdata.Client;
import com.example.androidapp.data.menudata.Dish;
import com.example.androidapp.data.menudata.DishAdapter;
import com.example.androidapp.data.menudata.DishViewModel;
import com.example.androidapp.data.orderdata.Order;
import com.example.androidapp.data.orderdata.OrderAdapter;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
        //Sound
        sound = MediaPlayer.create(getActivity(), R.raw.confirm_sound);

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
//                dishViewModel.deleteDish(dishAdapter.getDishAt(viewHolder.getAdapterPosition()));
//                Toast.makeText(getActivity(), "Dish deleted", Toast.LENGTH_SHORT).show();
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
            //get bitmap image from intent
            byte[] imageArray = data.getByteArrayExtra(NewDishActivity.EXTRA_DISH_IMAGE);
            Dish dish = new Dish(name, price, "");
            //check if dish exist
            if (checkDishExistForInsert(dish)) {
                if (imageArray == null || imageArray.length == 0) {
                    Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.rec_ava_dish_default);
                    //Store image to a file in internal memory
                    String imageDir = saveToInternalStorage(image, dish.getName() + "-" + dish.getPrice());
                    dish.setImageDir(imageDir);
                } else {
                    Bitmap image = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
                    //Store image to a file in internal memory
                    String imageDir = saveToInternalStorage(image, dish.getName() + "-" + dish.getPrice());
                    dish.setImageDir(imageDir);
                }
                dishViewModel.insertDish(dish);
            }

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
            String oldImagePath = data.getStringExtra(UpdateDishActivity.EXTRA_OLD_IMAGE);
            //delete the old image when update
            File oldImage = new File(oldImagePath);
            boolean deleted = oldImage.delete();
            //get bitmap image from intent
            byte[] imageArray = data.getByteArrayExtra(UpdateDishActivity.EXTRA_NEW_IMAGE);
            Dish dish = new Dish(name, price, "");
            dish.setDishID(id);
            //check if dish exist
            if (checkDishExistForUpdate(dish)) {
                if (imageArray == null || imageArray.length == 0) {
                    Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.rec_ava_dish_default);
                    //Store image to a file in internal memory
                    String imageDir = saveToInternalStorage(image, dish.getName() + "-" + dish.getPrice());
                    dish.setImageDir(imageDir);
                } else {
                    Bitmap image = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
                    //Store image to a file in internal memory
                    String imageDir = saveToInternalStorage(image, dish.getName() + "-" + dish.getPrice());
                    dish.setImageDir(imageDir);
                }
                dishViewModel.updateDish(dish);
            }
        }
    }
    private boolean checkDishExistForInsert(@NonNull Dish dish) {
        List<Dish> list  = AppDatabase.getInstance(getContext()).dishDao().checkDishExist(dish.getName(), dish.getPrice());
        return list == null || list.size() == 0;
    }
    private boolean checkDishExistForUpdate(@NonNull Dish dish) {
        List<Dish> list  = AppDatabase.getInstance(getContext()).dishDao().checkDishExist(dish.getName(), dish.getPrice());
        return (list == null) || (list.size() == 0) || (list.size() == 1 && list.get(0).getDishID() == dish.getDishID());
    }
    private String saveToInternalStorage(Bitmap bitmapImage, String fileName){
        ContextWrapper cw = new ContextWrapper(getContext());
        // path to /data/data/yourapp/app_data/imageDishDir
        File directory = cw.getDir("imageDishDir", Context.MODE_PRIVATE);
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
        return directory.getAbsolutePath() + "/" + fileName;
    }

    private void confirmDelDialog(Dish dish) {
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
