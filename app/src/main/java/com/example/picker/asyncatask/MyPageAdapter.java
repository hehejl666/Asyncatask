package com.example.picker.asyncatask;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 17930 on 2016/1/25.
 */
public class MyPageAdapter extends PagerAdapter {
    private List<View> mview;
    private Context mcontext;

    public MyPageAdapter(List<View> mview, Context mcontext) {
        this.mview = mview;
        this.mcontext = mcontext;
    }



    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=mview.get(position%mview.size());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mview.get(position % mview.size()));
    }


}
