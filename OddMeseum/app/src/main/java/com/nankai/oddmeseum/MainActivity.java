package com.nankai.oddmeseum;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.john.waveview.WaveView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import quatja.com.vorolay.VoronoiView;

enum LANGUAGES {
    CHINESE, ENGLISH;
}

public class MainActivity extends AppCompatActivity
            implements View.OnTouchListener{
    public static ArrayList<Painting> mPainting = new ArrayList<>();
    public static LANGUAGES language = LANGUAGES.CHINESE;
    // save views showed in Voronoi
    public ArrayList<View> views = new ArrayList<>();
    public static final int NUM_PAINTING = 16;
    // pointer to the layout elements
    private TextView welcome;
    public VoronoiView voronoiView;
    public AVLoadingIndicatorView avi;
    public WaveView wave;
    public FloatingActionButton floatBtn;
    public ViewMovement movement;
    // customized fonts
    public static Typeface huaWenKaiTi;
    public static Typeface poiretOne;
    public static final String LOG_TAG = com.nankai.oddmeseum.MainActivity.class.getSimpleName();
    public static boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // get fonts
        AssetManager assetManager = this.getAssets();
        huaWenKaiTi = Typeface.createFromAsset(assetManager, "font/hua_wen_kai_ti.ttf");
        poiretOne = Typeface.createFromAsset(assetManager, "font/PoiretOne-Regular.ttf");

        // show background and loading gif
        wave = (WaveView) findViewById(R.id.wave_view);
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.show();

        welcome = (TextView) findViewById(R.id.welcome);
        welcome.setTypeface(huaWenKaiTi);

        floatBtn = findViewById(R.id.floating_button);
        floatBtn.setOnTouchListener(this);
        movement = new ViewMovement(floatBtn, R.id.floating_button);

        voronoiView = (VoronoiView) findViewById(R.id.voronoi);
        LayoutInflater layoutInflater = getLayoutInflater();

        for (int i = 0; i < MainActivity.NUM_PAINTING; i++) {
            View view = layoutInflater.inflate(R.layout.item_voronoi, null, false);
            views.add(view);
            voronoiView.addView(view);
        }

        refresh();
        
        Log.d(LOG_TAG, "initialization...");
    }

    public void fillImages() {
        for (int i = 0; i < MainActivity.NUM_PAINTING; i++) {
            View view = views.get(i);
            ImageView imageView = (ImageView) view.findViewById(R.id.painting);
            imageView.setImageBitmap(mPainting.get(i).getImageBitmap());
        }

    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = movement.handleTouch(v, event);
        // click event
        if (action == 2) {
//            wave.setElevation(99);
            avi.show();

            switch (MainActivity.language) {
                case CHINESE: {
                    MainActivity.language = LANGUAGES.ENGLISH;
                    welcome.setText(getResources().getString(R.string.english_welcome_title));
                    welcome.setTypeface(poiretOne);
                    break;
                }
                case ENGLISH: {
                    MainActivity.language = LANGUAGES.CHINESE;
                    welcome.setText(getResources().getString(R.string.welcome_title));
                    welcome.setTypeface(huaWenKaiTi);
                    break;
                }
            }

            refresh();
        }
        return true;
    }

    public void refresh() {
        GetImageTask task = new GetImageTask(this);
        task.execute(language);
    }
}