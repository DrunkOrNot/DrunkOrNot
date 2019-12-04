package com.zacharadamian.drunkornot;

public class Calculations {
    //BAC - Blood Alcohol Content (https://en.wikipedia.org/wiki/Blood_alcohol_content)
    //ethanolMass in grams [g]
    public static Double CalculateBAC(Body body, Double ethanolMass, int drinkingSpan) {
        return (( (0.806d  * 1.2d * (ethanolMass / 10.0d) )
                / (body.GetWaterConstant() * body.mass) )
                - (body.GetMetabolicConstant() * drinkingSpan)) * 0.9d;
    };

    public static Double CalculateMinSoberingUpTime(Body body, Double ethanolMass) {
        return ethanolMass / ((double) body.mass * 0.11d );
    };

    public static Double CalculateMaxSoberingUpTime(Body body, Double ethanolMass) {
        return ethanolMass / ((double) body.mass * 0.09d );
    };
}
