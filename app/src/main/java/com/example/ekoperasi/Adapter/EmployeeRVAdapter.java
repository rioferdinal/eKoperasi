package com.example.ekoperasi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ekoperasi.Model.Karyawan;
import com.example.ekoperasi.R;

import java.util.ArrayList;

public class EmployeeRVAdapter extends RecyclerView.Adapter<EmployeeRVAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Karyawan> karyawanArrayList;

    // creating a constructor class.
    public EmployeeRVAdapter(Context context, ArrayList<Karyawan> karyawanArrayList) {
        this.context = context;
        this.karyawanArrayList = karyawanArrayList;
    }

    @NonNull
    @Override
    public EmployeeRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.emp_rv, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeRVAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Karyawan emp = karyawanArrayList.get(position);
        holder.tvNik.setText(emp.getNik());
        holder.tvNama.setText(emp.getNama());
        holder.tvEmail.setText(emp.getEmail());
        holder.tvPhone.setText(emp.getNohp());
    }

    @Override
    public int getItemCount() {
        return karyawanArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView tvNik;
        private final TextView tvNama;
        private final TextView tvEmail;
        private final TextView tvPhone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            tvNik = itemView.findViewById(R.id.tv_nik);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvPhone = itemView.findViewById(R.id.tv_nohp);
        }
    }
}
