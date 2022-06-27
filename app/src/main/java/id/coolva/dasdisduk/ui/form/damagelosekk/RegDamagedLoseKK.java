package id.coolva.dasdisduk.ui.form.damagelosekk;

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
import id.coolva.dasdisduk.databinding.ActivityRegDamagedLoseKkBinding;
import id.coolva.dasdisduk.ui.form.damagedlosektp.RegDamagedLoseKTP;
import id.coolva.dasdisduk.ui.main.MainActivity;

public class RegDamagedLoseKK extends AppCompatActivity {
    private ActivityRegDamagedLoseKkBinding binding;
    private Uri selectedSuratHilangKepolisian;
    private Uri selectedFotoKtp;
    private int fileSelectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegDamagedLoseKkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("KK Rusak/Hilang");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFF));
        getSupportActionBar().setElevation(0F);

        String[] jenisPengajuan = getResources().getStringArray(R.array.jenispengajuan);
        ArrayAdapter<String> arrayAdapterJenisPengajuan = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, jenisPengajuan);
        binding.actJenisPengajuan.setAdapter(arrayAdapterJenisPengajuan);

        binding.btnUploadSuratHilang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileSelectedIndex = 1;
                if (ActivityCompat.checkSelfPermission( RegDamagedLoseKK.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            RegDamagedLoseKK.this,
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

        binding.btnUploadFotoKtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileSelectedIndex = 2;
                if (ActivityCompat.checkSelfPermission( RegDamagedLoseKK.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            RegDamagedLoseKK.this,
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
                if (binding.actJenisPengajuan.getText().toString().isEmpty() || binding.edtTanggalHilangRusak.getText().toString().isEmpty()
                || binding.edtNoKk.getText().toString().isEmpty()) {
                    Toast.makeText(RegDamagedLoseKK.this, "Field tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                } else if (selectedFotoKtp == null || selectedSuratHilangKepolisian == null) {
                    Toast.makeText(RegDamagedLoseKK.this, "Semua gambar harus diupload!", Toast.LENGTH_SHORT).show();
                } else  if (!binding.cbKonfir.isChecked()) {
                    Toast.makeText(RegDamagedLoseKK.this, "Anda harus setuju!", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Random rand = new Random();
                    int randInt = rand.nextInt(10000);

                    String imageExtensionHilang = MimeTypeMap.getSingleton().getExtensionFromMimeType(
                            getContentResolver().getType(selectedSuratHilangKepolisian)
                    );

                    String imageExtensionKTP = MimeTypeMap.getSingleton().getExtensionFromMimeType(
                            getContentResolver().getType(selectedFotoKtp)
                    );

                    String childSuratHilang = "shkkold-" + firebaseUser.getUid().toString() + "-" + randInt + "." + imageExtensionHilang;
                    String childKTP = "ktpkkold-" + firebaseUser.getUid().toString() + "-" + randInt + "." + imageExtensionKTP;

                    StorageReference sRefSuratHilang = FirebaseStorage.getInstance().getReference().child(
                            childSuratHilang
                    );
                    sRefSuratHilang.putFile(selectedSuratHilangKepolisian);

                    StorageReference sRefKTP= FirebaseStorage.getInstance().getReference().child(
                            childKTP
                    );
                    sRefKTP.putFile(selectedFotoKtp);

                    Map<String, Object> updateNewKk = new HashMap<>();
                    updateNewKk.put("kkrusakhilang", 1);
                    db.collection("users").document(firebaseUser.getUid())
                            .set(updateNewKk, SetOptions.merge());

                    Map<String, Object> newKKData = new HashMap<>();
                    newKKData.put("uid", firebaseUser.getUid());
                    newKKData.put("jenispengajuan", binding.actJenisPengajuan.getText().toString());
                    newKKData.put("tanggalhilangrusak", binding.edtTanggalHilangRusak.getText().toString());
                    newKKData.put("nokk", binding.edtNoKk.getText().toString());
                    newKKData.put("fotosurathilang",
                            "https://firebasestorage.googleapis.com/v0/b/dasdisduk.appspot.com/o/" + childSuratHilang + "?alt=media");
                    newKKData.put("fotoktp",
                            "https://firebasestorage.googleapis.com/v0/b/dasdisduk.appspot.com/o/" + childKTP + "?alt=media");
                    newKKData.put("tanggalpengajuan", System.currentTimeMillis());
                    newKKData.put("status", "Pengajuan Baru");
                    newKKData.put("selesai", false);
                    newKKData.put("diterima", false);

                    db.collection("kkhilangrusak")
                            .document(firebaseUser.getUid().toString())
                            .set(newKKData, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(RegDamagedLoseKK.this, "Berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegDamagedLoseKK.this, MainActivity.class));
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
                            selectedSuratHilangKepolisian = data.getData();
                            binding.btnUploadSuratHilang.setText("Terupload");
                            binding.btnUploadSuratHilang.setBackgroundColor(Color.parseColor("#1dd1a1"));
                            binding.btnUploadSuratHilang.setTextColor(Color.parseColor("#ffffff"));
                            binding.btnUploadSuratHilang.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#1dd1a1")));
                        } else if (fileSelectedIndex == 2) {
                            selectedFotoKtp = data.getData();
                            binding.btnUploadFotoKtp.setText("Terupload");
                            binding.btnUploadFotoKtp.setBackgroundColor(Color.parseColor("#1dd1a1"));
                            binding.btnUploadFotoKtp.setTextColor(Color.parseColor("#ffffff"));
                            binding.btnUploadFotoKtp.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#1dd1a1")));
                        }
                    }
                }
            }
    );
}