package com.example.android.avantealertsystem.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.avantealertsystem.R;
import com.example.android.avantealertsystem.UserDetails;

import org.json.JSONException;

import java.util.Calendar;
import java.util.Date;

public class AlertItem extends Fragment {
    private TextView tvAlertNumber;
    private TextView textClock;
    private ImageView alertSymbol;


    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, int i) {
        View rootView = inflater.inflate(R.layout.item_alert,container,false);

        super.onCreateView(inflater,container,savedInstanceState);
        tvAlertNumber = (TextView) rootView.findViewById(R.id.alert_number);

        try {
            tvAlertNumber.setText(UserDetails.activeAlerts.get(i).get("SeqID").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        alertSymbol = (ImageView) rootView.findViewById(R.id.imageView3);
        alertSymbol.setImageResource(android.R.drawable.ic_dialog_alert);
        textClock = (TextView) rootView.findViewById(R.id.et_time);
        Date currentTime = Calendar.getInstance().getTime();

        String date = DateFormat.format("hh:mm:ss a\nMM-dd-yyyy", currentTime).toString();
        textClock.setText(date);
       // textClock.setFormat12Hour("hh:mm:ss a\ndd:MMM:yyyy");
       // textClock.getFormat12Hour();




        return rootView;
    }

    public TextView getTvAlertNumber(){
        return tvAlertNumber;
    }
}
