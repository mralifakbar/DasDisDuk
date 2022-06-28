package id.coolva.dasdisduk.ui.auth;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import id.coolva.dasdisduk.R;
import id.coolva.dasdisduk.data.preference.UserModel;
import id.coolva.dasdisduk.data.preference.UserPreference;
import id.coolva.dasdisduk.databinding.ActivityRegisterBinding;
import id.coolva.dasdisduk.ui.main.MainActivity;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private Uri selectedImageUriSelfie;
    public String selectedImageUriSelfiePath = "";
    private UserModel userModel;
    private UserPreference userPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String[] provinsiarr = getResources().getStringArray(R.array.provinsi);
        String[] kabupatenLampungarr = getResources().getStringArray(R.array.kabupatenkotalampung);
        String[] kabupatenSumselarr = getResources().getStringArray(R.array.kabupatenkotasumsel);
        ArrayAdapter<String> arrayAdapterProvinsi = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, provinsiarr);
        final String[] selectedProvinsi = new String[1];
        String selectedKabupaten;
        binding.actProvinsi.setAdapter(arrayAdapterProvinsi);

        binding.actProvinsi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedProvinsi[0] = arrayAdapterProvinsi.getItem(i);

                Log.d("Provinsi", selectedProvinsi[0]);
                if (selectedProvinsi[0].equals("Lampung")) {
                    ArrayAdapter<String> arrayAdapterLampung = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, kabupatenLampungarr);
                    binding.actKabupaten.setAdapter(arrayAdapterLampung);
                } else if (selectedProvinsi[0].equals("Sumatera Selatan")) {
                    ArrayAdapter<String> arrayAdapterSumsel = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, kabupatenSumselarr);
                    binding.actKabupaten.setAdapter(arrayAdapterSumsel);
                }
            }
        });

        binding.btnUploadSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission( RegisterActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            RegisterActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1
                    );
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    sActivityResultLauncher.launch(galleryIntent);
                }
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.edtEmail.getText().toString();
                String password = binding.edtPwRes.getText().toString();
                String confPassword = binding.edtPwResAgain.getText().toString();
                String NIK = binding.edtNik.getText().toString();
                String noKK = binding.edtNoKk.getText().toString();
                String fullName = binding.edtNama.getText().toString();
                String noWa = binding.edtNoWa.getText().toString();
                String address = binding.edtAlamat.getText().toString();
                String provinsi = binding.actProvinsi.getText().toString();
                String kabupaten = binding.actKabupaten.getText().toString();

                Boolean valid = true;

                if (password.compareTo(confPassword) != 0 && !password.isEmpty()) {
                    binding.edtPwResAgain.setError("Password harus sama!");
                    valid = false;
                }
                if (email.isEmpty()) {
                    binding.edtEmail.setError("Tidak boleh kosong");
                    valid = false;
                }
                if (password.isEmpty()) {
                    binding.edtPwRes.setError("Tidak boleh kosong");
                    valid = false;
                }
                if (confPassword.isEmpty()) {
                    binding.edtPwResAgain.setError("Tidak boleh kosong");
                    valid = false;
                }
                if (NIK.isEmpty()) {
                    binding.edtNik.setError("Tidak boleh kosong");
                    valid = false;
                }
                if (noKK.isEmpty()) {
                    binding.edtNoKk.setError("Tidak boleh kosong");
                    valid = false;
                }
                if (fullName.isEmpty()) {
                    binding.edtNama.setError("Tidak boleh kosong");
                    valid = false;
                }
                if (noWa.isEmpty()) {
                    binding.edtNoWa.setError("Tidak boleh kosong");
                    valid = false;
                }
                if (address.isEmpty()) {
                    binding.edtAlamat.setError("Tidak boleh kosong");
                    valid = false;
                }
                if (provinsi.isEmpty()) {
                    binding.actProvinsi.setError("Tidak boleh kosong");
                    valid = false;
                }
                if (kabupaten.isEmpty()) {
                    binding.actKabupaten.setError("Tidak boleh kosong");
                    valid = false;
                }
                if (selectedImageUriSelfie == null) {
                    Toast.makeText(RegisterActivity.this, "Foto selfi belum terupload", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
                if (!binding.cbKonfir.isChecked()) {
                    Toast.makeText(RegisterActivity.this, "Anda belum setuju", Toast.LENGTH_SHORT).show();
                    valid = false;
                }

                if (valid) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {

                                                Log.d("TaskReg", "Success");
                                                FirebaseUser firebaseUser = task.getResult().getUser();
                                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                Random rand = new Random();
                                                int randInt = rand.nextInt(10000);

                                                String imageExtension = MimeTypeMap.getSingleton().getExtensionFromMimeType(
                                                        getContentResolver().getType(selectedImageUriSelfie)
                                                );
                                                String child = "pp-" + firebaseUser.getUid().toString() + "-" + randInt + "." + imageExtension;
                                                StorageReference sRef = FirebaseStorage.getInstance().getReference().child(
                                                        child
                                                );

                                                sRef.putFile(selectedImageUriSelfie);

                                                Map<String, Object> userData = new HashMap<>();
                                                userData.put("uid", firebaseUser.getUid().toString());
                                                userData.put("nik", NIK);
                                                userData.put("nokk", noKK);
                                                userData.put("nama", fullName);
                                                userData.put("nowa", noWa);
                                                userData.put("alamat", address);
                                                userData.put("provinsi", provinsi);
                                                userData.put("kabupaten", kabupaten);
                                                userData.put("ktpbaru", 0);
                                                userData.put("ktprusakhilang", 0);
                                                userData.put("kkbaru", 0);
                                                userData.put("kkrusakhilang", 0);
                                                userData.put("pp",
                                                        "https://firebasestorage.googleapis.com/v0/b/dasdisduk.appspot.com/o/" + child + "?alt=media");

                                                db.collection("users")
                                                        .document(firebaseUser.getUid().toString())
                                                        .set(userData, SetOptions.merge())
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Log.d("DBUSER", "Berhasil");
                                                                userModel = new UserModel();
                                                                userPreference = new UserPreference(RegisterActivity.this);
                                                                userModel = userPreference.getUser();
                                                                userModel.setEmail(email);
                                                                userModel.setPassword(password);
                                                                userPreference.setUser(userModel);
                                                                Toast.makeText(RegisterActivity.this, "Anda berhasil mendaftar!", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                                                finish();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(RegisterActivity.this, "Gagal mengirim data pendafataran", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });


                                            } else {
                                                Toast.makeText(RegisterActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                            );
                }

            }
        });

        binding.btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    ActivityResultLauncher<Intent> sActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        selectedImageUriSelfie = data.getData();
                        Log.d("Selfie", selectedImageUriSelfie.toString());
                        binding.btnUploadSelfie.setText("Terupload");
                        binding.btnUploadSelfie.setBackgroundColor(Color.parseColor("#1dd1a1"));
                        binding.btnUploadSelfie.setTextColor(Color.parseColor("#ffffff"));
                        binding.btnUploadSelfie.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#1dd1a1")));
                    }
                }
            }
    );

    private boolean doubleBackToExit = false;

    @Override
    public void onBackPressed() {
        Log.d("Sampe", "Sini Klik");
        if (doubleBackToExit) {
            Log.d("Sampe", "Sini Klik2");
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setTitle("Keluar aplikasi");
            builder.setMessage("Apakah yakin ingin keluar aplikasi?")
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