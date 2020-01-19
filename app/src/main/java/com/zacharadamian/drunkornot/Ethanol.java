package com.zacharadamian.drunkornot;

public class Ethanol {

    private static Double m_ethanolIntake = 0.0;
    private static Double m_bac = 0.0;

    public static Double GetEthanolIntake() {
        return m_ethanolIntake;
    }

    public static void SetEthanolIntake(Double value) {
        m_ethanolIntake = value;
    }

    public static Double GetBAC() { return m_bac; }

    public static void SetBAC(Double value) { m_bac = value; }

    public static void ResetValues() {
        m_ethanolIntake = 0.0;
        m_bac = 0.0;
    }
}
