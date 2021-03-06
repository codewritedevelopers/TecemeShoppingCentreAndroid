package org.codewrite.teceme.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.codewrite.teceme.ui.product.ProductSliderFragment;

import java.util.ArrayList;
import java.util.List;

public class ProductSliderAdapter extends FragmentStatePagerAdapter {


    private List<String> list;

    public ProductSliderAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return new ProductSliderFragment(list.get(position));
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    public  void subList(List<String> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
