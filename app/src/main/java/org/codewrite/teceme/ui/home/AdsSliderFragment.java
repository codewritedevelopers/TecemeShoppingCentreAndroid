package org.codewrite.teceme.ui.home;


import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.codewrite.teceme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdsSliderFragment extends Fragment {

    private String imgUrl;
    private FragmentActivity activityContext;
    private  ImageView adsImage;

    public AdsSliderFragment() {
        // Required empty public constructor
    }

    public AdsSliderFragment(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        adsImage = rootView.findViewById(R.id.image);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupAds();
    }

    private void setupAds() {
        Resources resources = activityContext.getResources();
        try {
            Picasso.get()
                    .load(resources.getString(R.string.api_base_url)
                            +"customers/ads-image/"
                            + imgUrl)
                    .resize(adsImage.getWidth(),150)
                    .placeholder(R.drawable.loading_image)
                    .into(adsImage);
        } catch (Exception e) {
            adsImage.setImageResource(R.drawable.background_with_drop_shadow);
        }
    }
}
