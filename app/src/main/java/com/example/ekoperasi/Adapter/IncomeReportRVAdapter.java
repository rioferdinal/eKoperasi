package com.example.ekoperasi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ekoperasi.Model.Income;
import com.example.ekoperasi.R;

import java.util.ArrayList;

public class IncomeReportRVAdapter extends RecyclerView.Adapter<IncomeReportRVAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Income> incomeArrayList;

    // creating a constructor class.
    public IncomeReportRVAdapter(Context context, ArrayList<Income> incomeArrayList) {
        this.context = context;
        this.incomeArrayList = incomeArrayList;
    }

    @NonNull
    @Override
    public IncomeReportRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.inc_report_rv, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull IncomeReportRVAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Income income = incomeArrayList.get(position);
        holder.tvNama.setText(income.getNama());
        holder.tvJmlpemasukan.setText(Integer.toString(income.getJmlpemasukan()));
    }

    @Override
    public int getItemCount() {
        return incomeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView tvNama;
        private final TextView tvJmlpemasukan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvJmlpemasukan = itemView.findViewById(R.id.tv_incamt);
        }
    }
}
