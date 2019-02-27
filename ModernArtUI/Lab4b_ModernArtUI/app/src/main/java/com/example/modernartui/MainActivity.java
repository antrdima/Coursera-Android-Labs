package com.example.modernartui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    AlertDialog.Builder ad;
    String uriAddress = "https://www.moma.org/collection/works/79879?locale=en&on_view=1&page=8&with_images=1";
    String title = "Inspired by the works of Piet Mondrian ";
    String message = "Click below to learn more";
    String button1String = "Visit MOMA";
    String button2String = "Not now";
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7,
            imageView8, imageView9, imageView10, imageView11;
    ImageView[] yellowImageViews;
    ImageView[] redImageViews;
    ImageView[] blueImageViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ad = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_AppCompat_Dialog));
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.cancel();
            }
        });
        ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriAddress));
                startActivity(browserIntent);
            }
        });

        final SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        initializeImageViews();
    }

    private void initializeImageViews() {
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        imageView6 = findViewById(R.id.imageView6);
        imageView7 = findViewById(R.id.imageView7);
        imageView8 = findViewById(R.id.imageView8);
        imageView9 = findViewById(R.id.imageView9);
        imageView10 = findViewById(R.id.imageView10);
        imageView11 = findViewById(R.id.imageView11);
        yellowImageViews = new ImageView[]{imageView1, imageView2, imageView3,
                imageView6, imageView7, imageView11};
        redImageViews = new ImageView[]{imageView4, imageView9, imageView10};
        blueImageViews = new ImageView[]{imageView5, imageView8};
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_more_options) {
            createDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createDialog() {
        ad.show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        changeYellowImageViews(progress);
        changeRedImageViews(progress);
        changeBlueImageViews(progress);
    }

    private void changeBlueImageViews(int progress) {
        changeImageViews(blueImageViews, R.color.colorBlue, R.color.colorYellow, progress);
    }

    private void changeRedImageViews(int progress) {
        changeImageViews(redImageViews, R.color.colorOrange, R.color.colorBlue, progress);
    }

    private void changeYellowImageViews(int progress) {
        changeImageViews(yellowImageViews, R.color.colorYellow, R.color.colorOrange, progress);
    }

    private void changeImageViews(ImageView[] imageViews, int fromColorId, int toColorId, int progress) {
        int fromColor = getResources().getColor(fromColorId);
        int toColor = getResources().getColor(toColorId);
        int resultColor = getResultColor(fromColor, toColor, progress);

        for (ImageView imageView : imageViews) {
            imageView.setBackgroundColor(resultColor);
        }
    }

    private int getResultColor(int fromColor, int toColor, int progress) {
        int fromColorR = (fromColor >> 16) & 0xff;
        int fromColorG = (fromColor >> 8) & 0xff;
        int fromColorB = (fromColor) & 0xff;

        int toColorR = (toColor >> 16) & 0xff;
        int toColorG = (toColor >> 8) & 0xff;
        int toColorB = (toColor) & 0xff;

        int resultRed = fromColorR + (toColorR - fromColorR) * progress / 100;
        int resultGreen = fromColorG + (toColorG - fromColorG) * progress / 100;
        int resultBlue = fromColorB + (toColorB - fromColorB) * progress / 100;

        return Color.rgb(resultRed, resultGreen, resultBlue);
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
