package com.example.ekoperasi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ekoperasi.Model.Income;
import com.example.ekoperasi.Model.Karyawan;
import com.example.ekoperasi.R;

import java.util.ArrayList;

public class IncomeRVAdapter extends RecyclerView.Adapter<IncomeRVAdapter.IncomeViewHolder> {

    private Context context;
    private ArrayList<Income> incomeArrayList;

    // creating a constructor class.
    public IncomeRVAdapter(Context context, ArrayList<Income> incomeArrayList) {
        this.context = context;
        this.incomeArrayList = incomeArrayList;
    }

    @Override
    public IncomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.inc_rv, parent, false);
        return new IncomeViewHolder(view);
    }


    @Override
    public void onBindViewHolder(IncomeViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Income income = incomeArrayList.get(position);
        holder.tvNik.setText(income.getNik());
        holder.tvNama.setText(income.getNama());
        holder.tvTglpemasukan.setText((CharSequence) income.getTglpemasukan());
        holder.tvJmlpemasukan.setText(Integer.toString(income.getJmlpemasukan()));
    }

    @Override
    public int getItemCount() {
        return incomeArrayList.size();
    }

    public class IncomeViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private TextView tvNik, tvNama, tvTglpemasukan, tvJmlpemasukan;

        public IncomeViewHolder(View itemView) {
            super(itemView);
            tvNik = itemView.findViewById(R.id.tv_nik);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvTglpemasukan = itemView.findViewById(R.id.tv_incdate);
            tvJmlpemasukan = itemView.findViewById(R.id.tv_incamt);
        }
    }
}
