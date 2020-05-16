package org.codewrite.teceme.utils;

import android.app.Activity;

import androidx.viewpager.widget.ViewPager;

import java.util.TimerTask;

public class SliderTimer extends TimerTask {
    private Activity Slider;
    private ViewPager viewPager;
    private int size;

    public SliderTimer(Activity slider, ViewPager viewPager, int size) {
        Slider = slider;
        this.viewPager = viewPager;
        this.size = size;
    }

    @Override
    public void run() {
        Slider.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (viewPager.getCurrentItem() < size - 1) {

                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                } else {
                    viewPager.setCurrentItem(0,true);
                }
            }
        });
    }
}
