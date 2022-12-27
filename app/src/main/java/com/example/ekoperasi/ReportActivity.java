package com.example.ekoperasi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ekoperasi.Adapter.IncomeReportRVAdapter;
import com.example.ekoperasi.ExcelUtils.ExcelUtils;
import com.example.ekoperasi.Model.Income;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

    int count = 0;
    private RecyclerView incReportRv;
    private ArrayList<Income> incomeArrayList;
    private IncomeReportRVAdapter incomeReportRVAdapter;
    private ProgressBar loadingPB;
    private NestedScrollView nestedScrollView;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TextView tvDateFrom, tvDateTo;
    private EditText evEmpName;
    private Button btnSearch, btnExport;
    Calendar myCalendar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        incReportRv = findViewById(R.id.rvIncomeReport);
        loadingPB = findViewById(R.id.idPBLoading);
        nestedScrollView = findViewById(R.id.idNestedSV);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        myCalendar = Calendar.getInstance();

        tvDateFrom = (TextView) findViewById(R.id.tv_dateFrom);
        tvDateTo = (TextView) findViewById(R.id.tv_dateTo);
        evEmpName = (EditText) findViewById(R.id.etEmpName);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnExport = (Button) findViewById(R.id.btnExport);

        tvDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog("from");
            }
        });
        tvDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog("to");
            }
        });
        incomeArrayList = new ArrayList<>();
        populateData();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        incReportRv.setLayoutManager(manager);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateData();
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isExcelGenerated = ExcelUtils.exportDataIntoWorkbook(getApplication(),
                        "report.xlsx", incomeArrayList);
                Toast.makeText(getApplicationContext(), "Export Data Pemasukan Berhasil, silahkan buka di folder penyimpanan anda!", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void showDateDialog(String type){

        new RackMonthPicker(this)
                .setLocale(Locale.ENGLISH)
                .setNegativeButton(Dialog::hide)
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                        if(month < 10){
                            if ("from".equalsIgnoreCase(type)) {
                                tvDateFrom.setText(year+"-"+"0"+month);
                            }else{
                                tvDateTo.setText(year+"-"+"0"+month);
                            }
                        }else{
                            if ("from".equalsIgnoreCase(type)) {
                                tvDateFrom.setText(year+"-"+"0"+month);
                            }else{
                                tvDateTo.setText(year+"-"+"0"+month);
                            }
                        }
                    }
                }).show();
    }

    private void populateData() {
        int i = 0;
        final String empName = evEmpName.getText().toString();
        final String tgl_dari = "Dari".equalsIgnoreCase(tvDateFrom.getText().toString()) ? "" : tvDateFrom.getText().toString();
        final String tgl_ke = "Ke".equalsIgnoreCase(tvDateTo.getText().toString()) ? "" : tvDateTo.getText().toString();
        System.out.println("empname>> "+ empName+ ">> tgldari >> "+tgl_dari+">>tgl ke>> "+tgl_ke);
        class ShowReport extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();
                HashMap<String, String> params = new HashMap<>();
                params.put("nama_karyawan", empName);
                params.put("tgl_dari", tgl_dari);
                params.put("tgl_ke", tgl_ke);

                //returing the response
                String reqHandler = "";
                reqHandler = requestHandler.sendPostRequest(URLs.URL_GET_REPORT, params);
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
                        incomeArrayList = new ArrayList<>();

                        for(int i=0; i<ja.length(); i++){
                            JSONObject objEmp = ja.getJSONObject(i);
                            Income income = new Income();
                            income.setNama(objEmp.getString("nama_karyawan"));
                            income.setTglpemasukan(objEmp.getString("tgl_pemasukan"));
                            income.setJmlpemasukan(objEmp.getInt("jumlah"));
                            incomeArrayList.add(income);
                            initAdapter();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        //executing the async task
        ShowReport report = new ShowReport();
        report.execute();
    }

    private void initAdapter() {
        incomeReportRVAdapter = new IncomeReportRVAdapter(ReportActivity.this, incomeArrayList);
        incReportRv.setAdapter(incomeReportRVAdapter);
    }
}