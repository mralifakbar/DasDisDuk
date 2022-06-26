package id.coolva.dasdisduk.ui.main.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import id.coolva.dasdisduk.data.model.Users;
import id.coolva.dasdisduk.databinding.FragmentHomeBinding;
import id.coolva.dasdisduk.ui.form.damagedlosektp.RegDamagedLoseKTP;
import id.coolva.dasdisduk.ui.form.damagelosekk.RegDamagedLoseKK;
import id.coolva.dasdisduk.ui.form.newkk.RegNewKK;
import id.coolva.dasdisduk.ui.form.newktp.RegNewKTP;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
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
                                binding.tvUserName.setText(users[0].nama.toString());


                            }
                        });
        binding.cardKtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RegNewKTP.class));
            }
        });

        binding.cardKtpRusak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RegDamagedLoseKTP.class));
            }
        });

        binding.cardKkBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RegNewKK.class));
            }
        });

        binding.cardKkRusak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RegDamagedLoseKK.class));
            }
        });

        binding.ivUserProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}