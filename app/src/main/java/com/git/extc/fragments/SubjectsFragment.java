package com.git.extc.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.git.extc.R;
import com.git.extc.activities.Home;
import com.git.extc.activities.ProfileActivities.PrivacyPolicy;
import com.git.extc.activities.Semesters.Semester1;
import com.git.extc.activities.Semesters.Semester2;
import com.git.extc.activities.Semesters.Semester3;
import com.git.extc.activities.Semesters.Semester4;
import com.git.extc.activities.Semesters.Semester5;
import com.git.extc.activities.Semesters.Semester6;
import com.git.extc.activities.Semesters.Semester7;
import com.git.extc.activities.Semesters.Semester8;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubjectsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class SubjectsFragment extends Fragment {

    public LinearLayout semesterone, semestertwo, semesterthree, semesterfour, semesterfive, semestersix, semesterseven, semestereight;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LinearLayout view_lay, view_lay_gone, sem_one_holder_layout, sem_two_holder_layout, sem_three_holder_layout, sem_four_holder_layout, sem_five_holder_layout, sem_six_holder_layout, sem_seven_holder_layout, sem_eight_holder_layout;

    public SubjectsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubjectsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubjectsFragment newInstance(String param1, String param2) {
        SubjectsFragment fragment = new SubjectsFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subjects, container, false);

        semesterone = view.findViewById(R.id.semesterone);
        semestertwo = view.findViewById(R.id.semestertwo);
        semesterthree = view.findViewById(R.id.semesterthree);
        semesterfour = view.findViewById(R.id.semesterfour);
        semesterfive = view.findViewById(R.id.semesterfive);
        semestersix = view.findViewById(R.id.semestersix);
        semesterseven = view.findViewById(R.id.semesterseven);
        semestereight = view.findViewById(R.id.semestereight);

        semesterone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Semester1.class);
                intent.putExtra("sourceFragment", "Fragment2");
                startActivity(intent);
            }
        });

        semestertwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Semester2.class);
                intent.putExtra("sourceFragment", "Fragment2");
                startActivity(intent);
            }
        });
        semesterthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Semester3.class);
                intent.putExtra("sourceFragment", "Fragment2");
                startActivity(intent);
            }
        });
        semesterfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Semester4.class);
                intent.putExtra("sourceFragment", "Fragment2");
                startActivity(intent);
            }
        });
        semesterfive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Semester5.class);
                intent.putExtra("sourceFragment", "Fragment2");
                startActivity(intent);
            }
        });
        semestersix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Semester6.class);
                intent.putExtra("sourceFragment", "Fragment2");
                startActivity(intent);
            }
        });
        semesterseven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Semester7.class);
                intent.putExtra("sourceFragment", "Fragment2");
                startActivity(intent);
            }
        });
        semestereight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Semester8.class);
                intent.putExtra("sourceFragment", "Fragment2");
                startActivity(intent);
            }
        });

        return view;
    }


}