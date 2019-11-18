package com.zacharadamian.drunkornot;


enum Sex {
    Male,
    Female
}

public class Body {

    public Sex sex = Sex.Male;;
    public int mass =  -1; // in kg

    Body() {

    }

    Body(Sex givenSex, int givenMass) {
        sex = givenSex;
        mass = givenMass;
    }

    public double GetMetabolicConstant() {
        return sex == Sex.Male ? 0.015d : 0.017d ;
    };

    public double GetWaterConstant() {
        return sex == Sex.Male ? 0.58d : 0.49d;
    };
}
