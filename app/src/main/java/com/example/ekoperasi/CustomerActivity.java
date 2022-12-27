package com.example.ekoperasi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ekoperasi.Adapter.CustomerRVAdapter;
import com.example.ekoperasi.Adapter.IncomeRVAdapter;
import com.example.ekoperasi.Model.Nasabah;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerActivity extends AppCompatActivity {

    private RecyclerView nsbRV;
    private ArrayList<Nasabah> nasabahArrayList;
    private CustomerRVAdapter customerRVAdapter;
    private ProgressBar loadingPB;
    private FloatingActionButton addNewCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        nsbRV = findViewById(R.id.rvNasabah);
        loadingPB = findViewById(R.id.idCustPBLoading);
        addNewCustomer = findViewById(R.id.addNewCustomer);
        String usernik = getIntent().getStringExtra("USERNIK");
        nasabahArrayList = new ArrayList<>();
        populateData(usernik);

        addNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerActivity.this.finish();
                Intent custIntent = new Intent(getApplicationContext(), AddCustomerActivity.class);
                custIntent.putExtra("USERNIK", usernik.trim());
                startActivity(custIntent);
            }
        });
    }

    private void populateData(String usernik) {
        int i = 0;
        class ShowNasabah extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("nik", usernik);
                System.out.println("usernik>> "+usernik);

                //returing the response
                String reqHandler = "";
                reqHandler = requestHandler.sendPostRequest(URLs.URL_GET_NASABAH, params);
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
                    System.out.println("status>> "+status);
                    //if no error in response
                    if (status) {
                        //getting the user from the response
                        JSONArray ja = obj.getJSONArray("result");

                        for(int i=0; i<ja.length(); i++){
                            JSONObject objEmp = ja.getJSONObject(i);
                            Nasabah cust = new Nasabah();
                            String nik = objEmp.getString("nik");
                            String nama = objEmp.getString("nama");
                            cust.setNik(nik);
                            cust.setNama(nama);
                            nasabahArrayList.add(cust);
                        }
                        initAdapter();
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        //executing the async task
        ShowNasabah cust = new ShowNasabah();
        cust.execute();
    }

    private void initAdapter() {
        nsbRV = findViewById(R.id.rvNasabah);
        customerRVAdapter = new CustomerRVAdapter(CustomerActivity.this, nasabahArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CustomerActivity.this);
        nsbRV.setLayoutManager(layoutManager);
        nsbRV.setAdapter(customerRVAdapter);
    }
}