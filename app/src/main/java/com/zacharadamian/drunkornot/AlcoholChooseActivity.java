package com.zacharadamian.drunkornot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.view.View;
import android.widget.TextView;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class AlcoholChooseActivity extends AppCompatActivity {

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

    //---

    ArrayList<TextView> alcoholDescriptionCtrls;
    ArrayList<Alcohol> alcohols;

    private void initView() {
        constructContols();
        initControlsWithData();
    }

    private ArrayList<Alcohol> initData() {
        alcohols = new ArrayList<Alcohol>(
                Arrays.asList(
                        new Alcohol("Wódka 40%", new HashMap<Integer, Double>() {{
                            put(20, 6.41);
                            put(30, 9.62);
                            put(50, 16.03);
                        }}),
                        new Alcohol("Wódka 38%", new HashMap<Integer, Double>() {{
                            put(20, 6.09);
                            put(30, 9.13);
                            put(50, 15.22);
                        }})
                )
        );

        return alcohols;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alcohol_choose);
        initData();
        initView();

    }

    private void constructContols() {
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
    }

    private void initControlsWithData() {

        int controlsToFill = alcohols.size() <= alcoholDescriptionCtrls.size() ? alcohols.size() : alcoholDescriptionCtrls.size();

        for(int i = 0; i < controlsToFill; i++) {
            alcoholDescriptionCtrls.get(i).setText(alcohols.get(i).description);
        }

        for(int i = controlsToFill; i < alcoholDescriptionCtrls.size(); i++) {
            alcoholDescriptionCtrls.get(i).setVisibility(View.INVISIBLE);
        }
    }

}
