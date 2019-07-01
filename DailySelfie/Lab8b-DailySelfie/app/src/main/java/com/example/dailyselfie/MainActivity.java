package com.example.dailyselfie;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SelfieItemFragment.OnListFragmentInteractionListener {

    static final int REQUEST_TAKE_PHOTO = 1;
    private static final long TWO_MIN = 2 * 60 * 1000;
    private static final int MY_NOTIFICATION_ID = 1;

    String currentPhotoPath;

    SelfieItemFragment selfieItemFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);

        selfieItemFragment = new SelfieItemFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, selfieItemFragment);
        transaction.commit();

        InitializeContent();
        SetUpAlarm();
    }


    private void InitializeContent() {
        if (!SelfieItemContent.isEmpty())
            return;

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] imageFiles = storageDir.listFiles();

        for (File imageFile : imageFiles) {
            addNewImageItem(imageFile.getAbsolutePath());
        }
    }

    private void SetUpAlarm() {
        Notification notification = getNotification();
        scheduleNotification(notification);
    }

    private void scheduleNotification(Notification notification) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, MY_NOTIFICATION_ID);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + TWO_MIN;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME, futureInMillis, pendingIntent);
    }

    private Notification getNotification() {
        final Intent restartMainActivityIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                MY_NOTIFICATION_ID,
                restartMainActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setContentTitle("Daily Selfie")
                .setContentText("Time for another selfie")
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        return notificationBuilder.build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_take_photo:
                TakePhoto();
                return true;
            case R.id.action_remove_selfies:
                RemoveSelfies();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void TakePhoto() {
        dispatchTakePictureIntent();
    }

    private void RemoveSelfies() {
        selfieItemFragment.notifyClear();
        SelfieItemContent.clearAllItems();

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] imageFiles = storageDir.listFiles();

        for (File imageFile : imageFiles) {
            imageFile.delete();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("TakePicture", ex.getMessage());
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    takePictureIntent.setClipData(ClipData.newRawUri("", photoURI));
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            addNewImageItem(currentPhotoPath);
            selfieItemFragment.notifyDataChanged();
        }
    }

    private void addNewImageItem(String path) {
        File imageFile = new File(path);
        String imageName = imageFile.getName();

        SelfieItemContent.SelfieItem selfieItem = SelfieItemContent.createSelfieItem(imageName,
                path);
        SelfieItemContent.addItem(selfieItem);
    }

    @Override
    public void onListFragmentInteraction(SelfieItemContent.SelfieItem item) {
        return;
    }

    @Override
    public void onImageClick(SelfieItemContent.SelfieItem item) {
        Intent intent = new Intent(this, FullScreenImageActivity.class);
        intent.putExtra("path", item.imagePath);
        startActivity(intent);
    }
}
