package id.coolva.dasdisduk.ui.main.ui.profile;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import id.coolva.dasdisduk.R;
import id.coolva.dasdisduk.data.model.Users;
import id.coolva.dasdisduk.data.preference.UserModel;
import id.coolva.dasdisduk.data.preference.UserPreference;
import id.coolva.dasdisduk.databinding.FragmentProfileBinding;
import id.coolva.dasdisduk.ui.auth.LoginActivity;
import id.coolva.dasdisduk.ui.auth.RegisterActivity;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    private Uri selectedImageUriSelfie;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Users[] users = {new Users()};

        db.collection("users").document(firebaseUser.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        users[0] = documentSnapshot.toObject(Users.class);

                        if (users[0].pp != "") {
                            Glide.with(getActivity())
                                    .load(users[0].pp)
                                    .into(binding.profileImage);
                        }
                    }
                });

        binding.ivChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission( getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            getActivity(),
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

        binding.btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                builder.setTitle("Keluar aplikasi");
                builder.setMessage("Apakah yakin ingin keluar aplikasi?")
                        .setCancelable(false)
                        .setPositiveButton("Yakin", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth.getInstance().signOut();
                                UserPreference userPreference = new UserPreference(getActivity());
                                UserModel userModel = userPreference.getUser();
                                userModel.setPassword(null);
                                userModel.setEmail(null);
                                userPreference.setUser(userModel);
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
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
        });

        binding.cardGantiWa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireActivity(), "Fitur Cooming Soon!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.cardPerbaruiIdentitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireActivity(), "Fitur Cooming Soon!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.cardPerbaruiAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireActivity(), "Fitur Cooming Soon!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.cardKetentuanLayanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireActivity(), "Fitur Cooming Soon!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.cardPusatBantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireActivity(), "Fitur Cooming Soon!", Toast.LENGTH_SHORT).show();
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
                        String imageExtension = MimeTypeMap.getSingleton().getExtensionFromMimeType(
                                getContext().getContentResolver().getType(selectedImageUriSelfie)
                        );
                        Random rand = new Random();
                        int randInt = rand.nextInt(10000);
                        String child = "pp-" + firebaseUser.getUid().toString() + "-" + randInt + "." + imageExtension;
                        StorageReference sRef = FirebaseStorage.getInstance().getReference().child(
                                child
                        );

                        sRef.putFile(selectedImageUriSelfie);
                        Map<String, Object> userPhoto = new HashMap<>();
                        userPhoto.put("pp", "https://firebasestorage.googleapis.com/v0/b/dasdisduk.appspot.com/o/" + child + "?alt=media");

                        db.collection("users")
                                .document(firebaseUser.getUid())
                                .set(userPhoto, SetOptions.merge());
                                Glide.with(requireContext())
                                        .load(userPhoto.get("pp"))
                                        .into(binding.profileImage);

                    }
                }
            }
    );
}