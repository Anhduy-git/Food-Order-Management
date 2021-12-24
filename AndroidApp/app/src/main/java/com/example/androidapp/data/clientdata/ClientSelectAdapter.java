package com.example.androidapp.data.clientdata;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.data.ImageConverter;
import com.example.androidapp.data.menudata.Dish;
import com.example.androidapp.data.menudata.DishSelectAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ClientSelectAdapter extends RecyclerView.Adapter<ClientSelectAdapter.ClientSelectViewHolder> implements Filterable {
    private List<Client> mListClient;
    private List<Client> mListClientFull;


    private OnItemClickListener listener;

    public ClientSelectAdapter(List<Client> mListClient) {
        this.mListClient = mListClient;
    }

    public void setClient(List<Client> mListClient) {
        this.mListClient = mListClient;
        this.mListClientFull = new ArrayList<>(mListClient);

        notifyDataSetChanged();
    }

    //Get the client position
    public Client gClientAt(int postition) {
        return mListClient.get(postition);
    }

    @NonNull
    @Override
    public ClientSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_item_select_recycler, parent, false);

        return new ClientSelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientSelectViewHolder holder, int position) {
        Client client = mListClient.get(position);
        if (client == null) {
            return;
        }

        holder.tvClientName.setText(client.getClientName());
        holder.tvClientNumber.setText(client.getPhoneNumber());
        holder.tvClientAddress.setText(client.getAddress());
        //read image from file
        try {
            File f=new File(client.getImageDir());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            holder.imageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e) {
            Resources res = holder.imageView.getResources();
            Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.ava_client_default);
            holder.imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        if (mListClient != null) {
            return mListClient.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return clientFilter;
    }

    //Create a filter object for searching
    private Filter clientFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Client> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mListClientFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Client item : mListClientFull) {
                    if (item.getClientName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mListClient.clear();
            mListClient.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ClientSelectViewHolder extends RecyclerView.ViewHolder {

        private TextView tvClientName;
        private TextView tvClientNumber;
        private TextView tvClientAddress;
        private ImageView imageView;
        private RelativeLayout item;

        public ClientSelectViewHolder(@NonNull View itemView) {
            super(itemView);

            tvClientName = itemView.findViewById(R.id.client_name);
            tvClientNumber = itemView.findViewById(R.id.client_phone);
            tvClientAddress = itemView.findViewById(R.id.client_address);
            imageView = itemView.findViewById(R.id.client_avatar);
            item = itemView.findViewById(R.id.client_item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(mListClient.get(position));
                    }
                }
            });
        }
    }

    //Interface to click on a dish item
    public interface OnItemClickListener {
        void onItemClick(Client client);
    }

    //Method item click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}