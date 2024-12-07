package com.example.cosc341_project.data_classes;

import java.util.HashMap;
import java.util.Map;

public class LocationList {
    private static HashMap<String, double[]> locationList;
    static {
        locationList = new HashMap<>();
        locationList.put("UBCO", new double[]{49.9394, -119.3948});
        locationList.put("Glenrosa", new double[]{49.8337, -119.6605});
        locationList.put("Kin Beach, Vernon", new double[]{50.2493, -119.3486});
        locationList.put("Penticton City Center", new double[]{49.4991, -119.5937});
        locationList.put("Rutland", new double[]{49.8900, -119.3954});
        locationList.put("Select Location", null);
    }

    public static double[] getCoordinates(String locationName) {
        return locationList.get(locationName);
    }
}
