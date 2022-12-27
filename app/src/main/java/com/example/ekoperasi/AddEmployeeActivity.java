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

public class AddEmployeeActivity extends AppCompatActivity {


    EditText etEmpNik, etEmpNama, etEmpEmail, etEmpPhone;
    Button btnSubmitData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        etEmpNik = (EditText) findViewById(R.id.etEmpNik);
        etEmpNama = (EditText) findViewById(R.id.etEmpNama);
        etEmpEmail = (EditText) findViewById(R.id.etEmpEmail);
        etEmpPhone = (EditText) findViewById(R.id.etEmpPhone);
        btnSubmitData = (Button) findViewById(R.id.btnSubmitData);

        btnSubmitData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEmployeeData();
//                addUser();
            }
        });
    }

    private void addEmployeeData() {
        final String nik = etEmpNik.getText().toString().trim();
        final String nama = etEmpNama.getText().toString().trim();
        final String email = etEmpEmail.getText().toString().trim();
        final String nohp = etEmpPhone.getText().toString().trim();
        String password = "";
        if(nama.contains(" ")){
            password = nama.substring(0, nama.indexOf(" ")) + "123";
        }else{
            password = nama+"123";
        }

        System.out.println("password>> "+password);
        //first we will do the validations

        if (TextUtils.isEmpty(nik)) {
            etEmpNik.setError("Mohon masukkan NIK");
            etEmpNik.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(nama)) {
            etEmpNama.setError("Mohon masukkan nama karyawan");
            etEmpNama.requestFocus();
            return;
        }

        //if it passes all the validations
        String finalPassword = password;
        class AddEmployeeData extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("nik", nik);
                params.put("nama", nama);
                params.put("email", email);
                params.put("nohp", nohp);
                params.put("password", finalPassword);

                //returing the response
                String reqHandler = "";
                reqHandler = requestHandler.sendPostRequest(URLs.URL_ADD_KARYAWAN, params);
                System.out.println("reqhandle>> "+reqHandler);
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
                        new AlertDialog.Builder(AddEmployeeActivity.this)
                                .setMessage("Berhasil Menambahkan Data !")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = getIntent();
                                        setResult(RESULT_OK,i);
                                        AddEmployeeActivity.this.finish();
                                        startActivity(new Intent(getApplicationContext(), EmployeeActivity.class));
                                    }
                                })
                                .show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        AddEmployeeData addEmp = new AddEmployeeData();
        addEmp.execute();
    }
}