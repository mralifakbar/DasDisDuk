package id.coolva.dasdisduk.ui.form.newkk;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import id.coolva.dasdisduk.R;

public class RegNewKK extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_kk);

        getSupportActionBar().setTitle("Buat KK Baru");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFF));
        getSupportActionBar().setElevation(0F);
    }
}