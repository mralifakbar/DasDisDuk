package id.coolva.dasdisduk.ui.form;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import id.coolva.dasdisduk.R;

public class RegNewKTP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_new_ktp);

        getSupportActionBar().setTitle("Buat KTP Baru");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFF));
        getSupportActionBar().setElevation(0F);
    }
}