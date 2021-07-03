package com.nankai.oddmeseum;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class PaintingFragmentAdapter extends FragmentStatePagerAdapter {
    private ArrayList<PaintingFragment> mFragments = new ArrayList<>();
    private ViewPager mPager;
    private int mNumOfFragment;

    public PaintingFragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

        mFragments = new ArrayList<>();
        mNumOfFragment = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public void add(PaintingFragment parallaxFragment) {
        parallaxFragment.setAdapter(this);
        mFragments.add(parallaxFragment);
        notifyDataSetChanged();
        mPager.setCurrentItem(getCount() - 1, true);
    }

    public void remove(int i) {
        mFragments.remove(i);
        notifyDataSetChanged();
    }

    public void remove(PaintingFragment parallaxFragment) {
        mFragments.remove(parallaxFragment);

        int pos = mPager.getCurrentItem();
        notifyDataSetChanged();

        mPager.setAdapter(this);
        if (pos >= this.getCount()) {
            pos = this.getCount() - 1;
        }
        mPager.setCurrentItem(pos, true);

    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void setPager(ViewPager pager) {
        mPager = pager;
    }

}
