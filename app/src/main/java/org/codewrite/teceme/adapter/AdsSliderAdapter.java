package org.codewrite.teceme.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.codewrite.teceme.ui.home.AdsSliderFragment;
import org.codewrite.teceme.ui.product.ProductSliderFragment;

import java.util.ArrayList;
import java.util.List;

public class AdsSliderAdapter extends FragmentStatePagerAdapter {


    private List<String> list =new ArrayList<>();

    public AdsSliderAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new AdsSliderFragment(list.get(position));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public  void subList(List<String> uri){
        if (uri!=null) {
            this.list = uri;
            notifyDataSetChanged();
        }
    }
}
