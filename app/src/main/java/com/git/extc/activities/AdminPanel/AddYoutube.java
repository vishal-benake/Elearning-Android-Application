package com.git.extc.activities.AdminPanel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.git.extc.R;
import com.git.extc.databinding.ActivityAddYoutubeBinding;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class AddYoutube extends AppCompatActivity {

    ActivityAddYoutubeBinding binding;
    public Context context;
    public String title, description, yt_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddYoutubeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;


        binding.uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_edit = binding.titleedittxt.getText().toString().trim();
                String descp_edit = binding.descpedittxt.getText().toString().trim();
                String yt_edit = binding.descpedittxt.getText().toString().trim();

                if (title_edit.isEmpty()) {
                    binding.titleedittxt.setError("Title cannot be empty");
                } else {
                    binding.titleedittxt.setError(null); // Clear the error message
                }
                if (descp_edit.isEmpty()) {
                    binding.descpedittxt.setError("Description cannot be empty");
                } else {
                    binding.descpedittxt.setError(null); // Clear the error message
                }
                if (yt_edit.isEmpty()) {
                    binding.youtubeedittxt.setError("Description cannot be empty");
                } else {
                    binding.youtubeedittxt.setError(null); // Clear the error message
                }

                if (!title_edit.isEmpty() && !descp_edit.isEmpty()) {
                    title = String.valueOf(binding.titleedittxt.getText());
                    description = String.valueOf(binding.descpedittxt.getText());
                    yt_url = String.valueOf(binding.youtubeedittxt.getText());


                    if (!title.equals("")  && !description.equals("") && !yt_url.equals("")) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String[] field = new String[3];
                                field[0] = "name";
                                field[1] = "description";
                                field[2] = "yt_url";

                                String[] data = new String[3];
                                data[0] = title;
                                data[1] = description;
                                data[2] = yt_url;

                                PutData putData = new PutData("https://study-server.xyz/studyroom/AddYoutubeUrl.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        String result = putData.getResult();
                                        if (result.equals("Upload Success")) {
                                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                }
                            }
                        });
                    } else {
                        Toast.makeText(context, "All fields required", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }
}