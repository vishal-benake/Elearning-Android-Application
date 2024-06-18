package com.git.extc.activities.Semesters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.git.extc.R;
import com.git.extc.activities.Semesters.Backend.Semester8.Subject1;
import com.git.extc.activities.Semesters.Backend.Semester8.Subject2;
import com.git.extc.activities.Semesters.Backend.Semester8.Subject3;
import com.git.extc.activities.Semesters.Backend.Semester8.Subject4;

public class Semester8 extends AppCompatActivity {

    public LinearLayout subject1, subject2, subject3, subject4;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester8);

        context = this;

        subject1 = findViewById(R.id.subject1);
        subject1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Subject1.class);
                startActivity(intent);
            }
        });

        subject2 = findViewById(R.id.subject2);
        subject2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Subject2.class);
                startActivity(intent);
            }
        });

        subject3 = findViewById(R.id.subject3);
        subject3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Subject3.class);
                startActivity(intent);
            }
        });

        subject4 = findViewById(R.id.subject4);
        subject4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Subject4.class);
                startActivity(intent);
            }
        });
    }
}