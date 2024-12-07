package com.example.cosc341_project.data_classes;

import java.util.HashMap;
import java.util.Map;

public class LocationList {
    private static HashMap<String, double[]> locationList;
    static {
        locationList = new HashMap<>();
        locationList.put("Mission Creek", new double[]{49.8625, -119.4550});
        locationList.put("Bear Creek Provincial Park", new double[]{49.9152, -119.5126});
        locationList.put("UBCO", new double[]{49.9394, -119.3948});
        locationList.put("Select Location", null);
    }

    public static double[] getCoordinates(String locationName) {
        return locationList.get(locationName);
    }
}
