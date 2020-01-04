package com.zacharadamian.drunkornot;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class NextActivity extends AppCompatActivity {
    Button btnCalculate;
    EditText txtMass;
    Spinner spSex;
    ArrayAdapter<CharSequence> adapter;
    AppCompatActivity currentWindow = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        btnCalculate = findViewById(R.id.btnCalculate);
        txtMass = findViewById(R.id.txtMass);
        spSex = findViewById(R.id.spSex);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_array, android.R.layout.simple_dropdown_item_1line);
        spSex.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Sex sex = spSex.getSelectedItemPosition() == 0 ? Sex.Male : Sex.Female;

                try {
                    Body body = new Body(sex, Integer.valueOf(txtMass.getText().toString()));
                    Double bac = Calculations.CalculateBAC(body,
                            Ethanol.GetEthanolIntake(), 1); //hardcoded drinkingSpan WIP

                    double minTime = Calculations.CalculateMinSoberingUpTime
                            (body, Ethanol.GetEthanolIntake());

                    double maxTime = Calculations.CalculateMaxSoberingUpTime
                                    (body, Ethanol.GetEthanolIntake());

                    UIHelper.DisplayAlertWithText(
                            UIHelper.FormatCalculationResultForAlert(bac, minTime, maxTime, currentWindow), currentWindow);
                }
                catch (NumberFormatException e) {
                    UIHelper.DisplayAlertWithText(getString(R.string.str_incorrectValues), currentWindow);
                }
            }
        });
    }

}
