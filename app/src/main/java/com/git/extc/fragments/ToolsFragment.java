package com.git.extc.fragments;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.git.extc.R;
import com.git.extc.activities.MoreActivities.GoogleMeet;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToolsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ToolsFragment extends Fragment {

    public LinearLayout googlemeetlayout, chatgptlayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecyclerView recyclerView;

    public EditText messageEditText;
    public ImageButton sendButton;

    public LinearLayout secondLinear, firstLinear;


    public ToolsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToolsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ToolsFragment newInstance(String param1, String param2) {
        ToolsFragment fragment = new ToolsFragment();
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
        View view = inflater.inflate(R.layout.fragment_tools, container, false);

        googlemeetlayout = view.findViewById(R.id.googlemeetlayout);
        chatgptlayout = view.findViewById(R.id.chatgptlayout);

        googlemeetlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GoogleMeet.class);
                startActivity(intent);
            }
        });

        chatgptlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), chatgptWebview.class);
//                intent.putExtra("sourceFragment", "Fragment3");
//                startActivity(intent);
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.chatgpt.com"));
                try {
                    getActivity().startActivity(webIntent);
                } catch (ActivityNotFoundException ex) {
                }
            }
        });
        return view;
    }
}