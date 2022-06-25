package id.coolva.dasdisduk.ui.main.ui.history;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SectionsPagerAdapter extends FragmentStateAdapter {
    public SectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public SectionsPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public SectionsPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new HistoryProcessServiceFragment();
                break;
            case 1:
                fragment = new HistoryDoneServiceFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}