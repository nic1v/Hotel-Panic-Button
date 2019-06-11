package com.example.android.avantealertsystem;

import android.view.View;

import org.json.JSONObject;

import java.util.ArrayList;

public class UserDetails {
    public static String userName = "";
    public static int cid = 0;
    public static String token = "";
    public static String password ="";
    public static ArrayList<JSONObject> activeAlerts= new ArrayList<>();
    public static int selectedSeqID = 0;
}
