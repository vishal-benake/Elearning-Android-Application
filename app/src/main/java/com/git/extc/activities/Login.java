package com.git.extc.activities;

import static com.git.extc.loaderConfiguarations.LoaderConfig.ISMAINTENANCE;
import static com.git.extc.loaderConfiguarations.LoaderConfig.LOADER;
import static com.git.extc.loaderConfiguarations.LoaderConfig.LOADER_VERSION;
import static com.git.extc.loaderConfiguarations.LoaderConfig.UPDATE_LINK;
import static com.git.extc.loaderConfiguarations.LoaderConfig.VERSION;
import static com.git.extc.loaderConfiguarations.LoaderConfig.isMaintenance;
import static com.git.extc.loaderConfiguarations.LoaderConfig.server_status;
import static com.git.extc.loaderConfiguarations.LoaderConfig.status_server;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;

import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import android.Manifest;
import com.git.extc.R;
import com.git.extc.BuildConfig;
import com.git.extc.databinding.ActivityLoginBinding;

import com.git.extc.preferences.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {

    ActivityLoginBinding binding;
    public Preferences prefs;
    public Context context;
    public String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        theredes();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        prefs = Preferences.with(this);
        if (!prefs.contains(LOADER_VERSION))
        {
            prefs.write(LOADER_VERSION, BuildConfig.VERSION_NAME);
        }

        String verifications = prefs.read("oAuthVerificationOpenedIsConfirmed?");
        if (verifications.equals("confirmedVerifyOauth"))
        {
            Intent intent2 = new Intent(context, Home.class);
            startActivity(intent2);
            finish();
        }



        prefs.write(isMaintenance, status_server);

        binding.TextInputLayoutemail.setHintEnabled(false);
        binding.TextInputEditTextemail.setHint("Email");

        binding.TextInputLayoutpassword.setHintEnabled(false);
        binding.TextInputEditTextpassword.setHint("Password");

        binding.TextInputEditTextemail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setBorder1();
                } else {
                    removeBorder1();
                }
            }
        });

        binding.signuptxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Signup.class);
                startActivity(intent);
            }
        });


        binding.TextInputEditTextpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setBorder2();
                } else {
                    removeBorder2();
                }
            }
        });

        binding.loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_edit = binding.TextInputEditTextemail.getText().toString().trim();
                String password_edit = binding.TextInputEditTextpassword.getText().toString().trim();

                if (email_edit.isEmpty()) {
                    binding.TextInputEditTextemail.setError("Email cannot be empty");
                } else {
                    binding.TextInputEditTextemail.setError(null); // Clear the error message
                }

                if (password_edit.isEmpty()) {
                    binding.TextInputEditTextpassword.setError("Password cannot be empty");
                } else {
                    binding.TextInputEditTextpassword.setError(null); // Clear the error message
                }

                if ( !email_edit.isEmpty() && !password_edit.isEmpty()) {

                    binding.logFrag.setVisibility(View.VISIBLE);
                    //binding.forgotpass.setVisibility(View.GONE);
                    binding.signuplayoutlogin.setVisibility(View.GONE);
                    binding.loginbutton.setVisibility(View.GONE);
                    binding.aboutLogin.setText(R.string.verifying_txt);
                    LottieLoadingDialog loadingDialog = new LottieLoadingDialog(context);
                    loadingDialog.setCancelable(false); // Prevent dialog from being dismissed by back button or touch outside
                    loadingDialog.show();

                    email = String.valueOf(binding.TextInputEditTextemail.getText());
                    password = String.valueOf(binding.TextInputEditTextpassword.getText());


                    if (!email.equals("") && !password.equals("")) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {


                                String[] field = new String[2];
                                field[0] = "email";
                                field[1] = "password";

                                String[] data = new String[2];
                                data[0] = email;
                                data[1] = password;

                                PutData putData = new PutData("https://study-server.xyz/studyroom/login.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        loadingDialog.dismiss();
//                                    logFrag.setVisibility(View.GONE);
//                                    login_button.setVisibility(View.VISIBLE);
                                        String result = putData.getResult();
                                        String[] parts = result.split("\\|");
                                        String status = parts[0];
                                        if (status.equals("Login Success")) {
                                          //  String fullName = (parts.length > 1) ? parts[1] : ""; // Full name is at index 1 if available
                                          //  String department = (parts.length > 1) ? parts[2] : "";
                                            //String verificationStatus = checkVerificationStatus(username);
                                            if (parts.length >= 3) { // Ensure there are enough parts
                                                String fullName = parts[1];
                                                String department = parts[2];
                                                String verificationStatus = checkVerificationStatus(fullName);
                                                prefs.write("fullname", fullName);
                                                prefs.write("department", department);
                                                prefs.write("VerificationIsConfirmed?", verificationStatus);
                                            }
                                            prefs.write("oAuthVerificationOpenedIsConfirmed?", "confirmedVerifyOauth");
                                           // prefs.write("fullname", fullName);
                                            prefs.write("email", email);
                                            //prefs.write("department", department);

                                            //TastyToast.makeText(getApplicationContext(), result, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                                            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                                            Intent intent1 = new Intent(context, Home.class);
                                            startActivity(intent1);
                                            finish();
                                        } else {
                                           // TastyToast.makeText(getApplicationContext(), result, TastyToast.LENGTH_LONG, TastyToast.ERROR);
                                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            binding.logFrag.setVisibility(View.GONE);
                                            binding.loginbutton.setVisibility(View.VISIBLE);
                                          //  binding.forgotpass.setVisibility(View.VISIBLE);
                                            binding.signuplayoutlogin.setVisibility(View.VISIBLE);
                                        }

                                    }
                                }
                            }
                        });
                    } else {
                        //TastyToast.makeText(getApplicationContext(), "All fields required", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        Toast.makeText(context, "All fields required", Toast.LENGTH_SHORT).show();
                        binding.logFrag.setVisibility(View.GONE);
                        binding.loginbutton.setVisibility(View.VISIBLE);
                        //binding.forgotpass.setVisibility(View.VISIBLE);
                        binding.signuplayoutlogin.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void setBorder1() {
        Drawable border = createBorderDrawable();
        binding.TextInputLayoutemail.setBackground(border);
    }

    private void removeBorder1() {
        binding.TextInputLayoutemail.setBackgroundResource(R.drawable.inputlayout_bg);
    }

    private void setBorder2() {
        Drawable border = createBorderDrawable();
        binding.TextInputLayoutpassword.setBackground(border);
    }

    private void removeBorder2() {
        binding.TextInputLayoutpassword.setBackgroundResource(R.drawable.inputlayout_bg);
    }

    private Drawable createBorderDrawable() {
       // ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{2, 2, 2, 2, 2, 2, 2, 2}, null, null));
        shapeDrawable.getPaint().setColor(getResources().getColor(R.color.custom_gray));
        shapeDrawable.getPaint().setStyle(Paint.Style.STROKE);
        shapeDrawable.getPaint().setStrokeWidth(2);// Set border width here
        return shapeDrawable;
    }

    private String checkVerificationStatus(String fullname) {
        String[] field = new String[1];
        field[0] = "fullname";

        String[] data = new String[1];
        data[0] = fullname;

        PutData putData = new PutData("https://study-server.xyz/studyroom/check_verification.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                return putData.getResult();
            }
        }
        // Handle the case where the verification check fails
        return null;
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