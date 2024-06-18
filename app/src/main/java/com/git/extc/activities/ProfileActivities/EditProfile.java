package com.git.extc.activities.ProfileActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;


import com.git.extc.R;
import com.git.extc.activities.Home;
import com.git.extc.activities.Login;
import com.git.extc.databinding.ActivityEditProfileBinding;
import com.git.extc.databinding.ActivityPrivacyPolicyBinding;
import com.git.extc.preferences.Preferences;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

public class EditProfile extends AppCompatActivity {

    ActivityEditProfileBinding binding;
    Context context;
    public Preferences prefs;
    public String password,department;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;
        prefs = Preferences.with(context);

        binding.TextInputLayoutPassname.setHintEnabled(false);
        binding.TextInputEditTextPassname.setHint("Change Password");

        binding.TextInputLayoutDeptname.setHintEnabled(false);
        binding.TextInputEditTextDeptname.setHint("Change Department");

        binding.TextInputEditTextPassname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setBorder2();
                } else {
                    removeBorder2();
                }
            }
        });

        binding.TextInputEditTextDeptname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setBorder3();
                } else {
                    removeBorder3();
                }
            }
        });


        binding.backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Home.class);
                intent.putExtra("returnToFragment", "Fragment4");
                startActivity(intent);
            }
        });

        String fullname1 = prefs.getString("fullname", "");
        binding.fullname.setText(fullname1);

        binding.changeDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dept_edit = binding.TextInputEditTextDeptname.getText().toString().trim();
                if (dept_edit.isEmpty()) {
                    binding.TextInputEditTextDeptname.setError("Department cannot be empty");
                } else {
                    binding.TextInputEditTextDeptname.setError(null); // Clear the error message
                }

                if ( !dept_edit.isEmpty()) {
                    department = String.valueOf(binding.TextInputEditTextDeptname.getText());
                    String email = prefs.getString("email", "");
                    if (!department.equals("")) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                String[] field = new String[2];
                                field[0] = "email";
                                field[1] = "department";

                                String[] data = new String[2];

                                data[0] = email;
                                data[1] = department;
                                PutData putData = new PutData("https://study-server.xyz/studyroom/update_department.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        String result = putData.getResult();
                                        String[] parts = result.split("\\|");
                                        String department = (parts.length > 1) ? parts[1] : "";
                                        prefs.write("department", department);
                                        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();


                                    } else {
                                            //TastyToast.makeText(getApplicationContext(), result, TastyToast.LENGTH_LONG, TastyToast.ERROR);
                                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                        });

                    }
                }

                }
        });

        binding.changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password_edit = binding.TextInputEditTextPassname.getText().toString().trim();
                if (password_edit.isEmpty()) {
                    binding.TextInputEditTextPassname.setError("Password cannot be empty");
                } else {
                    binding.TextInputEditTextPassname.setError(null); // Clear the error message
                }

                if ( !password_edit.isEmpty()) {
                    password = String.valueOf(binding.TextInputEditTextPassname.getText());
                    String email = prefs.getString("email", "");
                    if (!password.equals("")) {
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
                                PutData putData = new PutData("https://study-server.xyz/studyroom/update_password.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
//
                                        String result = putData.getResult();
                                        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();


                                    } else {
                                        //TastyToast.makeText(getApplicationContext(), result, TastyToast.LENGTH_LONG, TastyToast.ERROR);
                                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void setBorder2() {
        Drawable border = createBorderDrawable();
        binding.TextInputLayoutPassname.setBackground(border);
    }

    private void removeBorder2() {
        binding.TextInputLayoutPassname.setBackgroundResource(R.drawable.inp_lay_bg2);
    }


    private void setBorder3() {
        Drawable border = createBorderDrawable();
        binding.TextInputLayoutDeptname.setBackground(border);
    }

    private void removeBorder3() {
        binding.TextInputLayoutDeptname.setBackgroundResource(R.drawable.inp_lay_bg2);
    }

    private Drawable createBorderDrawable() {
        // ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{2, 2, 2, 2, 2, 2, 2, 2}, null, null));
        shapeDrawable.getPaint().setColor(getResources().getColor(R.color.custom_gray));
        shapeDrawable.getPaint().setStyle(Paint.Style.STROKE);
        shapeDrawable.getPaint().setStrokeWidth(2);// Set border width here
        return shapeDrawable;
    }
}