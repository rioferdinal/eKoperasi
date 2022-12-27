package com.example.ekoperasi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ekoperasi.Adapter.EmployeeRVAdapter;
import com.example.ekoperasi.Adapter.IncomeRVAdapter;
import com.example.ekoperasi.Model.Income;
import com.example.ekoperasi.Model.Karyawan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class IncomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private IncomeRVAdapter incomeRVAdapter;
    private ArrayList<Income> incomeArrayList;

    private ProgressBar loadingPB;
//    private NestedScrollView nestedScrollView;
    private FloatingActionButton addNewIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        String usertype = getIntent().getStringExtra("USERTYPE");
        String usernik = getIntent().getStringExtra("USERNIK");

        loadingPB = findViewById(R.id.idPBLoading);
        addNewIncome = findViewById(R.id.addIncome);
        incomeArrayList = new ArrayList<>();
        populateData(usertype, usernik);

        addNewIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intentKeuangan = new Intent(getApplicationContext(), AddIncomeActivity.class);
                intentKeuangan.putExtra("USERTYPE", usertype.trim());
                intentKeuangan.putExtra("USERNIK", usernik.trim());
                startActivity(intentKeuangan);
            }
        });
    }

    private void populateData(String usertype, String usernik) {
        int i = 0;
        class ShowIncome extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("nik", usernik);

                //returing the response
                String reqHandler = "";
                reqHandler = requestHandler.sendPostRequest(URLs.URL_GET_INCOME, params);
                System.out.println("reqhandle>> "+reqHandler);
                return reqHandler;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingPB.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loadingPB.setVisibility(View.GONE);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    Boolean status = obj.getBoolean("status");
                    //if no error in response
                    if (status) {
                        //getting the user from the response
                        JSONArray ja = obj.getJSONArray("result");
                        Log.d("respon",""+ja);

                        for(int i=0; i<ja.length(); i++){
                            JSONObject objEmp = ja.getJSONObject(i);
                            String nik = objEmp.getString("nik");
                            String nama = objEmp.getString("nama_karyawan");
                            String tgl_pemasukan = objEmp.getString("tgl_pemasukan");
                            int jml_pemasukan = objEmp.getInt("jumlah");
                            incomeArrayList.add(new Income(nik, nama, tgl_pemasukan, jml_pemasukan));
                        }
                        //init value
                        initAdapter();
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        //executing the async task
        ShowIncome income = new ShowIncome();
        income.execute();
    }

    private void initAdapter() {
        recyclerView = findViewById(R.id.rvIncome);
        incomeRVAdapter = new IncomeRVAdapter(IncomeActivity.this, incomeArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(IncomeActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(incomeRVAdapter);
    }
}