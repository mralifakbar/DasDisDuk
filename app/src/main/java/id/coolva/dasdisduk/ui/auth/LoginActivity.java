package id.coolva.dasdisduk.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import id.coolva.dasdisduk.data.preference.UserModel;
import id.coolva.dasdisduk.data.preference.UserPreference;
import id.coolva.dasdisduk.databinding.ActivityLoginBinding;
import id.coolva.dasdisduk.ui.main.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private UserModel userModel;
    private UserPreference userPreference;
    private boolean isPreferenceEmpty = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userModel = new UserModel();
        userPreference = new UserPreference(LoginActivity.this);
        userModel = userPreference.getUser();

        if (!userModel.getEmail().isEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(userModel.getEmail(), userModel.getPassword())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Berhasil masuk!", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        } else {
            Log.d("Data", "Kosong");
        }

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.edtEmail.getText().toString().trim();
                String password = binding.edtPw.getText().toString();
                if (binding.edtEmail.getText().toString().isEmpty()) {
                    binding.edtEmail.setError("Harus diisi");
                }
                if (binding.edtPw.getText().toString().isEmpty()) {
                    binding.edtPw.setError("Harus diisi");
                }
                else {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Berhasil masuk!", Toast.LENGTH_SHORT).show();
                                        userModel.setEmail(email);
                                        userModel.setPassword(password);
                                        userPreference.setUser(userModel);
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }

        });
        binding.btnForgotPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
        binding.btnToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }

    private boolean doubleBackToExit = false;

    @Override
    public void onBackPressed() {
        Log.d("Sampe", "Sini Klik");
        if (doubleBackToExit) {
            Log.d("Sampe", "Sini Klik2");
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setTitle("Keluar dari akun");
            builder.setMessage("Apakah yakin ingin keluar")
                    .setCancelable(false)
                    .setPositiveButton("Yakin", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        doubleBackToExit = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExit = false;
            }
        }, 2000);
    }
}