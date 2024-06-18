package com.git.extc.fragments;


import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.git.extc.R;
import com.git.extc.activities.SliderData;
import com.git.extc.adapters.SliderAdapter;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnnouncementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class AnnouncementFragment extends Fragment {

    String url1 = "https://www.git-india.edu.in/git/images/slider/2.webp";
    String url2 = "https://www.git-india.edu.in/git/images/slider/3.webp";
    String url3 = "https://www.git-india.edu.in/git/images/slider/0.webp";
    String url4 = "https://www.git-india.edu.in/git/images/slider/4.webp";

    public LinearLayout visitweb;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    //public ImageSlider imageSlider;
    public RelativeLayout announcement_layout, Developer;


    public TextView announcements, announcement_title;

    public AnnouncementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnnouncementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnnouncementFragment newInstance(String param1, String param2) {
        AnnouncementFragment fragment = new AnnouncementFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_announcement, container, false);



        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
        SliderView sliderView = view.findViewById(R.id.slider);

        sliderDataArrayList.add(new SliderData(url1));
        sliderDataArrayList.add(new SliderData(url2));
        sliderDataArrayList.add(new SliderData(url3));
        sliderDataArrayList.add(new SliderData(url4));

        SliderAdapter adapter = new SliderAdapter(getActivity(), sliderDataArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        visitweb = view.findViewById(R.id.visitweb);
        visitweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.git-india.edu.in/git/index.html"));
                try {
                    getActivity().startActivity(webIntent);
                } catch (ActivityNotFoundException ex) {
                }
            }
        });


        return view;
    }


}