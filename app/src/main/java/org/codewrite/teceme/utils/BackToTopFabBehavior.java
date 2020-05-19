package org.codewrite.teceme.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BackToTopFabBehavior extends FloatingActionButton.Behavior {
    public BackToTopFabBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull FloatingActionButton child,
                                       @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type) {
        // Ensure we react to vertical scrolling
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull FloatingActionButton child,
                               @NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, int type,
                               @NonNull int[] consumed) {

        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed,
                dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);

        Log.d("BackToTopFabBehavior", "onNestedScroll dyConsumed : " + dyConsumed);
        Log.d("BackToTopFabBehavior", "onNestedScroll dyUnconsumed: " + dyUnconsumed);
//        if ((dyConsumed > 10 || dyConsumed < -10) && child.getVisibility() == View.INVISIBLE) {
//            child.show();
//        } else if (dyUnconsumed > 10 && child.getVisibility() == View.VISIBLE) {
//            child.setVisibility(View.INVISIBLE);
//        }

    }
}
