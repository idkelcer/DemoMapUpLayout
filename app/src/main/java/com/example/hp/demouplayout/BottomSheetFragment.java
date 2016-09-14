package com.example.hp.demouplayout;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.hp.demouplayout.entities.Benefit;

/**
 * Created by HP on 05/09/2016.
 */
public class BottomSheetFragment extends Fragment {

    public static final String TAG = "BottomSheetFragment";

    private Benefit benefit;


    public static final BottomSheetFragment newInstance(Benefit benefit)
    {
        BottomSheetFragment f = new BottomSheetFragment();
        f.benefit = benefit;

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*if (savedInstanceState != null) {
            //probably orientation change
            Log.i(TAG , "on savedinstance state");
            benefit = (Benefit) savedInstanceState.getSerializable("BenefitData");
        }*/


        final View view = inflater.inflate(R.layout.fragment_slide_page, null, false);

        TextView benefitTextView = (TextView) view.findViewById(R.id.txtBenefit);
        benefitTextView.setText(benefit.getTitulo());
        TextView chapitaTextView = (TextView) view.findViewById(R.id.txtChapita);
        chapitaTextView.setText(benefit.getChapita());

        ImageLoader imageLoader = ClubRequestManager.getInstance(getContext()).getImageLoader();

        imageLoader.get(benefit.getPathLogo(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bitmap = response.getBitmap();
                if (bitmap != null) {

                    ImageView imageView = (ImageView) view.findViewById(R.id.image);
                    imageView.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

       // outState.putSerializable("BenefitData", benefit);
    }
}
