package com.git.extc.activities.MoreActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.git.extc.R;
import com.git.extc.databinding.ActivityGoogleMeetBinding;
import com.git.extc.databinding.ActivityHomeBinding;

public class GoogleMeet extends AppCompatActivity {

    ActivityGoogleMeetBinding binding;
    public Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGoogleMeetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        binding.web.loadUrl("https://workspace.google.com/products/meet/");
        binding.web.getSettings().setJavaScriptEnabled(true);
        binding.web.setWebViewClient(new WebViewClient());
    }
}