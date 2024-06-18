package com.git.extc.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.git.extc.R;
import com.git.extc.databinding.ActivitySplashBinding;

public class Splash extends AppCompatActivity
{

    ActivitySplashBinding binding;
    private static int SPLASH_TIME_OUT = 5000;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        new Handler() .postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(context, SplashSlider.class);
                startActivity(intent);
            }
        },SPLASH_TIME_OUT);
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();

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
}