package id.coolva.dasdisduk.ui.form.damagedlosektp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import id.coolva.dasdisduk.R;

public class RegDamagedLoseKTP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_damaged_lose_ktp);

        getSupportActionBar().setTitle("KTP Rusak/Hilang");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFF));
        getSupportActionBar().setElevation(0F);
    }
}