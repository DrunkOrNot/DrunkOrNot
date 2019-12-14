package com.zacharadamian.drunkornot;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.FutureTask;

import java.io.IOException;
import java.util.Objects;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    TextView txtData;
    Button btnGo;
    Button btnSensor;
    Button btnSelectAlcohol;
    Button btnCalculate;
    EditText txtMass;
    EditText txtEthanolIntake;
    EditText txtIpAddress;
    Spinner spSex;

    ArrayAdapter<CharSequence> adapter;
    public void initView() {
        btnGo = findViewById(R.id.btnGo);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnSensor = findViewById(R.id.btnSensor);
        btnSelectAlcohol = findViewById(R.id.btnSelectAlcohol);
        txtData = this.findViewById(R.id.txtData);
        txtMass = findViewById(R.id.txtMass);
        txtIpAddress = findViewById(R.id.txtIpAddress);
        txtEthanolIntake = findViewById(R.id.txtEthanolIntake);
        spSex = findViewById(R.id.spSex);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_array, android.R.layout.simple_dropdown_item_1line);
        spSex.setAdapter(adapter);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("MQ3");
                Query last = mDatabase.orderByKey().limitToLast(1);
                last.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String result = Objects.requireNonNull(ds.child("ethanol").getValue()).toString();
                            txtData.setText(result);
                        }
                    }
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        txtData.setText("error");
                    }
                });
            }
        });

        txtEthanolIntake.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Double alcoholIntake = Double.valueOf(txtEthanolIntake.getText().toString());
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

            }});

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Sex sex = spSex.getSelectedItem().equals("Male") ? Sex.Male : Sex.Female;

                try {
                    Body body = new Body(sex, Integer.valueOf(txtMass.getText().toString()));
                    Double bac = Calculations.CalculateBAC(body,
                            Ethanol.GetEthanolIntake(), 1); //hardcoded drinkingSpan WIP

                    double minTime = Calculations.CalculateMinSoberingUpTime
                            (body, Ethanol.GetEthanolIntake());

                    double maxTime = Calculations.CalculateMaxSoberingUpTime
                                    (body, Ethanol.GetEthanolIntake());

                    DisplayAlertWithText(FormatCalculationResultForAlert(body, bac, minTime, maxTime));
                }
                catch (NumberFormatException e) {
                    DisplayAlertWithText("Provided values are incorrect");
                }
            }
        });
    }

    private String FormatCalculationResultForAlert(Body body, Double bac, Double minTime, Double maxTIme) {
        String result = "Your Blood Alcohol Content is:\n";
        result += String.format("%.2f", bac) + "\n";
        result += "You will sober up in:\n";
        result += ConvertDoubleToTimeString(minTime)
                        + " - " + ConvertDoubleToTimeString(maxTIme);
        return result;

    }

    private void DisplayAlertWithText(String text) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(text);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private String ConvertDoubleToTimeString(double value) {
        if (value == 0.0)
            return "0 min";

        String result = (int) Math.floor(value) + " h " +
                        (int) Math.floor(((value - Math.floor(value)) * 60))
                        + " min";
        return result;
    }
}
