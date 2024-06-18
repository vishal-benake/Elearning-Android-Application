package com.git.extc.activities.AdminPanel;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUploader extends AsyncTask<Void, Integer, Boolean> {
    private Context mContext;
    private String mFileUrl;
    private String mFilePath;

    public FileUploader(Context context, String fileUrl, String filePath) {
        mContext = context;
        mFileUrl = fileUrl;
        mFilePath = filePath;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(mContext, "Starting upload...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        FileInputStream fileInputStream = null;

        try {
            File file = new File(mFilePath);
            URL url = new URL(mFileUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=*****");
            connection.setRequestProperty("uploaded_file", mFilePath);

            outputStream = new DataOutputStream(connection.getOutputStream());
            fileInputStream = new FileInputStream(file);
            int totalSize = fileInputStream.available();
            int bytesRead;
            byte[] buffer = new byte[4096];
            int uploadedSize = 0;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                uploadedSize += bytesRead;
                publishProgress((int) ((uploadedSize * 100) / totalSize));
            }

            outputStream.flush();

            int serverResponseCode = connection.getResponseCode();
            if (serverResponseCode == 200) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connection != null)
                connection.disconnect();
        }

        return false;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Toast.makeText(mContext, "Upload progress: " + values[0] + "%", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        if (success) {
            Toast.makeText(mContext, "File uploaded successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Failed to upload file.", Toast.LENGTH_SHORT).show();
        }
    }
}
