package com.zacharadamian.drunkornot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class AlcoholChooseActivity extends AppCompatActivity {

    Button btnConfirm;

    //---ALCOHOL DESCRIPTIONS

    TextView txtAlcohol;
    TextView txtAlcohol1;
    TextView txtAlcohol2;
    TextView txtAlcohol3;
    TextView txtAlcohol4;
    TextView txtAlcohol5;
    TextView txtAlcohol6;
    TextView txtAlcohol7;
    TextView txtAlcohol8;
    TextView txtAlcohol9;
    TextView txtAlcohol10;
    TextView txtAlcohol11;
    TextView txtAlcohol12;
    TextView txtAlcohol13;

    //---AMOUNT SPINNERS

    Spinner amount;
    Spinner amount1;
    Spinner amount2;
    Spinner amount3;
    Spinner amount4;
    Spinner amount5;
    Spinner amount6;
    Spinner amount7;
    Spinner amount8;
    Spinner amount9;
    Spinner amount10;
    Spinner amount11;
    Spinner amount12;
    Spinner amount13;

    //---COUNT SPINNERS

    Spinner count;
    Spinner count1;
    Spinner count2;
    Spinner count3;
    Spinner count4;
    Spinner count5;
    Spinner count6;
    Spinner count7;
    Spinner count8;
    Spinner count9;
    Spinner count10;
    Spinner count11;
    Spinner count12;
    Spinner count13;

    int activeControlsCount = 0; // is set in initControlsWithData();
    ArrayList<TextView> alcoholDescriptionCtrls;
    ArrayList<Spinner> alcoholAmountCtrls;
    ArrayList<Spinner> countCtrls;
    ArrayList<Alcohol> alcohols;

    private ArrayList<Alcohol> initData() {
        alcohols = new ArrayList<Alcohol>(
                Arrays.asList(
                        new Alcohol(getString(R.string.str_txtAlcohol), new TreeMap<Integer, Double>() {{
                            put(20, 6.41);
                            put(30, 9.62);
                            put(50, 16.03);
                        }}),
                        new Alcohol(getString(R.string.str_txtAlcohol1), new TreeMap<Integer, Double>() {{
                            put(20, 6.09);
                            put(30, 9.13);
                            put(50, 15.22);
                        }}),
                        new Alcohol(getString(R.string.str_txtAlcohol2), new TreeMap<Integer, Double>() {{
                            put(20, 6.01);
                            put(30, 9.01);
                            put(50, 15.02);
                        }}),
                        new Alcohol(getString(R.string.str_txtAlcohol3), new TreeMap<Integer, Double>() {{
                            put(120, 14.42);
                            put(150, 18.03);
                            put(200, 24.04);
                        }}),
                        new Alcohol(getString(R.string.str_txtAlcohol4), new TreeMap<Integer, Double>() {{
                            put(120, 11.54);
                            put(150, 14.42);
                            put(200, 19.23);
                        }}),
                        new Alcohol(getString(R.string.str_txtAlcohol5), new TreeMap<Integer, Double>() {{
                            put(120, 9.13);
                            put(150, 11.42);
                            put(200, 15.22);
                        }}),
                        new Alcohol(getString(R.string.str_txtAlcohol6), new TreeMap<Integer, Double>() {{
                            put(330, 18.51);
                            put(500, 28.05);
                            put(1000, 56.09);
                        }}),
                        new Alcohol(getString(R.string.str_txtAlcohol7), new TreeMap<Integer, Double>() {{
                            put(330, 15.87);
                            put(500, 24.04);
                            put(1000, 48.08);
                        }}),
                        new Alcohol(getString(R.string.str_txtAlcohol8), new TreeMap<Integer, Double>() {{
                            put(330, 14.81);
                            put(500, 22.44);
                            put(1000, 44.87);
                        }}),
                        new Alcohol(getString(R.string.str_txtAlcohol9), new TreeMap<Integer, Double>() {{
                            put(330, 13.75);
                            put(500, 20.83);
                            put(1000, 41.67);
                        }})
                )
        );

        return alcohols;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alcohol_choose);
        this.setTitle(getString(R.string.str_empty));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initData();
        constructControls();
        initControlsWithData();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ethanol.SetEthanolIntake(getEthanolAmountFromView());
                AlcoholChooseActivity.this.startActivity(new Intent(AlcoholChooseActivity.this, NextActivity.class));
                setResult(AlcoholChooseActivity.RESULT_OK, getIntent());
                finish();
            }});

    }

    private void initControlsWithData() {

        activeControlsCount = alcohols.size() <= alcoholDescriptionCtrls.size() ? alcohols.size() : alcoholDescriptionCtrls.size();

        int counterLimit = 20;
        ArrayList<Integer> counts = new ArrayList<Integer>(counterLimit);
        for(int i = 0; i < counterLimit; i++)
            counts.add(i);

        for(int i = 0; i < activeControlsCount; i++) {
            // Set descriptions
            alcoholDescriptionCtrls.get(i).setText(alcohols.get(i).description);

            // Set amount spinner
            ArrayList<String> spinnerData = new ArrayList<String>(alcohols.get(i).contents.size());

            alcohols.get(i).contents.forEach((k, v)->
                    spinnerData.add(String.valueOf(k + " ml")));

            ArrayAdapter adapter = new ArrayAdapter(
                    getApplicationContext(),
                    android.R.layout.simple_spinner_item,
                    spinnerData
            );

            alcoholAmountCtrls.get(i).setAdapter(adapter);

            // Set counter spinner
            ArrayAdapter countAdapter = new ArrayAdapter(
                    getApplicationContext(),
                    android.R.layout.simple_spinner_item,
                    counts
            );

            countCtrls.get(i).setAdapter(countAdapter);

        }

        // Set unused controls invisible
        for(int i = activeControlsCount; i < alcoholDescriptionCtrls.size(); i++) {
            alcoholDescriptionCtrls.get(i).setVisibility(View.INVISIBLE);
            alcoholAmountCtrls.get(i).setVisibility(View.INVISIBLE);
            countCtrls.get(i).setVisibility(View.INVISIBLE);
        }
    }

    private Double getEthanolAmountFromView() {
        Double ethanol = 0.0;
        for(int i = 0; i < activeControlsCount; i++)
        {
            Integer countVal = Integer.valueOf(countCtrls.get(i).getSelectedItem().toString());
            if(countVal > 0) {
                String amountStr = alcoholAmountCtrls.get(i).getSelectedItem().toString();
                Integer amount = Integer.valueOf(amountStr.substring(0, amountStr.length() - 3));
                String name = alcoholDescriptionCtrls.get(i).getText().toString();

                for (Alcohol alcohol : alcohols) {
                    if(alcohol.description.equals(name)) {
                        ethanol += alcohol.contents.get(amount) * countVal;
                    };
                }
            }
        }

        return ethanol;
    }

    private void constructControls() {

        btnConfirm = findViewById(R.id.btnConfirm);

        //---ALCOHOL DESCRIPTIONS

        txtAlcohol = findViewById(R.id.txtAlcohol);
        txtAlcohol1 = findViewById(R.id.txtAlcohol1);
        txtAlcohol2 = findViewById(R.id.txtAlcohol2);
        txtAlcohol3 = findViewById(R.id.txtAlcohol3);
        txtAlcohol4 = findViewById(R.id.txtAlcohol4);
        txtAlcohol5 = findViewById(R.id.txtAlcohol5);
        txtAlcohol6 = findViewById(R.id.txtAlcohol6);
        txtAlcohol7 = findViewById(R.id.txtAlcohol7);
        txtAlcohol8 = findViewById(R.id.txtAlcohol8);
        txtAlcohol9 = findViewById(R.id.txtAlcohol9);
        txtAlcohol10 = findViewById(R.id.txtAlcohol10);
        txtAlcohol11 = findViewById(R.id.txtAlcohol11);
        txtAlcohol12 = findViewById(R.id.txtAlcohol12);
        txtAlcohol13 = findViewById(R.id.txtAlcohol13);

        alcoholDescriptionCtrls = new ArrayList<TextView>(14);
        alcoholDescriptionCtrls.add(txtAlcohol  );
        alcoholDescriptionCtrls.add(txtAlcohol1 );
        alcoholDescriptionCtrls.add(txtAlcohol2 );
        alcoholDescriptionCtrls.add(txtAlcohol3 );
        alcoholDescriptionCtrls.add(txtAlcohol4 );
        alcoholDescriptionCtrls.add(txtAlcohol5 );
        alcoholDescriptionCtrls.add(txtAlcohol6 );
        alcoholDescriptionCtrls.add(txtAlcohol7 );
        alcoholDescriptionCtrls.add(txtAlcohol8 );
        alcoholDescriptionCtrls.add(txtAlcohol9 );
        alcoholDescriptionCtrls.add(txtAlcohol10);
        alcoholDescriptionCtrls.add(txtAlcohol11);
        alcoholDescriptionCtrls.add(txtAlcohol12);
        alcoholDescriptionCtrls.add(txtAlcohol13);

        //---AMOUNT SPINNERS

        amount = findViewById(R.id.spinner01);
        amount1 = findViewById(R.id.spinner11);
        amount2 = findViewById(R.id.spinner21);
        amount3 = findViewById(R.id.spinner31);
        amount4 = findViewById(R.id.spinner41);
        amount5 = findViewById(R.id.spinner51);
        amount6 = findViewById(R.id.spinner61);
        amount7 = findViewById(R.id.spinner71);
        amount8 = findViewById(R.id.spinner81);
        amount9 = findViewById(R.id.spinner91);
        amount10 = findViewById(R.id.spinner101);
        amount11 = findViewById(R.id.spinner111);
        amount12 = findViewById(R.id.spinner121);
        amount13 = findViewById(R.id.spinner131);

        alcoholAmountCtrls = new ArrayList<Spinner>(14);
        alcoholAmountCtrls.add(amount);
        alcoholAmountCtrls.add(amount1);
        alcoholAmountCtrls.add(amount2);
        alcoholAmountCtrls.add(amount3);
        alcoholAmountCtrls.add(amount4);
        alcoholAmountCtrls.add(amount5);
        alcoholAmountCtrls.add(amount6);
        alcoholAmountCtrls.add(amount7);
        alcoholAmountCtrls.add(amount8);
        alcoholAmountCtrls.add(amount9);
        alcoholAmountCtrls.add(amount10);
        alcoholAmountCtrls.add(amount11);
        alcoholAmountCtrls.add(amount12);
        alcoholAmountCtrls.add(amount13);

        // COUNT SPINNERS

        count = findViewById(R.id.spinner02);
        count1 = findViewById(R.id.spinner12);
        count2 = findViewById(R.id.spinner22);
        count3 = findViewById(R.id.spinner32);
        count4 = findViewById(R.id.spinner42);
        count5 = findViewById(R.id.spinner52);
        count6 = findViewById(R.id.spinner62);
        count7 = findViewById(R.id.spinner72);
        count8 = findViewById(R.id.spinner82);
        count9 = findViewById(R.id.spinner92);
        count10 = findViewById(R.id.spinner102);
        count11 = findViewById(R.id.spinner112);
        count12 = findViewById(R.id.spinner122);
        count13 = findViewById(R.id.spinner132);

        countCtrls = new ArrayList<Spinner>(14);
        countCtrls.add(count);
        countCtrls.add(count1);
        countCtrls.add(count2);
        countCtrls.add(count3);
        countCtrls.add(count4);
        countCtrls.add(count5);
        countCtrls.add(count6);
        countCtrls.add(count7);
        countCtrls.add(count8);
        countCtrls.add(count9);
        countCtrls.add(count10);
        countCtrls.add(count11);
        countCtrls.add(count12);
        countCtrls.add(count13);

    }

}
