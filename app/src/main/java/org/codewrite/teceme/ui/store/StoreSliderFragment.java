package org.codewrite.teceme.ui.store;


import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.squareup.picasso.Picasso;

import org.codewrite.teceme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreSliderFragment extends Fragment {

private String imgUrl;
    private FragmentActivity activityContext;
    public StoreSliderFragment() {
        // Required empty public constructor
    }

    public StoreSliderFragment(String imgUrl){
        this.imgUrl=imgUrl;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        final ImageView imageView = rootView.findViewById(R.id.image);
        // set image
        Resources resources = activityContext.getResources();
        try {
            Picasso.get()
                    .load(resources.getString(R.string.api_base_url)
                           +"stores/store-image2/"
                            + imgUrl)
                    .resize(imageView.getWidth(),250)
                    .placeholder(R.drawable.loading_image)
                    .into(imageView);
        } catch (Exception e) {
            imageView.setImageResource(R.drawable.no_store_image);
        }
        return rootView;
    }
}
