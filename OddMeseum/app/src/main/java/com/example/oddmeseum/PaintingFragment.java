package com.example.oddmeseum;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PaintingFragment extends Fragment {

    private PaintingFragmentAdapter mPaintingsAdapter;
    public static final String LOG_TAG = com.example.oddmeseum.PaintingFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.painting_fragment_parallax, container, false);
        final ImageView image = (ImageView) view.findViewById(R.id.image);

        // Get Painting Object
        int pos = getArguments().getInt("painting_index");
        Painting painting = MainActivity.mPainting.get(pos);
        Author author = painting.getAuthor();
        Museum museum = painting.getMuseum();

        String authorTag = Painting.authorTag;
        String comma = Painting.comma;
        String locationTag = Painting.locationTag;
        String descriptionTag = Painting.descriptionTag;
        Typeface tf = Painting.tf;

        image.setImageBitmap(
                painting.getImageBitmap()
        );
//        image.post(new Runnable() {
//            @Override
//            public void run() {
//                Matrix matrix = new Matrix();
//                matrix.reset();
//
//                float wv = image.getWidth();
//                float hv = image.getHeight();
//                Log.d(LOG_TAG, "img_width: " + wv + "\timg_height: " + hv);
//
//                float wi = image.getDrawable().getIntrinsicWidth();
//                float hi = image.getDrawable().getIntrinsicHeight();
//                Log.d(LOG_TAG, "drawable_width: " + wi + "\tdrawable_height: " + hi);
//
//                float width = wv;
//                float height = hv;
//
//                if (wi / wv > hi / hv) {
//                    matrix.setScale(hv / hi, hv / hi);
//                    width = wi * hv / hi;
//                } else {
//                    matrix.setScale(wv / wi, wv / wi);
//                    height= hi * wv / wi;
//                }
//
//                matrix.preTranslate((wv - width) / 2, (hv - height) / 2);
//                image.setScaleType(ImageView.ScaleType.MATRIX);
//                image.setImageMatrix(matrix);
//            }
//        });

        TextView name = (TextView)view.findViewById(R.id.name);
        name.setText(painting.getName());
        name.setTypeface(tf);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(authorTag)
                .append(author.getCountry())
                .append(comma)
                .append(author.getName());
        TextView authorText = (TextView)view.findViewById(R.id.author_info);
        authorText.setText(stringBuilder.toString());
        authorText.setTypeface(tf);

        stringBuilder = new StringBuilder();
        stringBuilder.append(locationTag)
                .append(museum.getNation())
                .append(comma)
                .append(museum.getName());
        TextView locationText = (TextView)view.findViewById(R.id.location_info);
        locationText.setText(stringBuilder.toString());
        locationText.setTypeface(tf);

        stringBuilder = new StringBuilder();
        stringBuilder.append(descriptionTag)
                .append(painting.getDescription());
        TextView descriptionText = (TextView)view.findViewById(R.id.description);
        descriptionText.setText(stringBuilder.toString());
        descriptionText.setTypeface(tf);

        return view;
    }

    public void setAdapter(PaintingFragmentAdapter paintingFragmentAdapter) {
        mPaintingsAdapter = paintingFragmentAdapter;
    }
}
