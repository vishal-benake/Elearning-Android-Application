package com.git.extc.activities.AdminPanel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Toast;
import android.database.Cursor;
import com.git.extc.databinding.ActivityAddBooksBinding;
import com.vishnusivadas.advanced_httpurlconnection.PutData;
import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddBooks extends AppCompatActivity {

    ActivityAddBooksBinding binding;
    public Context context;

    public Uri uri;
    public String title, description, pdfname;

    private static final int PICK_PDF_REQUEST = 1;
    private Uri selectedPdfUri;

    private String selectedFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddBooksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        binding.pickPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPdfFile();
            }
        });

        binding.uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_edit = binding.titleedittxt.getText().toString().trim();
                String descp_edit = binding.descpedittxt.getText().toString().trim();

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

                if (selectedPdfUri != null) {
                    uploadPdfFile(selectedPdfUri);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select a PDF file first", Toast.LENGTH_SHORT).show();
                }

                if (!title_edit.isEmpty() && !descp_edit.isEmpty()) {
                    title = String.valueOf(binding.titleedittxt.getText());
                    description = String.valueOf(binding.descpedittxt.getText());
                    pdfname = String.valueOf(binding.selectedPdfTextView.getText());

                    if (!title.equals("")  && !description.equals("")) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String[] field = new String[3];
                                field[0] = "name";
                                field[1] = "description";
                                field[2] = "pdfname";

                                String[] data = new String[3];
                                data[0] = title;
                                data[1] = description;
                                data[2] = pdfname;

                                PutData putData = new PutData("https://study-server.xyz/studyroom/AddBook.php", "POST", field, data);
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

    private void selectPdfFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedPdfUri = data.getData();
            if (selectedPdfUri != null) {
                selectedFilePath = getFileName(selectedPdfUri);
                binding.selectedPdfTextView.setText(selectedFilePath);
            }
        }
    }

    private void uploadPdfFile(Uri pdfUri) {
        new UploadPdfTask(this).execute(pdfUri);
    }
    private static class UploadPdfTask extends AsyncTask<Uri, Void, String> {
        private static final String UPLOAD_URL = "https://study-server.xyz/studyroom/upload_pdf.php";
        private ProgressDialog progressDialog;
        private Context context;

        UploadPdfTask(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Uploading PDF...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Uri... uris) {
            Uri pdfUri = uris[0];
            String response = "";

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(UPLOAD_URL).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                String boundary = "*****";
                String lineEnd = "\r\n";

                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes("--" + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"pdfFile\"; filename=\"" + pdfUri.getLastPathSegment() + "\"" + lineEnd);
                outputStream.writeBytes(lineEnd);

                FileInputStream fileInputStream = (FileInputStream) context.getContentResolver().openInputStream(pdfUri);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                fileInputStream.close();

                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes("--" + boundary + "--" + lineEnd);
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    response = "PDF uploaded successfully.";
                } else {
                    response = "Error uploading PDF. Response code: " + responseCode;
                }
            } catch (IOException e) {
                e.printStackTrace();
                response = "Exception: " + e.getMessage();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                   result = cursor.getString(nameIndex);
               }
           } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
           }
        }
        return result;
    }
}