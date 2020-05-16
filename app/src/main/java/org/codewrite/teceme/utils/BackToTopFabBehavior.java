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
    public void onNestedScroll(@NonNull final CoordinatorLayout coordinatorLayout,
                               @NonNull final FloatingActionButton child,
                               @NonNull final View target,
                               final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        Log.d("BackToTopFabBehavior", "onNestedScroll dyUnconsumed: " + dyUnconsumed);
        Log.d("BackToTopFabBehavior", "onNestedScroll dyConsumed: " + dyConsumed);
        if (dyConsumed > 3 || dyConsumed < 0) {
            child.show();
        } else if (dyConsumed == 0 && child.getVisibility() == View.VISIBLE && dyUnconsumed < -3) {
            child.setVisibility(View.INVISIBLE);
        }
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


}
