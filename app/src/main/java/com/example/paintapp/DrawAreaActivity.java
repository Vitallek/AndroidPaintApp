package com.example.paintapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.madrapps.pikolo.ColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;

public class DrawAreaActivity extends AppCompatActivity {

    private static int REQUEST_CODE_WRITE = 100;

    private DrawView drawView;

    private ImageButton btn_back_to_menu,
            btn_open_instruments,
            btn_palette,
            btn_eraser,
            btn_text,
            btn_undo,
            btn_save,
            btn_clear;

    private ImageButton btn_pick_color;
    private ConstraintLayout instrument_panel, colorPickerWrapper;
    private ColorPicker colorPicker;
    private SeekBar drawWidthBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_area);

        drawView = findViewById(R.id.draw_area);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            drawView.loadFromStorage((Uri) extras.get("uri"));
        }

        btn_back_to_menu = findViewById(R.id.btn_back_to_menu);
        btn_open_instruments = findViewById(R.id.btn_open_instruments);
        btn_open_instruments.setRotation(180);

        btn_palette = findViewById(R.id.btn_palette);
        btn_eraser = findViewById(R.id.btn_eraser);
        btn_text = findViewById(R.id.btn_text);
        btn_undo = findViewById(R.id.btn_undo);
        btn_save = findViewById(R.id.btn_save);
        btn_clear = findViewById(R.id.btn_clear);

        instrument_panel = findViewById(R.id.instrument_panel);


        drawWidthBar = findViewById(R.id.drawWidth);

        colorPicker = findViewById(R.id.colorPicker);
        colorPickerWrapper = findViewById(R.id.colorPickerWrapper);
        colorPickerWrapper.setAlpha(0f); //for animation
        colorPickerWrapper.setVisibility(View.GONE); //for animation
        colorPickerWrapper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        btn_pick_color = findViewById(R.id.btn_pick_color);

        btn_back_to_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DrawAreaActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        btn_eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.setDrawingColor(Color.WHITE);
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.clear();
            }
        });

        colorPicker.setColorSelectionListener(new SimpleColorSelectionListener() {

            @Override
            public void onColorSelected(int color) {
                // Do whatever you want with the color
                btn_pick_color.getBackground().setTint(color);
                //btn_pick_color.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);

                drawView.setDrawingColor(color);
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(DrawAreaActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    drawView.saveToInternalStorage();
                }else {
                    askPermissionWrite();
                }

            }
        });
        btn_pick_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorPickerWrapper.setVisibility(View.GONE);
                colorPickerWrapper.animate()
                        .alpha(0f)
                        .setDuration(300)
                        .setListener(null);
            }
        });

        btn_palette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorPickerWrapper.setVisibility(View.VISIBLE);
                colorPickerWrapper.animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(null);
            }
        });

        drawWidthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //change width of drawing line here
                drawView.setLineWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void askPermissionWrite() {
        ActivityCompat.requestPermissions(DrawAreaActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_WRITE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_WRITE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                drawView.saveToInternalStorage();
            }else {
                Toast.makeText(DrawAreaActivity.this,"Please provide the required permissions",Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}