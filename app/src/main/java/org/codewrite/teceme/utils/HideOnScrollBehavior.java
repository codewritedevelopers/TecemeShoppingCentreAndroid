package org.codewrite.teceme.utils;

import android.view.View;

import androidx.core.widget.NestedScrollView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HideOnScrollBehavior implements
        NestedScrollView.OnScrollChangeListener {
    private FloatingActionButton fab;
    public HideOnScrollBehavior(FloatingActionButton fab){
        this.fab=fab;
    }
    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY > 300){
            fab.setVisibility(View.INVISIBLE);
        }else{
            fab.show();
        }
    }
}
