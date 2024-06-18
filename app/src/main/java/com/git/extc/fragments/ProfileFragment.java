package com.git.extc.fragments;



import static com.git.extc.loaderConfiguarations.LoaderConfig.LOADER_VERSION;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;


import com.git.extc.BuildConfig;
import com.git.extc.R;
import com.git.extc.activities.Login;
import com.git.extc.activities.ProfileActivities.EditProfile;
import com.git.extc.activities.ProfileActivities.Help;
import com.git.extc.activities.ProfileActivities.PrivacyPolicy;
import com.git.extc.preferences.Preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class ProfileFragment extends Fragment {

    public LinearLayout logoutlayout, privacylayout, helplayout, shareuslayout;
    public Button editbtn;
    public Preferences prefs;

    public TextView fullname, deptname;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if (getActivity() != null) {
            prefs = Preferences.with(getActivity());
            if (!prefs.contains(LOADER_VERSION)) {
                prefs.write(LOADER_VERSION, BuildConfig.VERSION_NAME);
            }
        }

        shareuslayout = view.findViewById(R.id.shareuslayout);
        helplayout = view.findViewById(R.id.helplayout);
        privacylayout = view.findViewById(R.id.privacylayout);
        logoutlayout = view.findViewById(R.id.logoutlayout);
        editbtn = view.findViewById(R.id.editbtn);
        fullname = view.findViewById(R.id.fullname);
        deptname = view.findViewById(R.id.deptname);

        String fullname1 = prefs.getString("fullname", "");
        String deptname1 = prefs.getString("department", "");

        fullname.setText(fullname1);
        deptname.setText(deptname1);

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                intent.putExtra("sourceFragment", "Fragment4");
                startActivity(intent);
            }
        });

        shareuslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApplication();
            }
        });

        helplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Help.class);
                intent.putExtra("sourceFragment", "Fragment4");
                startActivity(intent);
            }
        });

        privacylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PrivacyPolicy.class);
                intent.putExtra("sourceFragment", "Fragment4");
                startActivity(intent);
            }
        });

        logoutlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Logout")
                        .setMessage("Do you really want to logout?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                prefs.write("oAuthVerificationOpenedIsConfirmed?", "");
                                prefs.write("fullname", "");
                                prefs.write("department", "");
                                prefs.write("VerificationIsConfirmed?", "");
                                Toast.makeText(getActivity(), "Logout Success", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(getActivity(), Login.class);
                                startActivity(intent1);
                                getActivity().finish();
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                            }
                        })
                        .show();

            }
        });


        return view;
    }

    private void shareApplication() {
        ApplicationInfo app = getActivity().getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;

        Intent intent = new Intent(Intent.ACTION_SEND);

        // MIME of .apk is "application/vnd.android.package-archive".
        // but Bluetooth does not accept this. Let's use "*/*" instead.
        intent.setType("*/*");

        // Append file and send Intent
        File originalApk = new File(filePath);

        try {
            //Make new directory in new location=
            File tempFile = new File(getActivity().getExternalCacheDir() + "/ExtractedApk");
            //If directory doesn't exists create new
            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;
            //Get application's name and convert to lowercase
            Resources resources = getResources();
            String appLabel = resources.getString(R.string.app_name_new);
            String formattedLabel = appLabel.replace(" ", "").toLowerCase();

            tempFile = new File(tempFile.getPath() + "/" + formattedLabel + ".apk");
            //If file doesn't exists create new
            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }
            //Copy file to new location
            InputStream in = new FileInputStream(originalApk);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
            //Open share dialog
//          intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
            Uri photoURI = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", tempFile);
//          intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
            intent.putExtra(Intent.EXTRA_STREAM, photoURI);
            startActivity(Intent.createChooser(intent, "Share app via"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}