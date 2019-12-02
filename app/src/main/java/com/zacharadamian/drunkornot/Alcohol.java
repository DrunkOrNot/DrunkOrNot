package com.zacharadamian.drunkornot;

import java.util.HashMap;

public class Alcohol {
    public Alcohol(String givenDescription, HashMap<Integer, Double> givenContents) {
        description = givenDescription;
        contents = givenContents;
    }
    public String description;
    public HashMap<Integer, Double> contents; // <ml, g>
}
