package id.coolva.dasdisduk.ui.main.ui.history;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import id.coolva.dasdisduk.R;
import id.coolva.dasdisduk.data.model.DamagedLoseKK;
import id.coolva.dasdisduk.data.model.DamagedLoseKtp;
import id.coolva.dasdisduk.data.model.NewKK;
import id.coolva.dasdisduk.data.model.NewKTP;
import id.coolva.dasdisduk.data.model.Users;
import id.coolva.dasdisduk.databinding.FragmentHistoryProcessServiceBinding;
import id.coolva.dasdisduk.databinding.FragmentHomeBinding;

public class HistoryProcessServiceFragment extends Fragment {

    FragmentHistoryProcessServiceBinding binding;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public HistoryProcessServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHistoryProcessServiceBinding.inflate(inflater, container, false);
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
                        Log.d("users", users[0].nama);
                        Log.d("users", String.valueOf(users[0].ktpbaru));

                        if (users[0].ktpbaru == 1) {
                            final NewKTP[] newKTP = {new NewKTP()};
                            db.collection("ktpbaru").document(firebaseUser.getUid())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            try {
                                                newKTP[0] = documentSnapshot.toObject(NewKTP.class);
                                                Log.d("users", String.valueOf(newKTP[0].selesai));

                                                if (!newKTP[0].selesai) {
                                                    binding.newKtpCardProcess.setVisibility(View.VISIBLE);
                                                    binding.tvService1.setText("KTP Baru");
                                                    binding.tvStatsService1.setText("STATUS: " +newKTP[0].status);
                                                }
                                            } catch (Exception e) {

                                            }
                                                                                    }
                                    });
                        }

                        if (users[0].ktprusakhilang == 1) {
                            final DamagedLoseKtp[] damagedLoseKtps = {new DamagedLoseKtp()};
                            db.collection("ktphilangrusak").document(firebaseUser.getUid())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            try {
                                                damagedLoseKtps[0] = documentSnapshot.toObject(DamagedLoseKtp.class);

                                                if (!damagedLoseKtps[0].selesai) {
                                                    binding.damagedLoseKtpCardProcess.setVisibility(View.VISIBLE);
                                                    binding.tvService2.setText("KTP " + damagedLoseKtps[0].jenispengajuan);
                                                    binding.tvStatsService2.setText("STATUS: " +damagedLoseKtps[0].status);
                                                }
                                            } catch (Exception e) {

                                            }
                                        }
                                    });
                        }

                        if (users[0].kkbaru == 1) {
                            final NewKK[] newKK = {new NewKK()};
                            db.collection("kkbaru").document(firebaseUser.getUid())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            try {
                                                newKK[0] = documentSnapshot.toObject(NewKK.class);

                                                if (!newKK[0].selesai) {
                                                    binding.newKkCardProcess.setVisibility(View.VISIBLE);
                                                    binding.tvService3.setText("KK Baru");
                                                    binding.tvStatsService3.setText("STATUS: " + newKK[0].status);
                                                }
                                            }
                                            catch (Exception e) {

                                            }
                                        }
                                    });
                        }

                        if (users[0].kkrusakhilang == 1) {
                            final DamagedLoseKK[] damagedLoseKK = {new DamagedLoseKK()};
                            db.collection("kkhilangrusak").document(firebaseUser.getUid())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            try {
                                                damagedLoseKK[0] = documentSnapshot.toObject(DamagedLoseKK.class);

                                                if (!damagedLoseKK[0].selesai) {
                                                    binding.damagedLoseKkCardProcess.setVisibility(View.VISIBLE);
                                                    binding.tvService4.setText("KK " + damagedLoseKK[0].jenispengajuan);
                                                    binding.tvStatsService4.setText("STATUS: " + damagedLoseKK[0].status);
                                                }
                                            } catch (Exception e) {

                                            }
                                        }
                                    });
                        }
                    }
                });


//

//


    }
}