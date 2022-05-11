package com.example.paintapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class DrawAreaActivity extends AppCompatActivity {

    private DrawView drawView;

    private ImageButton btn_back_to_menu,
            btn_open_instruments,
            btn_palette,
            btn_eraser,
            btn_text,
            btn_undo,
            btn_save,
            btn_clear;

    private ConstraintLayout instrument_panel;
    private SeekBar drawWidthBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_area);

        drawView = findViewById(R.id.draw_area);

        btn_back_to_menu = findViewById(R.id.btn_back_to_menu);
        btn_open_instruments = findViewById(R.id.btn_open_instruments);
        btn_palette = findViewById(R.id.btn_palette);
        btn_eraser = findViewById(R.id.btn_eraser);
        btn_text = findViewById(R.id.btn_text);
        btn_undo = findViewById(R.id.btn_undo);
        btn_save = findViewById(R.id.btn_save);
        btn_clear = findViewById(R.id.btn_clear);

        instrument_panel = findViewById(R.id.instrument_panel);
        drawWidthBar = findViewById(R.id.drawWidth);

        btn_back_to_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DrawAreaActivity.this, MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.clear();
            }
        });

        drawWidthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
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
}