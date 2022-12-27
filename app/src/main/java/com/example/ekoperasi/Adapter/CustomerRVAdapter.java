package com.example.ekoperasi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ekoperasi.Model.Karyawan;
import com.example.ekoperasi.Model.Nasabah;
import com.example.ekoperasi.R;

import java.util.ArrayList;

public class CustomerRVAdapter extends RecyclerView.Adapter<CustomerRVAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Nasabah> nasabahArrayList;

    // creating a constructor class.
    public CustomerRVAdapter(Context context, ArrayList<Nasabah> nasabahArrayList) {
        this.context = context;
        this.nasabahArrayList = nasabahArrayList;
    }

    @NonNull
    @Override
    public CustomerRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new CustomerRVAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.cust_rv, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull CustomerRVAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Nasabah nsb = nasabahArrayList.get(position);
        holder.tvNik.setText(nsb.getNik());
        holder.tvNama.setText(nsb.getNama());
    }

    @Override
    public int getItemCount() {
        return nasabahArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView tvNik;
        private final TextView tvNama;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            tvNik = itemView.findViewById(R.id.tv_nik);
            tvNama = itemView.findViewById(R.id.tv_nama);
        }
    }
}
