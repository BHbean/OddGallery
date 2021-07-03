package com.nankai.oddmeseum;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import quatja.com.vorolay.VoronoiView;

public class GetImageTask extends AsyncTask<LANGUAGES, Void, ArrayList<Painting>> {
    private MainActivity mainActivity;

    GetImageTask(MainActivity a) {
         mainActivity = a;
    }

    @Override
    protected ArrayList<Painting> doInBackground(LANGUAGES... languages) {
        String data = NetworkUtils.getPaintingList(languages[0]);
        switch (languages[0]) {
            case ENGLISH: {
                Painting.authorTag = mainActivity.getResources().getString(R.string.english_author_tag);
                Painting.locationTag = mainActivity.getResources().getString(R.string.english_location_tag);
                Painting.descriptionTag = mainActivity.getResources().getString(R.string.english_description_tag);
                Painting.comma = mainActivity.getResources().getString(R.string.english_comma);
                Painting.tf = MainActivity.poiretOne;
                break;
            }
            case CHINESE: {
                Painting.authorTag = mainActivity.getResources().getString(R.string.author_tag);
                Painting.locationTag = mainActivity.getResources().getString(R.string.location_tag);
                Painting.descriptionTag = mainActivity.getResources().getString(R.string.description_tag);
                Painting.comma = mainActivity.getResources().getString(R.string.chinese_comma);
                Painting.tf = MainActivity.huaWenKaiTi;
                break;
            }
        }
        ArrayList<Painting> paintings = NetworkUtils.parseJson(data);
        if (paintings != null) {
            for (Painting painting : paintings) {
                // get the image resource only when there is no bitmap
                if (painting.getImageBitmap() == null) {
                    painting.setImageBitmap(NetworkUtils.getImage(painting.getImageResource()));
                }
            }
        }

        return paintings;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onPostExecute(ArrayList<Painting> paintings) {
        super.onPostExecute(paintings);

        if (paintings != null) {
            mainActivity.wave.setElevation(-1000);
            mainActivity.mPainting = paintings;
            mainActivity.fillImages();
            mainActivity.voronoiView.setOnRegionClickListener(new VoronoiView.OnRegionClickListener() {

                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(mainActivity, DetailActivity.class);

                    intent.putExtra("painting_position", position);

                    mainActivity.startActivity(intent);
                }
            });
            mainActivity.avi.hide();
        } else {
            System.out.println("null painting object!");
        }
    }
}
