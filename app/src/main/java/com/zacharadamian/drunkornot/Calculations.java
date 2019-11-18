package com.zacharadamian.drunkornot;

public class Calculations {
    //BAC - Blood Alcohol Content (https://en.wikipedia.org/wiki/Blood_alcohol_content)
    //ethanolMass in grams [g]
    public static double CalculateBAC(Body body, int ethanolMass, int drinkingSpan) {
        return (( (0.806d  * 1.2d * (ethanolMass / 10.0d) )
                / (body.GetWaterConstant() * body.mass) )
                - (body.GetMetabolicConstant() * drinkingSpan)) * 0.9d;
    };

    public static double CalculateMinSoberingUpTime(Body body, int ethanolMass) {
        return ethanolMass / body.mass * 0.11d ;
    };

    public static double CalculateMaxSoberingUpTime(Body body, int ethanolMass) {
        return ethanolMass / body.mass * 0.9d ;
    };
}
