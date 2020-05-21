package org.codewrite.teceme.utils;

import android.util.Log;
import android.view.View;

import androidx.core.widget.NestedScrollView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BackToTopFabBehavior implements
        NestedScrollView.OnScrollChangeListener {
    private FloatingActionButton fab;
    public BackToTopFabBehavior(FloatingActionButton fab){
        this.fab=fab;
    }
    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

        Log.d("BackToTopFabBehavior", "onScrollChange: "+scrollY);
        if (scrollY > 300){
            fab.show();
        }else{
            fab.setVisibility(View.INVISIBLE);
        }
    }
}
