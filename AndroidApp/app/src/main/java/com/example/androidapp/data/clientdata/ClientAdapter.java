package com.example.androidapp.data.clientdata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;


import java.util.ArrayList;
import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> implements Filterable {
    private List<Client> mListClient;
    private List<Client> mListClientFull;

    private OnItemClickListener listener;

    public ClientAdapter(List<Client> mListClient) {
        this.mListClient = mListClient;
    }

    public void setClient(List<Client> mListClient) {
        this.mListClient = mListClient;
        this.mListClientFull = new ArrayList<>(mListClient);

        notifyDataSetChanged();
    }

    //Get the Client position
    public Client getClientAt(int postition) {
        return mListClient.get(postition);
    }

    @NonNull
    @Override
    public ClientAdapter.ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_item_recycler, parent, false);

        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        Client client = mListClient.get(position);
        if (client == null) {
            return;
        }

        holder.tvClientName.setText(client.getClientName());
        holder.tvClientNumber.setText(client.getPhoneNumber());
        holder.tvClientAddress.setText(client.getAddress());
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

    public class ClientViewHolder extends RecyclerView.ViewHolder {

        private TextView tvClientName;
        private TextView tvClientNumber;
        private TextView tvClientAddress;

        public ClientViewHolder(@NonNull View itemView) {
            super(itemView);

            tvClientName = itemView.findViewById(R.id.client_name);
            tvClientNumber = itemView.findViewById(R.id.client_phone);
            tvClientAddress = itemView.findViewById(R.id.client_address);

            itemView.setOnClickListener(new View.OnClickListener() {
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

    //Interface to click on a Client item
    public interface OnItemClickListener {
        void onItemClick(Client client);
    }

    //Method item click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

