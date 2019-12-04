package com.zacharadamian.drunkornot;

public class Ethanol {

    private static Double m_ethanolIntake = 0.0;

    public static Double GetEthanolIntake() {
        return m_ethanolIntake;
    }

    public static void SetEthanolIntake(Double value) {
        m_ethanolIntake = value;
    }
}
