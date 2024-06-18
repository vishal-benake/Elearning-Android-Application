package com.git.extc.activities;

import static com.git.extc.loaderConfiguarations.LoaderConfig.ISMAINTENANCE;
import static com.git.extc.loaderConfiguarations.LoaderConfig.LOADER;
import static com.git.extc.loaderConfiguarations.LoaderConfig.LOADER_VERSION;
import static com.git.extc.loaderConfiguarations.LoaderConfig.UPDATE_LINK;
import static com.git.extc.loaderConfiguarations.LoaderConfig.VERSION;
import static com.git.extc.loaderConfiguarations.LoaderConfig.isMaintenance;
import static com.git.extc.loaderConfiguarations.LoaderConfig.server_status;
import static com.git.extc.loaderConfiguarations.LoaderConfig.status_server;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.git.extc.BuildConfig;
import com.git.extc.R;
import com.git.extc.databinding.ActivitySignupBinding;
import com.git.extc.preferences.Preferences;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Signup extends AppCompatActivity {

    ActivitySignupBinding binding;
    Context context;
    public Preferences prefs;
    public String name,email,password,department;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        prefs = Preferences.with(this);
        if (!prefs.contains(LOADER_VERSION))
        {
            prefs.write(LOADER_VERSION, BuildConfig.VERSION_NAME);
        }

        prefs.write(isMaintenance, status_server);

        theredes();

        binding.TextInputLayoutemail.setHintEnabled(false);
        binding.TextInputEditTextemail.setHint("Email");

        binding.TextInputLayoutpassword.setHintEnabled(false);
        binding.TextInputEditTextpassword.setHint("Password");

        binding.TextInputLayoutdept.setHintEnabled(false);
        binding.TextInputEditTextdept.setHint("Department");

        binding.TextInputLayoutname.setHintEnabled(false);
        binding.TextInputEditTextname.setHint("FullName");

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

        binding.TextInputEditTextdept.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setBorder3();
                } else {
                    removeBorder3();
                }
            }
        });

        binding.TextInputEditTextname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setBorder4();
                } else {
                    removeBorder4();
                }
            }
        });

        binding.signintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Login.class);
                startActivity(intent);
            }
        });

        binding.signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_edit = binding.TextInputEditTextname.getText().toString().trim();
                String email_edit = binding.TextInputEditTextemail.getText().toString().trim();
                String password_edit = binding.TextInputEditTextpassword.getText().toString().trim();
                String dept_edit = binding.TextInputEditTextdept.getText().toString().trim();
                if (name_edit.isEmpty()) {
                    binding.TextInputEditTextname.setError("FullName cannot be empty");
                } else {
                    binding.TextInputEditTextname.setError(null); // Clear the error message
                }
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
                if (dept_edit.isEmpty()) {
                    binding.TextInputEditTextdept.setError("Department cannot be empty");
                } else {
                    binding.TextInputEditTextdept.setError(null); // Clear the error message
                }

                if (!name_edit.isEmpty() && !email_edit.isEmpty() && !password_edit.isEmpty() && !dept_edit.isEmpty() && binding.privacycheck.isChecked()) {

                    binding.logFrag.setVisibility(View.VISIBLE);
                    binding.signinlayoutholder.setVisibility(View.GONE);
                    binding.signupbtn.setVisibility(View.GONE);
                    binding.aboutLogin.setText(R.string.verifying_txt);
                    LottieLoadingDialog loadingDialog = new LottieLoadingDialog(context);
                    loadingDialog.setCancelable(false); // Prevent dialog from being dismissed by back button or touch outside
                    loadingDialog.show();

                    name = String.valueOf(binding.TextInputEditTextname.getText());
                    email = String.valueOf(binding.TextInputEditTextemail.getText());
                    password = String.valueOf(binding.TextInputEditTextpassword.getText());
                    department = String.valueOf(binding.TextInputEditTextdept.getText());


                    if (!name.equals("")  && !email.equals("") && !password.equals("") && !department.equals("")) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String[] field = new String[4];
                                field[0] = "fullname";
                                field[1] = "password";
                                field[2] = "email";
                                field[3] = "department";

                                String[] data = new String[4];
                                data[0] = name;
                                data[1] = password;
                                data[2] = email;
                                data[3] = department;
                                PutData putData = new PutData("https://study-server.xyz/studyroom/SignUp.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        loadingDialog.dismiss();
//                                    logFrag.setVisibility(View.GONE);
//                                    signup_button.setVisibility(View.VISIBLE);
                                        String result = putData.getResult();
                                        if (result.equals("Sign Up Success")) {
                                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                            Intent intent1 = new Intent(context, Login.class);
                                            startActivity(intent1);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                            binding.logFrag.setVisibility(View.GONE);
                                            binding.signupbtn.setVisibility(View.VISIBLE);
                                            binding.signinlayoutholder.setVisibility(View.VISIBLE);
                                        }

                                    }
                                }
                            }
                        });
                    } else {
                       // TastyToast.makeText(getApplicationContext(), "All fields required", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        Toast.makeText(context, "All fields required", Toast.LENGTH_SHORT).show();
                        binding.logFrag.setVisibility(View.GONE);
                        binding.signupbtn.setVisibility(View.VISIBLE);
                        binding.signinlayoutholder.setVisibility(View.VISIBLE);
                    }
                }

                if(!binding.privacycheck.isChecked()){
                    Toast.makeText(context, "Agree Terms ans Conditions before continuing..", Toast.LENGTH_SHORT).show();
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


    private void setBorder3() {
        Drawable border = createBorderDrawable();
        binding.TextInputLayoutdept.setBackground(border);
    }

    private void removeBorder3() {
        binding.TextInputLayoutdept.setBackgroundResource(R.drawable.inputlayout_bg);
    }

    private void setBorder4() {
        Drawable border = createBorderDrawable();
        binding.TextInputLayoutname.setBackground(border);
    }

    private void removeBorder4() {
        binding.TextInputLayoutname.setBackgroundResource(R.drawable.inputlayout_bg);
    }

    private Drawable createBorderDrawable() {
        // ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{2, 2, 2, 2, 2, 2, 2, 2}, null, null));
        shapeDrawable.getPaint().setColor(getResources().getColor(R.color.custom_gray));
        shapeDrawable.getPaint().setStyle(Paint.Style.STROKE);
        shapeDrawable.getPaint().setStrokeWidth(2);// Set border width here
        return shapeDrawable;
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