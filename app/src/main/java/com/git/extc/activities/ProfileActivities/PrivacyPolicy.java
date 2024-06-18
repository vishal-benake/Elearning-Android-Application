package com.git.extc.activities.ProfileActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.git.extc.R;
import com.git.extc.activities.Home;
import com.git.extc.databinding.ActivityPrivacyPolicyBinding;
import com.git.extc.databinding.ActivityReportProblemBinding;

public class PrivacyPolicy extends AppCompatActivity {

    ActivityPrivacyPolicyBinding binding;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_privacy_policy);

        binding = ActivityPrivacyPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        binding.backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Home.class);
                intent.putExtra("returnToFragment", "Fragment4");
                startActivity(intent);
            }
        });
    }
}