package id.coolva.dasdisduk.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import id.coolva.dasdisduk.R;
import id.coolva.dasdisduk.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordActivity extends AppCompatActivity {
    ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnForgotPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(binding.edtEmail.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ForgotPasswordActivity.this, "Periksa Email Anda", Toast.LENGTH_LONG).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.btnForgotPw.setEnabled(false);
                                    }
                                }, 10000);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ForgotPasswordActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}