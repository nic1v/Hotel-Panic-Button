package com.example.android.avantealertsystem.Fragments;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.avantealertsystem.BackgroundWorker;
import com.example.android.avantealertsystem.R;
import com.example.android.avantealertsystem.UserDetails;

import org.json.JSONException;


public class AlertFragment extends Fragment {
    private static LinearLayout mLinearLayout;

    private Vibrator vibrator;
    private static TextView noActiveAlerts;
    private SwipeRefreshLayout swipeRefreshLayout;
    public AlertFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alert, container, false);
        vibrator =  (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        noActiveAlerts = (TextView) rootView.findViewById(R.id.tv_no_alerts);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.scrollView2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BackgroundWorker backgroundWorker = new BackgroundWorker(getContext());
                backgroundWorker.execute("getAlerts");
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        setmLinearLayout((LinearLayout) rootView.findViewById(R.id.ll_alerts));
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.toolbar_layout);
        appBarLayout.setTitle("Active Alerts:");

        appBarLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));





        return rootView;
    }

    public static LinearLayout fillView(){
        Log.e("USERDETAILS",UserDetails.activeAlerts.size()+"");

        for(int i = 0; i<UserDetails.activeAlerts.size();i++){
            noActiveAlerts.setVisibility(View.GONE);



            final View alertItem = new AlertItem().onCreateView(LayoutInflater.from(getmLinearLayout().getContext()),mLinearLayout,null,i);
            final int finalI = i;
            alertItem.setTag("ALERTTAG");

            alertItem.setOnLongClickListener(new View.OnLongClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public boolean onLongClick(View v) {
                    ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
                    ClipData dragData = new ClipData((CharSequence) v.getTag(),new String[]{
                            ClipDescription.MIMETYPE_TEXT_PLAIN},item);

                    View.DragShadowBuilder myShadow = new View.DragShadowBuilder(alertItem);

                    v.startDrag(dragData,myShadow,null,0);

                    return true;
                    }
                });




            alertItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        v.setElevation(20f);
                        UserDetails.selectedSeqID = UserDetails.activeAlerts.get(finalI).getInt("SeqID");
                        Log.e("INSIDE CLICK",""+ UserDetails.activeAlerts.get(finalI).getInt("SeqID"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            mLinearLayout.addView(alertItem);
        }
        return mLinearLayout;
    }



    public static void removeViews(){
        mLinearLayout.removeViews(0,mLinearLayout.getChildCount());
    }

    public static LinearLayout getmLinearLayout(){
        return mLinearLayout;
    }
    public static void setmLinearLayout(LinearLayout mLinearLayout) {
        AlertFragment.mLinearLayout = mLinearLayout;
    }


}
