package com.git.extc.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import com.airbnb.lottie.LottieAnimationView;
import com.git.extc.R;

public class LottieLoadingDialog extends Dialog {

    public LottieLoadingDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_lottie_loading);

        LottieAnimationView animationView = findViewById(R.id.lottie_animation_view);
        animationView.setAnimation(R.raw.loading);
        animationView.loop(true);
        animationView.playAnimation();
    }
}
