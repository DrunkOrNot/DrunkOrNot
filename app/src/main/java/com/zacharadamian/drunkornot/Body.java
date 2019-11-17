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

    //BAC - Blood Alcohol Content (https://en.wikipedia.org/wiki/Blood_alcohol_content)
    //ethanolMass in grams [g]
    public double CalculateBAC(int ethanolMass, int drinkingSpan) {
        return (( (0.806d  * 1.2d * (ethanolMass / 10.0d) )
                / (GetWaterConstant() * mass) )
                - (GetMetabolicConstant() * drinkingSpan)) * 0.9d;
    };

    private double GetMetabolicConstant() {
        return sex == Sex.Male ? 0.015d : 0.017d ;
    };

    private double GetWaterConstant() {
        return sex == Sex.Male ? 0.58d : 0.49d;
    };
}
