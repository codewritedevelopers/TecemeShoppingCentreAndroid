package org.codewrite.teceme.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.codewrite.teceme.ui.home.AdsSliderFragment;

public class AdsSliderAdapter extends FragmentStatePagerAdapter {


    private static final int NUM_PAGES = 5;

    public AdsSliderAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new AdsSliderFragment();
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

}
