package org.codewrite.teceme.ui.product;


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
public class ProductSliderFragment extends Fragment {

private String imgUrl;
    private FragmentActivity activityContext;

    public ProductSliderFragment() {
        // Required empty public constructor
    }

    public ProductSliderFragment(String imgUrl){
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        ImageView productImage = rootView.findViewById(R.id.image);
        // set image
        Resources resources = activityContext.getResources();
        try {
            Picasso.get()
                    .load(resources.getString(R.string.api_base_url)
                           +"Products/product-image/"
                            + imgUrl)
                    .placeholder(R.drawable.loading_image)
                    .error(R.drawable.no_product_image)
                    .into(productImage);
        } catch (Exception e) {
            productImage.setImageResource(R.drawable.no_product_image);
        }
        return rootView;
    }


}
