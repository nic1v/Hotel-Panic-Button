package com.example.android.avantealertsystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.MainThread;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.avantealertsystem.Fragments.AlertFragment;
import com.example.android.avantealertsystem.Fragments.AlertItem;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Attr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BackgroundWorker extends AsyncTask<String, Void, String>  {
    Context context;
    public BackgroundWorker(Context c){
        context = c;
    }
    public static String results="";
    public static ArrayList<String> resultsArray= new ArrayList<>();
    public static int lineCount=0;
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        JSONObject jsonObject;
        JSONArray jsonArray;
        String baseUrl = "http://10.1.10.141";
        String login_url = "http://10.1.10.141/api/login";
        String alert_url = "http://10.1.10.141/api/getactivealertbycid/92099/";


        if (type.equals("login")) {
            OkHttpClient client = new OkHttpClient();
            InputStream inStream = null;

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n    \"UserName\": \"avanteadmin\",\n    \"Password\": \"av701Q!pl,\",\n    \"CID\": \"92099\"\n}");
            Request request = new Request.Builder()
                    .url(login_url)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", "PostmanRuntime/7.13.0")
                    .addHeader("Accept", "*/*")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Postman-Token", "ff516183-62ac-4106-b4fc-4265d1650a70,b432bdd8-7df9-4d8c-bf8b-7e041d669510")
                    .addHeader("Host", "www.pavswebapi.com")
                    .addHeader("accept-encoding", "gzip, deflate")
                    .addHeader("content-length", "83")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("cache-control", "no-cache")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                Log.e("RESPONSE RESULTS",response.message());
                inStream = response.body().byteStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                String temp, line = "";
                while ((temp = bReader.readLine()) != null) {
                    line += temp;
                }
                //jsonObject = (JSONObject) new JSON(line).nextValue();
                Log.e("JSON RESULTS",line);
                line = line.substring(1,line.length()-1);

                UserDetails.token = line;
                return line;

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Connection Error","####################");
                return "ConnectionError";

            }
        }else if(type.equals("getAlerts")){
            OkHttpClient client = new OkHttpClient();
            InputStream inStream = null;

            Request request = new Request.Builder()
                    .url("http://10.1.10.141/api/getactivealertbycid/92099/"+UserDetails.token)
                    .get()
                    .addHeader("User-Agent", "PostmanRuntime/7.13.0")
                    .addHeader("Accept", "*/*")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Postman-Token", "fb8c08dd-147d-449a-8762-10b4c71675cf,2cacf9a4-a5f6-4220-bbae-554233d3b29b")
                    .addHeader("Host", "www.pavswebapi.com")
                    .addHeader("accept-encoding", "gzip, deflate")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("cache-control", "no-cache")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                inStream = response.body().byteStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                String temp, line = "";
                while ((temp = bReader.readLine()) != null) {
                    line += temp;
                }
                jsonArray = new JSONArray(line);
                final AlertFragment alertFragment = new AlertFragment();

                for(int i = 0; i<jsonArray.length();i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    Log.e("JSON RESULTS",jsonObject.getString("SeqID"));
                    UserDetails.activeAlerts.add(jsonObject);



                }


                for(int x=0;x<UserDetails.activeAlerts.size();x++)
                Log.e("USERDETAILS",UserDetails.activeAlerts.get(x)+"");
                return "alertsAdded";
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(type.equals("dismissAlerts")){
            OkHttpClient client = new OkHttpClient();
            String token1 = UserDetails.token;
            Log.e("TOKEN IN DISMISS", token1+"");
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{ \"UserName\":\"avanteadmin\", \"CID\":\"92099\", \"TOKEN\":\""+token1+"\", \"SeqID\":\""+UserDetails.selectedSeqID+"\", \"Comment\":\"\" }");
            Request request = new Request.Builder()
                    .url("http://10.1.10.141/api/dismissalertbyid")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "342980fa-82eb-4d81-8924-90ad5a93b31f")
                    .addHeader("Host", "www.pavswebapi.com")
                    .addHeader("accept-encoding", "gzip, deflate")
                    .addHeader("content-length", "113")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("cache-control", "no-cache")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()) return "fillAfterDismiss";
                else return "ConnectionError";

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


       return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    @MainThread
    protected void onPostExecute(String s) {

        if(s!=null) {

            switch (s) {

                case ("fillAfterDismiss"):
                    AlertFragment.removeViews();
                    AlertFragment.fillView();
                    break;
                case ("alertsAdded"):
                    AlertFragment.fillView();
                    break;
                case ("ConnectionError"):
                    Toast.makeText(context, "ConnectionError", Toast.LENGTH_SHORT).show();
                    AlertFragment.getmLinearLayout().removeViews(0,1);
                    TextView view1 = new TextView(context);
                    view1.setText("Connection Error");
                    view1.setGravity(Gravity.CENTER_HORIZONTAL);
                    view1.setTextSize(32f);
                    AlertFragment.getmLinearLayout().addView(view1);

                    break;
                    default: break;
            }
        }

    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
