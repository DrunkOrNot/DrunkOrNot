package com.zacharadamian.drunkornot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    Button btnSensor;
    Button btnManual;
    Button btnSelectAlcohol;
    EditText txtEthanolIntake;
    ImageButton btnInfo;
    AppCompatActivity currentWindow = this;


    public void initView() {
        btnSensor = findViewById(R.id.btnSensor);
        btnManual = findViewById(R.id.btnManual);
        btnSelectAlcohol = findViewById(R.id.btnSelectAlcohol);
        txtEthanolIntake = findViewById(R.id.txtEthanolIntake);
        btnInfo = findViewById(R.id.btnInfo);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        txtEthanolIntake.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(String.valueOf(txtEthanolIntake.getText()).equals(("")))
                    return;
                Double alcoholIntake = Double.valueOf(String.valueOf(txtEthanolIntake.getText()));
                if(alcoholIntake != 0.0)
                    Ethanol.SetEthanolIntake(alcoholIntake);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        btnSelectAlcohol.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, AlcoholChooseActivity.class));
            }
            });

        btnSensor.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, SensorActivity.class));
            }});

        btnManual.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, NextActivity.class));
            }});

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alertTxt = getString(R.string.str_authors) + "\n";
                //
                StringBuffer stringBuffer = new StringBuffer();
                InputStream is = getResources().openRawResource(R.raw.info);
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(is));
                try {
                    String data;
                    while ((data = bufferedreader.readLine()) != null) {
                        alertTxt += data;
                        alertTxt += "\n";
                    }
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                UIHelper.DisplayAlertWithText(alertTxt, currentWindow);
            }
        });
    }
}
