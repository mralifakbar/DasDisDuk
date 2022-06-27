package id.coolva.dasdisduk.ui.form.damagedlosektp;

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
import android.widget.AdapterView;
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
import id.coolva.dasdisduk.databinding.ActivityRegDamagedLoseKtpBinding;
import id.coolva.dasdisduk.ui.form.newktp.RegNewKTP;
import id.coolva.dasdisduk.ui.main.MainActivity;

public class RegDamagedLoseKTP extends AppCompatActivity {

    private ActivityRegDamagedLoseKtpBinding binding;
    private Uri selectedImageKK;
    private Uri selectedImageUriSelfie;
    private Uri selectedImageUriKeteranganPolisi;
    private int fileSelectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegDamagedLoseKtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnUploadSuratHilang.setEnabled(false);
        getSupportActionBar().setTitle("KTP Rusak/Hilang");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFFFFFF));
        getSupportActionBar().setElevation(0F);

        String[] jk = getResources().getStringArray(R.array.jeniskelamin);
        String[] goldar = getResources().getStringArray(R.array.goldar);
        String[] agama = getResources().getStringArray(R.array.agama);
        String[] statusNikah = getResources().getStringArray(R.array.statusnikah);
        String[] wn = getResources().getStringArray(R.array.wn);
        String[] jenisPengajuan = getResources().getStringArray(R.array.jenispengajuan);
        ArrayAdapter<String> arrayAdapterJk= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, jk);
        ArrayAdapter<String> arrayAdapterGoldar = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, goldar);
        ArrayAdapter<String> arrayAdapterAgama = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, agama);
        ArrayAdapter<String> arrayAdapterStatusNikah = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, statusNikah);
        ArrayAdapter<String> arrayAdapterWn = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, wn);
        ArrayAdapter<String> arrayAdapterJenisPengajuan = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, jenisPengajuan);
        binding.actJenisKelamin.setAdapter(arrayAdapterJk);
        binding.actAgama.setAdapter(arrayAdapterAgama);
        binding.edtGolDar.setAdapter(arrayAdapterGoldar);
        binding.statNikah.setAdapter(arrayAdapterStatusNikah);
        binding.statWargaNegara.setAdapter(arrayAdapterWn);
        binding.actJenisPengajuan.setAdapter(arrayAdapterJenisPengajuan);

        binding.btnUploadKk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileSelectedIndex = 1;
                if (ActivityCompat.checkSelfPermission( RegDamagedLoseKTP.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            RegDamagedLoseKTP.this,
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
                if (ActivityCompat.checkSelfPermission( RegDamagedLoseKTP.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            RegDamagedLoseKTP.this,
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

        binding.btnUploadSuratHilang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileSelectedIndex = 3;
                if (ActivityCompat.checkSelfPermission( RegDamagedLoseKTP.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            RegDamagedLoseKTP.this,
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

        binding.actJenisPengajuan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = arrayAdapterJenisPengajuan.getItem(i);

                if (selected.equals("Hilang")) {
                    binding.btnUploadSuratHilang.setEnabled(true);
                } else {
                    binding.btnUploadSuratHilang.setEnabled(false);
                }
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtTempatLahir.getText().toString().isEmpty() || binding.edtTanggalLahir.getText().toString().isEmpty()
                        || binding.actJenisKelamin.getText().toString().isEmpty() || binding.edtGolDar.getText().toString().isEmpty()
                        || binding.edtAlamatJalan.getText().toString().isEmpty() || binding.edtRtRw.getText().toString().isEmpty()
                        || binding.edtKelDesa.getText().toString().isEmpty() || binding.edtKecamatan.getText().toString().isEmpty()
                        || binding.actAgama.getText().toString().isEmpty() || binding.statNikah.getText().toString().isEmpty()
                        || binding.edtPekerjaan.getText().toString().isEmpty() || binding.statWargaNegara.getText().toString().isEmpty()
                        || binding.actJenisPengajuan.getText().toString().isEmpty()) {
                    Toast.makeText(RegDamagedLoseKTP.this, "Tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                } else if (selectedImageUriSelfie == null || selectedImageKK == null) {
                    Toast.makeText(RegDamagedLoseKTP.this, "Semua gambar harus diupload!", Toast.LENGTH_SHORT).show();
                } else  if (!binding.cbKonfir.isChecked()) {
                    Toast.makeText(RegDamagedLoseKTP.this, "Anda harus setuju!", Toast.LENGTH_SHORT).show();
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

                    String imageExtensionSuratHilang = MimeTypeMap.getSingleton().getExtensionFromMimeType(
                            getContentResolver().getType(selectedImageUriKeteranganPolisi)
                    );

                    String childSelfie = "slktpold-" + firebaseUser.getUid().toString() + "-" + randInt + "." + imageExtensionSelfie;
                    String childKK = "kkktpold-" + firebaseUser.getUid().toString() + "-" + randInt + "." + imageExtensionKK;
                    String childSuratHilang = "shktpold-" + firebaseUser.getUid().toString() + "-" + randInt + "." + imageExtensionSuratHilang;

                    StorageReference sRefSelfie = FirebaseStorage.getInstance().getReference().child(
                            childSelfie
                    );
                    sRefSelfie.putFile(selectedImageUriSelfie);

                    StorageReference sRefKK = FirebaseStorage.getInstance().getReference().child(
                            childKK
                    );
                    sRefKK.putFile(selectedImageKK);

                    StorageReference sRefSuratHilang = FirebaseStorage.getInstance().getReference().child(
                            childSuratHilang
                    );
                    sRefSuratHilang.putFile(selectedImageUriKeteranganPolisi);

                    Map<String, Object> updateNewKtp = new HashMap<>();
                    updateNewKtp.put("ktprusakhilang", 1);
                    db.collection("users").document(firebaseUser.getUid())
                            .set(updateNewKtp, SetOptions.merge());

                    Map<String, Object> newKTPData = new HashMap<>();
                    newKTPData.put("uid", firebaseUser.getUid().toString());
                    newKTPData.put("jenispengajuan", binding.actJenisPengajuan.getText().toString());
                    newKTPData.put("tempatlahir", binding.edtTempatLahir.getText().toString());
                    newKTPData.put("tanggallahir", binding.edtTanggalLahir.getText().toString());
                    newKTPData.put("jeniskelamin", binding.actJenisKelamin.getText().toString());
                    newKTPData.put("golongandarah", binding.edtGolDar.getText().toString());
                    newKTPData.put("alamat", binding.edtAlamatJalan.getText().toString());
                    newKTPData.put("rtrw", binding.edtRtRw.getText().toString());
                    newKTPData.put("kelurahandesa", binding.edtKelDesa.getText().toString());
                    newKTPData.put("kecamatan", binding.edtKecamatan.getText().toString());
                    newKTPData.put("agama", binding.actAgama.getText().toString());
                    newKTPData.put("statusnikah", binding.statNikah.getText().toString());
                    newKTPData.put("pekerjaan", binding.edtPekerjaan.getText().toString());
                    newKTPData.put("wn", binding.statWargaNegara.getText().toString());
                    newKTPData.put("status", "Pengajuan Baru");
                    newKTPData.put("selesai", false);
                    newKTPData.put("diterima", false);
                    newKTPData.put("tanggalpengajuan", System.currentTimeMillis());
                    newKTPData.put("fotokk",
                            "https://firebasestorage.googleapis.com/v0/b/dasdisduk.appspot.com/o/" + childKK + "?alt=media");
                    newKTPData.put("fotoselfie",
                            "https://firebasestorage.googleapis.com/v0/b/dasdisduk.appspot.com/o/" + childSelfie + "?alt=media");
                    newKTPData.put("fotosurathilang",
                            "https://firebasestorage.googleapis.com/v0/b/dasdisduk.appspot.com/o/" + childSuratHilang + "?alt=media");

                    db.collection("ktphilangrusak")
                            .document(firebaseUser.getUid().toString())
                            .set(newKTPData, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(RegDamagedLoseKTP.this, "Berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegDamagedLoseKTP.this, MainActivity.class));
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
                            selectedImageUriKeteranganPolisi = data.getData();
                            binding.btnUploadSuratHilang.setText("Terupload");
                            binding.btnUploadSuratHilang.setBackgroundColor(Color.parseColor("#1dd1a1"));
                            binding.btnUploadSuratHilang.setTextColor(Color.parseColor("#ffffff"));
                            binding.btnUploadSuratHilang.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#1dd1a1")));
                        }
                    }
                }
            }
    );
}