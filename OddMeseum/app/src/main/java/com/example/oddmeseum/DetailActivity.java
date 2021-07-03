package com.example.oddmeseum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.xgc1986.parallaxPagerTransformer.ParallaxPagerTransformer;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    TextView mTextView;
    ViewPager mPager;
    PaintingFragmentAdapter mAdapter;
    public static final String TAG = "com.example.oddmeseum";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int pos = getIntent().getIntExtra("painting_position", 0);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setBackgroundColor(0xFF000000);

        ParallaxPagerTransformer pt = new ParallaxPagerTransformer((R.id.image));
        pt.setBorder(20);
        //pt.setSpeed(0.2f);
        mPager.setPageTransformer(false, pt);

        mAdapter = new PaintingFragmentAdapter(getSupportFragmentManager(), MainActivity.mPainting.size());
        mAdapter.setPager(mPager); //only for this transformer
        for (int i = 0; i < MainActivity.NUM_PAINTING; i++) {
            Bundle bundle = new Bundle();
//            System.out.println(painting.getName() + ": " + painting.getImageResource());
            bundle.putInt("painting_index", i);
            PaintingFragment fragment = new PaintingFragment();
            fragment.setArguments(bundle);
            mAdapter.add(fragment);
        }
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(pos, true);
    }
}