package com.example.ekoperasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ekoperasi.Adapter.CustomerRVAdapter;
import com.example.ekoperasi.Adapter.EmployeeRVAdapter;
import com.example.ekoperasi.Model.Karyawan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EmployeeActivity extends AppCompatActivity {
    private RecyclerView empRV;
    private ArrayList<Karyawan> karyawanArrayList;
    private EmployeeRVAdapter empRVAdapter;
    private ProgressBar loadingPB;
    private FloatingActionButton addNewEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        empRV = findViewById(R.id.rvEmployee);
        loadingPB = findViewById(R.id.idPBLoading);
        addNewEmployee = findViewById(R.id.addNewEmployee);
        karyawanArrayList = new ArrayList<>();
        populateData();

        addNewEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), AddEmployeeActivity.class));
            }
        });

        setSwipeToDelete();
    }

    private void setSwipeToDelete(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Karyawan emp = karyawanArrayList.get(viewHolder.getBindingAdapterPosition());
                deleteEmp(emp.getNik());
                int pos = viewHolder.getBindingAdapterPosition();
                karyawanArrayList.remove(viewHolder.getBindingAdapterPosition());
                empRVAdapter.notifyItemRemoved(viewHolder.getBindingAdapterPosition());
                Snackbar.make(empRV, emp.getNama(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        karyawanArrayList.add(pos, emp);
                        empRVAdapter.notifyItemInserted(pos);
                        empRV.scrollToPosition(pos);
                    }
                }).show();
            }

            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 1f;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c,
                                    @NonNull  RecyclerView recyclerView,
                                    @NonNull  RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                setDeleteIcon(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(empRV);
    }

    private void setDeleteIcon(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Paint mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        ColorDrawable mBackground = new ColorDrawable();
        int bgColor = Color.parseColor("#b80f0a");
        Drawable delDrawable = ContextCompat.getDrawable(this, R.drawable.ic_baseline_delete_sweep);
        int intrinsicWidth = delDrawable.getIntrinsicWidth();
        int intrinsicHeight = delDrawable.getIntrinsicHeight();

        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();
        boolean isCancelled = dX == 0 && !isCurrentlyActive;

        if(isCancelled){
            c.drawRect(itemView.getRight() + dX, (float)itemView.getTop(),
                    (float)itemView.getRight(), (float)itemView.getBottom(), mClearPaint);
            return;
        }

        mBackground.setColor(bgColor);
        mBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        mBackground.draw(c);

        int delIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int delIconMargin = (itemHeight - intrinsicHeight) / 2;
        int delIconLeft = itemView.getRight() - delIconMargin - intrinsicWidth;
        int delIconRight = itemView.getRight() - delIconMargin;
        int delIconBottom = delIconTop + intrinsicHeight;

        delDrawable.setBounds(delIconLeft, delIconTop, delIconRight, delIconBottom);
        delDrawable.draw(c);
    }


    private void populateData() {
        int i = 0;
        class ShowKaryawan extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //returing the response
                String reqHandler = "";
                reqHandler = requestHandler.sendPostRequestWithoutParam(URLs.URL_GET_KARYAWAN);
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
                            String nama = objEmp.getString("nama");
                            String email = objEmp.getString("email");
                            String nohp = objEmp.getString("nohp");
                            karyawanArrayList.add(new Karyawan(nik, nama, email, nohp));
                        }
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
        ShowKaryawan emp = new ShowKaryawan();
        emp.execute();
    }

    private void initAdapter() {
        empRV = findViewById(R.id.rvEmployee);
        empRVAdapter = new EmployeeRVAdapter(EmployeeActivity.this, karyawanArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EmployeeActivity.this);
        empRV.setLayoutManager(layoutManager);
        empRV.setAdapter(empRVAdapter);
    }

    private void deleteEmp(String nik) {
        System.out.println("nik>> "+nik);
        class DeleteEmployeeData extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("nik", nik);

                //returing the response
                String reqHandler = "";
                reqHandler = requestHandler.sendPostRequest(URLs.URL_DEL_EMP, params);
                System.out.println("reqhandle>> "+reqHandler);
                return reqHandler;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);
                    boolean status = obj.getBoolean("status");
                    //if no error in response
                    if (status) {
                        new AlertDialog.Builder(EmployeeActivity.this)
                                .setMessage("Berhasil Menghapus Data !")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        DeleteEmployeeData delEmp = new DeleteEmployeeData();
        delEmp.execute();
    }
}