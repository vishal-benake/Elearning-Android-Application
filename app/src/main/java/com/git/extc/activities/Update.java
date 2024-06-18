package com.git.extc.activities;

import static com.git.extc.loaderConfiguarations.LoaderConfig.ISMAINTENANCE;
import static com.git.extc.loaderConfiguarations.LoaderConfig.LOADER;
import static com.git.extc.loaderConfiguarations.LoaderConfig.LOADER_VERSION;
import static com.git.extc.loaderConfiguarations.LoaderConfig.UPDATE_INFO;
import static com.git.extc.loaderConfiguarations.LoaderConfig.UPDATE_LINK;
import static com.git.extc.loaderConfiguarations.LoaderConfig.UPDATE_LOADER;
import static com.git.extc.loaderConfiguarations.LoaderConfig.UPDATE_TTL;
import static com.git.extc.loaderConfiguarations.LoaderConfig.VERSION;
import static com.git.extc.loaderConfiguarations.LoaderConfig.isMaintenance;
import static com.git.extc.loaderConfiguarations.LoaderConfig.server_status;
import static com.git.extc.loaderConfiguarations.LoaderConfig.status_server;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.git.extc.BuildConfig;
import com.git.extc.R;
import com.git.extc.databinding.ActivityUpdateBinding;
import com.git.extc.preferences.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Update extends AppCompatActivity {

    ActivityUpdateBinding binding;
    public Preferences prefs;
    public ProgressDialog mPDialog;
    public Context context;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 123;
    private long downloadID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        prefs = Preferences.with(this);

        theredes();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        binding.updateloadererrorinfo.setText("Common issues while update:\n\nIf you are facing 'Apk not installed', Goto /sdcard/android/data/" + getPackageName() + "/files/(You'll get a apk named: GITEXTC.apk)\n\n* Get a backup of the latest loader(GITEXTC.apk) in your internal storage or as your wish.\n* After backup done, Uninstall old loader\n* After uninstall compleate, install the latest loader(GITEXTC.apk).\n\n\n********** \n\nAndroid version error like: 'There was a problem parsing the package', kindly contact with any of our admin. \n\n**********");

        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        binding.downloadloader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestStoragePermission();

                runOnUiThread(() -> {
                    mPDialog = new ProgressDialog(new ContextThemeWrapper(context, com.google.android.material.R.style.Theme_Material3_Dark_Dialog));
                    mPDialog.setMessage("Downloading...");
                    mPDialog.setIndeterminate(true);

                    mPDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mPDialog.setCancelable(false);
                    mPDialog.show();
                });
            }
        });

        binding.continuewithcurrentversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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



                final String updateLogs = update.getJSONObject(UPDATE_TTL).getString(UPDATE_INFO);
                final String currAppVersion = prefs.read(LOADER_VERSION, BuildConfig.VERSION_NAME);

                final String newLoaderVersion = update.getJSONObject(LOADER).getString(VERSION);
                final String currentLoaderVersion = prefs.read(LOADER_VERSION, BuildConfig.VERSION_NAME);

                runOnUiThread(()->binding.updateinfotxt.setText(String.format("current version: %s | New version: %s\n\nWhat's new:\n%s",currAppVersion, newLoaderVersion, updateLogs, UPDATE_LINK)));

                runOnUiThread(()->binding.currentversion.setText(String.format("%s", currAppVersion)));
                runOnUiThread(()->binding.upcomingVersion.setText(String.format("%s", newLoaderVersion)));

            } catch (IOException | JSONException e) {
                runOnUiThread(()->{
                    Toast.makeText(context,""+e, Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }


    private void checkAndRequestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_REQUEST_CODE);
            } else {
                downloadAndInstallApk();
            }
        } else {
            downloadAndInstallApk();
        }
    }

    private void downloadAndInstallApk() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(UPDATE_LOADER));
        File externalFilesDir = getExternalFilesDir(null);
        if (externalFilesDir != null) {
            String apkFileName = "Studyroom.apk";
            File apkFile = new File(externalFilesDir, apkFileName);
            if (apkFile.exists()) {
                apkFile.delete();
            }
            request.setDestinationUri(Uri.fromFile(apkFile));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            downloadID = downloadManager.enqueue(request);
        }
    }

    private void installApk() {
        try {

            File externalFilesDir = getExternalFilesDir(null);
            if (externalFilesDir != null) {
                File apkFile = new File(externalFilesDir, "GITEXTC.apk");

                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT >= 24) {
                    Uri downloaded_apk = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", apkFile);
                    intent.setDataAndType(downloaded_apk, "application/vnd.android.package-archive");
                    List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        this.grantUriPermission(this.getApplicationContext().getPackageName() + ".provider", downloaded_apk, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } else {
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                    intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//installApk


    private final BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (id == downloadID) {
                // Download completed
                if (mPDialog != null)
                {
                    mPDialog.dismiss();
                }
                //TastyToast.makeText(getApplicationContext(), "Loader Downloaded Please Wait ", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                Toast.makeText(context,"Loader Downloaded Please Wait", Toast.LENGTH_SHORT).show();
                installApk();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        if(!Preferences.isPermissionGranted(this))
        {

            new AlertDialog.Builder(this)
                    .setTitle("Git Extc")
                    .setMessage("Allow Git Extc to Access your Device Storage")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)

                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            takePermission();
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            denyPermission();
                        }
                    })
                    .show();
        }
        else
        {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show();
            //TastyToast.makeText(getApplicationContext(), "Permission Granted!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
        }
    }

    private void denyPermission()
    {
        Toast.makeText(context, "Something Went Wrong! Please Restart Loader", Toast.LENGTH_SHORT).show();
        // TastyToast.makeText(getApplicationContext(), "Something Went Wrong! Please Restart Loader!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
    }

    private void takePermission()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            try
            {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");

                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);

            } catch (Exception e)
            {
                e.printStackTrace();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 101);

            }
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 101);
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadAndInstallApk();
            } else {
                //TastyToast.makeText(getApplicationContext(), "Storage permission denied", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                Toast.makeText(context, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }

        if(grantResults.length > 0)
        {
            if(requestCode == 101)
            {
                boolean readExt = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(!readExt)
                {
                    takePermission();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }

    @Override
    public void onBackPressed() {
      //  super.onBackPressed();

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