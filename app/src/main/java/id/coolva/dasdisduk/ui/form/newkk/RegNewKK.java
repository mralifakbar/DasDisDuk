package id.coolva.dasdisduk.ui.form.newkk;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import id.coolva.dasdisduk.R;
import id.coolva.dasdisduk.databinding.ActivityNewKkBinding;
import id.coolva.dasdisduk.ui.form.newktp.RegNewKTP;
import id.coolva.dasdisduk.ui.main.MainActivity;

public class RegNewKK extends AppCompatActivity {
    private ActivityNewKkBinding binding;
    private Uri selectedImageKK;
    private Uri selectedImageUriSelfie;
    private Uri selectedImageBukuNikah;
    private int fileSelectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewKkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Buat KK Baru");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFF));
        getSupportActionBar().setElevation(0F);

        String[] agama = getResources().getStringArray(R.array.agama);
        String[] jk = getResources().getStringArray(R.array.jeniskelamin);

        ArrayAdapter<String> arrayAdapterAgama = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, agama);
        ArrayAdapter<String> arrayAdapterJk= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, jk);

        binding.actAgama.setAdapter(arrayAdapterAgama);
        binding.actJenisKelamin.setAdapter(arrayAdapterJk);

        binding.btnUploadKk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileSelectedIndex = 1;
                if (ActivityCompat.checkSelfPermission( RegNewKK.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            RegNewKK.this,
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

        binding.btnUploadSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileSelectedIndex = 2;
                if (ActivityCompat.checkSelfPermission( RegNewKK.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            RegNewKK.this,
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

        binding.btnUploadBukuNikah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileSelectedIndex = 3;
                if (ActivityCompat.checkSelfPermission( RegNewKK.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            RegNewKK.this,
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
                if (binding.edtNama.getText().toString().isEmpty() || binding.edtTanggalLahir.getText().toString().isEmpty()
                || binding.actJenisKelamin.getText().toString().isEmpty() || binding.edtTempatLahir.getText().toString().isEmpty()
                || binding.edtPendidikan.getText().toString().isEmpty() || binding.actAgama.getText().toString().isEmpty()
                || binding.edtPekerjaan.getText().toString().isEmpty()) {
                    Toast.makeText(RegNewKK.this, "Tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                } else if (selectedImageKK == null || selectedImageUriSelfie == null) {
                    Toast.makeText(RegNewKK.this, "Semua gambar harus diupload!", Toast.LENGTH_SHORT).show();
                } else  if (!binding.cbKonfir.isChecked()) {
                    Toast.makeText(RegNewKK.this, "Anda harus setuju!", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Random rand = new Random();
                    int randInt = rand.nextInt(10000);

                    String imageExtensionSelfie = MimeTypeMap.getSingleton().getExtensionFromMimeType(
                            getContentResolver().getType(selectedImageUriSelfie)
                    );

                    String imageExtensionKK = MimeTypeMap.getSingleton().getExtensionFromMimeType(
                            getContentResolver().getType(selectedImageKK)
                    );

                    String imageExtensionBukuNikah = MimeTypeMap.getSingleton().getExtensionFromMimeType(
                            getContentResolver().getType(selectedImageBukuNikah)
                    );

                    String childSelfie = "slkk-" + firebaseUser.getUid().toString() + "-" + randInt + "." + imageExtensionSelfie;
                    String childKK = "kkkk-" + firebaseUser.getUid().toString() + "-" + randInt + "." + imageExtensionKK;
                    String childBukuNikah = "bkkk-" + firebaseUser.getUid().toString() + "-" + randInt + "." + imageExtensionBukuNikah;

                    StorageReference sRefSelfie = FirebaseStorage.getInstance().getReference().child(
                            childSelfie
                    );
                    sRefSelfie.putFile(selectedImageUriSelfie);

                    StorageReference sRefKK = FirebaseStorage.getInstance().getReference().child(
                            childKK
                    );
                    sRefKK.putFile(selectedImageKK);

                    StorageReference sRefBukuNikah = FirebaseStorage.getInstance().getReference().child(
                            childBukuNikah
                    );
                    sRefBukuNikah.putFile(selectedImageBukuNikah);

                    Map<String, Object> updateNewKK = new HashMap<>();
                    updateNewKK.put("kkbaru", 1);
                    db.collection("users").document(firebaseUser.getUid())
                            .set(updateNewKK, SetOptions.merge());

                    Map<String, Object> newKKData = new HashMap<>();
                    newKKData.put("uid", firebaseUser.getUid());
                    newKKData.put("tempatlahir", binding.edtTempatLahir.getText().toString());
                    newKKData.put("tanggallahir", binding.edtTanggalLahir.getText().toString());
                    newKKData.put("jeniskelamin", binding.actJenisKelamin.getText().toString());
                    newKKData.put("agama", binding.actAgama.getText().toString());
                    newKKData.put("pendidikan", binding.edtPendidikan.getText().toString());
                    newKKData.put("pekerjaan", binding.edtPekerjaan.getText().toString());
                    newKKData.put("status", "Pengajuan Baru");
                    newKKData.put("fotokklama",
                            "https://firebasestorage.googleapis.com/v0/b/dasdisduk.appspot.com/o/" + childKK + "?alt=media");
                    newKKData.put("fotoselfie",
                            "https://firebasestorage.googleapis.com/v0/b/dasdisduk.appspot.com/o/" + childSelfie + "?alt=media");
                    newKKData.put("fotobukunikah",
                            "https://firebasestorage.googleapis.com/v0/b/dasdisduk.appspot.com/o/" + childBukuNikah + "?alt=media");
                    newKKData.put("selesai", false);
                    newKKData.put("diterima", false);
                    newKKData.put("tanggalpengajuan", System.currentTimeMillis());

                    db.collection("kkbaru")
                            .document(firebaseUser.getUid().toString())
                            .set(newKKData, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(RegNewKK.this, "Berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegNewKK.this, MainActivity.class));
                                    finish();
                                }
                            });
                }
            }
        });
    }


    ActivityResultLauncher<Intent> sActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        if (fileSelectedIndex == 1) {
                            selectedImageKK = data.getData();
                            binding.btnUploadKk.setText("Terupload");
                            binding.btnUploadKk.setBackgroundColor(Color.parseColor("#1dd1a1"));
                            binding.btnUploadKk.setTextColor(Color.parseColor("#ffffff"));
                            binding.btnUploadKk.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#1dd1a1")));
                        } else if (fileSelectedIndex == 2) {
                            selectedImageUriSelfie = data.getData();
                            binding.btnUploadSelfie.setText("Terupload");
                            binding.btnUploadSelfie.setBackgroundColor(Color.parseColor("#1dd1a1"));
                            binding.btnUploadSelfie.setTextColor(Color.parseColor("#ffffff"));
                            binding.btnUploadSelfie.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#1dd1a1")));
                        } else if (fileSelectedIndex == 3) {
                            selectedImageBukuNikah = data.getData();
                            binding.btnUploadBukuNikah.setText("Terupload");
                            binding.btnUploadBukuNikah.setBackgroundColor(Color.parseColor("#1dd1a1"));
                            binding.btnUploadBukuNikah.setTextColor(Color.parseColor("#ffffff"));
                            binding.btnUploadBukuNikah.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#1dd1a1")));
                        }
                    }
                }
            }
    );
}