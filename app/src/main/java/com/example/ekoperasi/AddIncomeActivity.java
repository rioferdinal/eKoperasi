package com.example.ekoperasi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddIncomeActivity extends AppCompatActivity {

    private static final String TAG = "AddIncomeActivity";

    TextView tvIncomeDate, tvEmpName, tvEmpNik;
    EditText etIncome;
    Button btnSaveIncome;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-mm-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);
        String usertype = getIntent().getStringExtra("USERTYPE");
        String usernik = getIntent().getStringExtra("USERNIK");
        tvEmpName = (TextView) findViewById(R.id.tvEmpName);
        tvEmpNik = (TextView) findViewById(R.id.tvEmpNik);
        tvIncomeDate = (TextView) findViewById(R.id.tvIncomeDate);
        etIncome = findViewById(R.id.etIncome);
        btnSaveIncome = findViewById(R.id.btnSaveIncome);

        setDefaultData(usertype, usernik);

        tvIncomeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog dialog = new DatePickerDialog(
                        AddIncomeActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        mYear,mMonth,mDay);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month +=1;
                Log.d(TAG,"onDateSet: date: "+day+"/"+month+"/"+year);
                String date = year + "-" + month +"-"+ day;
                tvIncomeDate.setText(date);
            }
        };

        btnSaveIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addIncomeData(usertype, usernik);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addIncomeData(String usertype, String usernik) throws ParseException {
        final String nik = tvEmpNik.getText().toString().trim();
        final String nama = tvEmpName.getText().toString().trim();
        final String tanggal = tvIncomeDate.getText().toString().trim();
        System.out.println("tanggal>> "+tanggal);
        Date d = simpleDate.parse(tanggal);
        final String jumlah = etIncome.getText().toString().trim();
        //first we will do the validations

        if (TextUtils.isEmpty(tanggal)) {
            tvIncomeDate.setError("Mohon masukkan tanggal transaksi");
            tvIncomeDate.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(jumlah)) {
            etIncome.setError("Mohon masukkan jumlah pemasukan");
            etIncome.requestFocus();
            return;
        }

        //if it passes all the validations
        class AddIncomeData extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("nik", nik);
                params.put("nama_karyawan", nama);
                params.put("tgl_pemasukan", simpleDate.format(d));
                params.put("jumlah", jumlah);

                //returing the response
                String reqHandler = "";
                reqHandler = requestHandler.sendPostRequest(URLs.URL_ADD_INCOME, params);
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
                        new AlertDialog.Builder(AddIncomeActivity.this)
                                .setMessage("Berhasil Menambahkan Data !")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = getIntent();
                                        setResult(RESULT_OK,i);
                                        AddIncomeActivity.this.finish();
                                        Intent incintent = new Intent(getApplicationContext(), IncomeActivity.class);
                                        incintent.putExtra("USERTYPE", usertype.trim());
                                        incintent.putExtra("USERNIK", usernik.trim());
                                        startActivity(incintent);
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
        AddIncomeData incomeData = new AddIncomeData();
        incomeData.execute();
    }

    private void setDefaultData(String usertype, String usernik){
        tvEmpName.setText(usertype);
        tvEmpNik.setText(usernik);
    }
}