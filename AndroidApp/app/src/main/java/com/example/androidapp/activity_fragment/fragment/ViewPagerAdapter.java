package com.example.androidapp.activity_fragment.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.androidapp.activity_fragment.activity.historyactivity.HisAllFragment;
import com.example.androidapp.activity_fragment.activity.historyactivity.HisCanceledragment;
import com.example.androidapp.activity_fragment.activity.historyactivity.HisCompletedFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HisAllFragment();
            case 1:
                return new HisCompletedFragment();
            case 2:
                return new HisCanceledragment();
            default:
                return new HisAllFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
