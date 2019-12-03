package com.zacharadamian.drunkornot;

import java.util.TreeMap;

public class Alcohol {
    public Alcohol(String givenDescription, TreeMap<Integer, Double> givenContents) {
        description = givenDescription;
        contents = givenContents;
    }
    public String description;
    public TreeMap<Integer, Double> contents; // <ml, g>
}
