package com.insighters.ash.note_maker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.insighters.ash.note_maker.R;


/**
 * Created by ash.note_maker on 4/7/16.
 */
public class splash_screen extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View V=inflater.inflate(R.layout.fragment_splash_screen,container,false);
        ImageView myImageView= (ImageView)V.findViewById(R.id.splash_icon);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        myImageView.startAnimation(myFadeInAnimation);
        return V;
    }

    /**
     * Created by Akshay on 4/7/16.
     */

}
