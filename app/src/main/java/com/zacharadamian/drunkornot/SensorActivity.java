package com.zacharadamian.drunkornot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;

public class SensorActivity extends AppCompatActivity {
    TextView txtData;
    EditText txtIpAddress;
    Button btnStartSensor;
    Button btnFetchData;
    TextView txtInstructions;
    AppCompatActivity currentWindow = this;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        txtData = findViewById(R.id.txtData);
        txtIpAddress = findViewById(R.id.txtIpAddress);
        btnStartSensor = findViewById(R.id.btnStartSensor);
        btnFetchData = findViewById(R.id.btnFetchData);
        txtInstructions = findViewById(R.id.SensorInstr);

        this.setTitle(getString(R.string.str_empty));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnStartSensor.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                String raspberryIP = String.valueOf(txtIpAddress.getText());
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                RequestBody body = RequestBody.create(mediaType, "on_click_button=true");

                Request request = new Request.Builder()
                        .url("http://" + raspberryIP + "/mq/info.php")
                        .post(body)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("Accept", "*/*")
                        .addHeader("Cache-Control", "no-cache")
                        .addHeader("Host", raspberryIP)
                        .addHeader("Accept-Encoding", "gzip, deflate")
                        .addHeader("Connection", "keep-alive")
                        .addHeader("cache-control", "no-cache")
                        .build();

                Call call = client.newCall(request);
                CompletableFuture.runAsync(() -> {
                    try {
                        Response response = call.execute();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                CompletableFuture.runAsync(() -> updateTxtAsync());
            }
        });


        btnFetchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("MQ3");
                Query last = mDatabase.orderByKey().limitToLast(1);
                last.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String ethanol = Objects.requireNonNull(ds.child("ethanol").getValue()).toString();
                            String time = Objects.requireNonNull(ds.child("time").getValue()).toString();
                            String result = time + ethanol;
                            UIHelper.DisplayAlertWithText(result, currentWindow);
                            Ethanol.SetBAC(Double.valueOf(ethanol));
                        }
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        txtData.setText("error");
                    }
                });

                SensorActivity.this.startActivity(new Intent(SensorActivity.this, NextActivity.class));
            }
        });

    }
    void updateTxtAsync() {
        long counter = 10000;
        long start = System.currentTimeMillis();
        long destTime = start + counter;
        while(System.currentTimeMillis() < destTime)
            txtInstructions.setText((getString(R.string.str_calibrating)) +' ' + String.valueOf((System.currentTimeMillis() - destTime) / -1000 ));

        long blowFor = 5000;
        start = System.currentTimeMillis();
        destTime = start + blowFor;
        while(System.currentTimeMillis() < destTime)
            txtInstructions.setText ((getString(R.string.str_blow))+' ' + String.valueOf((System.currentTimeMillis() - destTime) / -1000 ));

        txtInstructions.setText(getString(R.string.str_DataReady));


    }
}
