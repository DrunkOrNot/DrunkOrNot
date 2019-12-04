package com.zacharadamian.drunkornot;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    TextView txtData;
    Button btnGo;
    Spinner spSex;
    EditText txtMass;
    Button btnSelectAlcohol;
    EditText txtEthanolIntake;
    Button btnCalculate;
    TextView txtBACResult;
    TextView txtSoberUpResult;

    ArrayAdapter<CharSequence> adapter;
    public void initView() {
        btnGo = findViewById(R.id.btnGo);
        txtData = this.findViewById(R.id.txtData);
        spSex = findViewById(R.id.spSex);
        txtMass = findViewById(R.id.editTextMass);
        txtEthanolIntake = findViewById(R.id.editTextEthanolIntake);
        btnCalculate = findViewById(R.id.btnCalculate);
        txtBACResult = findViewById(R.id.txtBACResult);
        txtSoberUpResult = findViewById(R.id.txtSoberingUpTimeResult);
        btnSelectAlcohol = findViewById(R.id.btnSelectAlcohol);
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
                            String result = Objects.requireNonNull(ds.child("ethanol").child("GAS_ALC").getValue()).toString();
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

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Sex sex = spSex.getSelectedItem().equals("Male") ? Sex.Male : Sex.Female;

                //Huge trycatch WIP
                try {
                    Body body = new Body(sex, Integer.valueOf(txtMass.getText().toString()));
                    txtBACResult.setText(String.valueOf(Calculations.CalculateBAC(body,
                            Ethanol.GetEthanolIntake(), 1))); //hardcoded drinkingSpan WIP

                    double resultMin = Calculations.CalculateMinSoberingUpTime
                            (body, Ethanol.GetEthanolIntake());

                    double resultMax = Calculations.CalculateMaxSoberingUpTime
                                    (body, Ethanol.GetEthanolIntake());

                    txtSoberUpResult.setText(
                            ConvertDoubleToTimeString(Calculations.CalculateMinSoberingUpTime
                                        (body, Ethanol.GetEthanolIntake()))
                            + " - " +
                            ConvertDoubleToTimeString(Calculations.CalculateMaxSoberingUpTime
                                        (body, Ethanol.GetEthanolIntake()))
                    );
                }
                catch (NumberFormatException e) {
                    txtBACResult.setText("Provided values are incorrect");
                }
            }
        });
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
