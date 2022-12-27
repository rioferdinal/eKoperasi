package com.example.ekoperasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ekoperasi.Model.Karyawan;
import com.example.ekoperasi.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewUsername, textViewEmail, textViewPhone, textViewNik, textViewAddress;
    private ProgressBar loadingPB;
    private Karyawan emp = new Karyawan();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //if the user is not logged in
        //starting the login activity
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewNik = (TextView) findViewById(R.id.textViewNik);
        textViewPhone = (TextView) findViewById(R.id.textViewPhone);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        loadingPB = findViewById(R.id.idPBLoading);

        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        //setting the values to the textviews
        defaultData(user.getEmail());

        //when the user presses logout button
        //calling the logout method
        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SharedPrefManager.getInstance(getApplicationContext()).logout();
                SharedPrefManager.getInstance(getApplicationContext()).saveSPBoolean("sudah_login", false);
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void defaultData(String uemail) {
        //first getting the values
        final String email = uemail;

        //if everything is fine
        class DefaultData extends AsyncTask<Void, Void, String> {

            ProgressBar progressBar;

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
                    JSONObject obj = new JSONObject(s);
                    Boolean status = obj.getBoolean("status");
                    if (status) {
                        JSONArray ja = obj.getJSONArray("result");
                        Log.d("respon",""+ja);

                        for(int i=0; i<ja.length(); i++){
                            JSONObject objEmp = ja.getJSONObject(i);
                            emp.setNik(objEmp.getString("nik"));
                            emp.setNama(objEmp.getString("nama"));
                            emp.setEmail(objEmp.getString("email"));
                            emp.setNohp(objEmp.getString("nohp"));
                            emp.setAlamat(objEmp.getString("alamat"));
                            initData();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("email", uemail);

                //returing the response
                String reqHandler = "";
                reqHandler = requestHandler.sendPostRequest(URLs.URL_GET_KARYAWAN, params);
                System.out.println("reqhandle>> "+reqHandler);
                return reqHandler;
            }
        }

        DefaultData dd = new DefaultData();
        dd.execute();
    }

    private void initData(){
        textViewUsername.setText("ganteng");
        textViewEmail.setText(emp.getEmail());
        textViewNik.setText(emp.getNik());
        textViewPhone.setText(emp.getNohp());
        textViewAddress.setText(emp.getAlamat());
    }

}