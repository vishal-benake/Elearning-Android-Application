package com.git.extc.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import com.git.extc.activities.Semesters.Backend.Semester8.Subject1;
import com.git.extc.activities.Semesters.Semester8;
import com.git.extc.databinding.ActivityPdfBinding;
import com.git.extc.databinding.SemEightActivitySubject1Binding;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.git.extc.R;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;


public class PDFActivity extends Activity implements OnLoadCompleteListener, OnPageErrorListener {

    //ProgressBar PdfViewProgressBar;
    ActivityPdfBinding binding;
    Context context;

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String STORAGE_DIRECTORY = "GitExtc";
    private int nextFileNumber = 1;

    public String fileName;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_pdf);

        binding = ActivityPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        LottieLoadingDialog loadingDialog = new LottieLoadingDialog(context);
        loadingDialog.setCancelable(false); // Prevent dialog from being dismissed by back button or touch outside
        loadingDialog.show();



        // final PDFView pdfView = findViewById(R.id.pdfView);
      //  PdfViewProgressBar = findViewById(R.id.PdfViewProgressBar);
        binding.PdfViewProgressBar.setVisibility(View.VISIBLE);

        Intent intent = this.getIntent();
        fileName = intent.getExtras().getString("NAME");

        Intent i= this.getIntent();
        final String path=i.getExtras().getString("PATH");



        FileLoader.with(this)
                .load(path, false)
                .fromDirectory("My_PDFs", FileLoader.DIR_INTERNAL)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest request, FileResponse<File> response) {
                        binding.PdfViewProgressBar.setVisibility(View.GONE);
                        loadingDialog.dismiss();
                        File pdfFile = response.getBody();
                        try
                        {
                            binding.pdfView.fromFile(pdfFile)
                                    .defaultPage(1)
                                    .enableAnnotationRendering(true)
                                    .onLoad(PDFActivity.this)
                                    .scrollHandle(new DefaultScrollHandle(PDFActivity.this))
                                    .spacing(10)
                                    .onPageError(PDFActivity.this)
                                    .load();
                        } catch (Exception e) {
                           e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(FileLoadRequest request, Throwable t) {
                        binding.PdfViewProgressBar.setVisibility(View.GONE);
                        loadingDialog.dismiss();
                       // TastyToast.makeText(getApplicationContext(),t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                    }

                });




        binding.downloadpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    new DownloadPDF().execute(path);
                } else {
                    requestPermission();
                }
            }
        });
    }

    @Override
    public void onPageError(int page, Throwable t) {
        binding.PdfViewProgressBar.setVisibility(View.GONE);
       // TastyToast.makeText(getApplicationContext(),t.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
    }

    @Override
    public void loadComplete(int nbPages) {
        binding.PdfViewProgressBar.setVisibility(View.GONE);
       // TastyToast.makeText(getApplicationContext(),String.valueOf(nbPages), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent i= this.getIntent();
                final String path=i.getExtras().getString("PATH");
                new DownloadPDF().execute(path);
            } else {
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void createFolder() {
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), STORAGE_DIRECTORY);
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                Log.d("Folder", "Folder created successfully");
            } else {
                Log.e("Folder", "Failed to create folder");
            }
        }
    }


    private class DownloadPDF extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            createFolder();
            String fileUrl = strings[0];
            try {
                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + STORAGE_DIRECTORY, fileName+".pdf");
              //  File outputFile = new File(Environment.getExternalStorageDirectory() + File.separator + STORAGE_DIRECTORY, "example.pdf");
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                InputStream inputStream = urlConnection.getInputStream();

                byte[] buffer = new byte[1024];
                int bufferLength;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, bufferLength);
                }

                fileOutputStream.close();
                inputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "PDF downloaded successfully", Toast.LENGTH_SHORT).show();
        }

    }



}
