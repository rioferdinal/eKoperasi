package com.example.ekoperasi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddCustomerActivity extends AppCompatActivity {

    EditText etCusNik, etCusNama, etCusEmail, etCusPhone, etCusAddress;
    Button btnSubmitCustData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        String usernik = getIntent().getStringExtra("USERNIK");

        etCusNik = (EditText) findViewById(R.id.etCusNik);
        etCusNama = (EditText) findViewById(R.id.etCusNama);
        etCusEmail = (EditText) findViewById(R.id.etCusEmail);
        etCusPhone = (EditText) findViewById(R.id.etCusPhone);
        etCusAddress = (EditText) findViewById(R.id.etCusAddress);
        btnSubmitCustData = (Button) findViewById(R.id.btnSubmitCustData);

        btnSubmitCustData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCustomerData(usernik);
            }
        });
    }


    private void addCustomerData(String usernik) {
        final String nik = etCusNik.getText().toString().trim();
        final String nik_emp = usernik;
        final String nama = etCusNama.getText().toString().trim();
        final String email = etCusEmail.getText().toString().trim();
        final String nohp = etCusPhone.getText().toString().trim();
        final String alamat = etCusAddress.getText().toString().trim();
        //first we will do the validations

        if (TextUtils.isEmpty(nik)) {
            etCusNik.setError("Mohon masukkan NIK");
            etCusNik.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(nama)) {
            etCusNama.setError("Mohon masukkan nama karyawan");
            etCusNama.requestFocus();
            return;
        }

        //if it passes all the validations
        class addCustomerData extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("nik", nik);
                params.put("nik_emp", nik_emp);
                params.put("nama", nama);
                params.put("email", email);
                params.put("nohp", nohp);
                params.put("alamat", alamat);

                //returing the response
                String reqHandler = "";
                reqHandler = requestHandler.sendPostRequest(URLs.URL_ADD_NASABAH, params);
                return reqHandler;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    boolean status = obj.getBoolean("status");
                    //if no error in response
                    if (status) {
                        new AlertDialog.Builder(AddCustomerActivity.this)
                                .setMessage("Berhasil Menambahkan Data !")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = getIntent();
                                        setResult(RESULT_OK,i);
                                        AddCustomerActivity.this.finish();
                                        Intent custintent = new Intent(getApplicationContext(), CustomerActivity.class);
                                        custintent.putExtra("USERNIK", usernik.trim());
                                        startActivity(custintent);
                                    }
                                })
                                .show();
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        addCustomerData addCus = new addCustomerData();
        addCus.execute();
    }
}
