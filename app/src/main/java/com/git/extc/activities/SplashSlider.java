package com.git.extc.activities;

import static com.git.extc.loaderConfiguarations.LoaderConfig.ISMAINTENANCE;
import static com.git.extc.loaderConfiguarations.LoaderConfig.LOADER;
import static com.git.extc.loaderConfiguarations.LoaderConfig.LOADER_VERSION;
import static com.git.extc.loaderConfiguarations.LoaderConfig.UPDATE_LINK;
import static com.git.extc.loaderConfiguarations.LoaderConfig.VERSION;
import static com.git.extc.loaderConfiguarations.LoaderConfig.isMaintenance;
import static com.git.extc.loaderConfiguarations.LoaderConfig.server_status;
import static com.git.extc.loaderConfiguarations.LoaderConfig.status_server;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.git.extc.BuildConfig;
import com.git.extc.R;
import com.git.extc.adapters.ViewPagerAdapter;
import com.git.extc.databinding.ActivitySplashSliderBinding;
import com.git.extc.preferences.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashSlider extends AppCompatActivity {

    ActivitySplashSliderBinding binding;

    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;
    Context context;
    public Preferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashSliderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        theredes();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        prefs = Preferences.with(this);

        String verifications = prefs.read("oAuthVerificationOpenedIsConfirmed?");
        if (verifications.equals("confirmedVerifyOauth"))
        {
            Intent intent2 = new Intent(context, Home.class);
            startActivity(intent2);
            finish();
        }

        viewPagerAdapter = new ViewPagerAdapter(context);
        binding.slideviewpager.setAdapter(viewPagerAdapter);
        setUpindicator(0);
        binding.slideviewpager.addOnPageChangeListener(viewListener);
        binding.signintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Login.class);
                startActivity(intent);
            }
        });

        binding.signuptxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Signup.class);
                startActivity(intent);
            }
        });
    }

    public void setUpindicator(int position){
        dots = new TextView[4];
        binding.indicatorlayout.removeAllViews();

        for(int i = 0; i < dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(45);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                dots[i].setTextColor(getResources().getColor(R.color.custom_gray, getApplicationContext().getTheme()));
            }
            binding.indicatorlayout.addView(dots[i]);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dots[position].setTextColor(getResources().getColor(R.color.custom_blue, getApplicationContext().getTheme()));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setUpindicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onBackPressed() {
      //  super.onBackPressed();

        new AlertDialog.Builder(context)
                        .setTitle("Confirmation")
                        .setMessage("Do you really want to Exit?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                finishAffinity();
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

    @Override
    protected void onResume() {
        super.onResume();

        if(!Preferences.isPermissionGranted(this))
        {

            new AlertDialog.Builder(this)
                    .setTitle("Git Extc")
                    .setMessage("Allow Git Extc to Access your Device Storage")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)

                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            takePermission();
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            denyPermission();
                        }
                    })
                    .show();
        }
        else
        {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show();
            //TastyToast.makeText(getApplicationContext(), "Permission Granted!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
        }
    }

    private void denyPermission()
    {
        Toast.makeText(context, "Something Went Wrong! Please Restart Loader", Toast.LENGTH_SHORT).show();
        // TastyToast.makeText(getApplicationContext(), "Something Went Wrong! Please Restart Loader!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
    }

    private void takePermission()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            try
            {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");

                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);

            } catch (Exception e)
            {
                e.printStackTrace();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 101);

            }
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 101);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0)
        {
            if(requestCode == 101)
            {
                boolean readExt = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(!readExt)
                {
                    takePermission();
                }
            }
        }
    }


    private void theredes() {
        new Thread(() -> {
            try {

                HttpURLConnection connection = (HttpURLConnection) new URL(UPDATE_LINK).openConnection();
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setRequestProperty("Connection", "close");
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    throw new IOException("Request Code Not 200");
                }

                JSONObject update;

                InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] bArr = new byte[8192];
                while (true) {
                    long read = inputStream.read(bArr, 0, 8192);
                    if (read != -1) {
                        byteArrayOutputStream.write(bArr, 0, (int) read);
                    } else {
                        inputStream.close();
                        connection.disconnect();
                        update = new JSONObject(byteArrayOutputStream.toString("UTF-8"));
                        break;
                    }
                }


                //LoaderUpdate
                final String newSver = update.getJSONObject(LOADER).getString(VERSION);
                final String currSver = prefs.read(LOADER_VERSION, BuildConfig.VERSION_NAME);

                if (!currSver.equals(newSver)) {
                    runOnUiThread(()->{
                        Intent openUpdateLogActivity = new Intent(this, Update.class);
                        startActivity(openUpdateLogActivity);
                    });
                }

                //ServerStatusChecker
                final String status_in_server = update.getJSONObject(ISMAINTENANCE).getString(server_status);
                final String status_in_prefs = prefs.read(isMaintenance, status_server);

                final String descriptionServer = update.getJSONObject(ISMAINTENANCE).getString(VERSION);

                if (!status_in_prefs.equals(status_in_server)) {
                    runOnUiThread(()->{
                        Toast.makeText(context, descriptionServer, Toast.LENGTH_SHORT).show();
                    });
                } else {
                    //Loader Update
                    final String newSVer = update.getJSONObject(LOADER).getString(VERSION);
                    final String currSVer = prefs.read(LOADER_VERSION, BuildConfig.VERSION_NAME);
                    runOnUiThread(() -> {
                        prefs.write(LOADER_VERSION, currSVer);
                    });
                }
            } catch (IOException | JSONException e) {
                //HelloWorld
                runOnUiThread(()->{
                    e.printStackTrace();
                    Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}

