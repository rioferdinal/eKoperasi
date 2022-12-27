package com.example.ekoperasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ekoperasi.Model.User;

public class MainMenuActivity extends AppCompatActivity {

    Button btKaryawan, btLaporan, btNasabah, btKeuangan, btAkun;
    TextView tvUserName, tvUserNik;
    RelativeLayout menuKaryawan, menuLaporan, menuNasabah, menuKeuangan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        if (SharedPrefManager.getInstance(this).sudahLogin() == false) {
            Intent loginIntent = new Intent(MainMenuActivity.this, LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
            finish();
            return;
        }

        User user = SharedPrefManager.getInstance(this).getUser();
        String usertype = user.getUsername();
        String usernik = user.getNik();
        System.out.println("usertype di mm>> "+usertype);
        System.out.println("usernik di mm>> "+usernik);

        btKaryawan = findViewById(R.id.btKaryawan);
        btLaporan = findViewById(R.id.btLaporan);
        btNasabah = findViewById(R.id.btNasabah);
        btKeuangan = findViewById(R.id.btKeuangan);
        btAkun = findViewById(R.id.btAkun);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserNik = findViewById(R.id.tvUserNik);
        menuKaryawan = findViewById(R.id.menuKaryawan);
        menuLaporan = findViewById(R.id.menuLaporan);
        menuNasabah = findViewById(R.id.menuNasabah);
        menuKeuangan = findViewById(R.id.menuKeuangan);

        tvUserName.setText(usertype);
        tvUserNik.setText(usernik);
        if("admin".equalsIgnoreCase(usertype)){
            menuNasabah.setVisibility(View.GONE);
            menuKeuangan.setVisibility(View.GONE);
        }else{
            menuKaryawan.setVisibility(View.GONE);
            menuLaporan.setVisibility(View.GONE);
        }

        btKaryawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentKaryawan = new Intent(getApplicationContext(), EmployeeActivity.class);
                startActivity(intentKaryawan);
            }
        });

        btLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLaporan = new Intent(getApplicationContext(), ReportActivity.class);
                startActivity(intentLaporan);
            }
        });

        btNasabah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentNasabah = new Intent(getApplicationContext(), CustomerActivity.class);
                intentNasabah.putExtra("USERTYPE", usertype);
                intentNasabah.putExtra("USERNIK", usernik);
                startActivity(intentNasabah);
            }
        });

        btKeuangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentKeuangan = new Intent(getApplicationContext(), IncomeActivity.class);
                intentKeuangan.putExtra("USERTYPE", usertype.trim());
                intentKeuangan.putExtra("USERNIK", usernik.trim());
                startActivity(intentKeuangan);
            }
        });

        btAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("dipencet Profile");
                Intent intentProfile = new Intent(getApplicationContext(), ProfileActivity.class);
                intentProfile.putExtra("Profile", view.getId());
                startActivity(intentProfile);
            }
        });
    }
}