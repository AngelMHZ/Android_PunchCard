package com.herokuapp.punchcard_app.punchd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class BufferActivity extends AppCompatActivity {
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_test);
        Bundle extras = getIntent().getExtras();


        TextView text1 = (TextView) findViewById(R.id.textView);
        TextView text2 = (TextView) findViewById(R.id.textView2);

        text1.setText(extras.getString("_scanResult"));
        text2.setText(extras.getString("_barcodeResult"));

        prefs = getSharedPreferences("PrefsFile", MODE_PRIVATE);

        if(true){ // if the qr code is acceptable
            sendServerSync();

        }
        else{
            Intent intent = new Intent(BufferActivity.this, BarcodeScanner.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

    }

    private void sendServerSync() {
        new JSONServerSync().execute("https://punchcard-app.herokuapp.com/api/punches/");
    }

    class JSONServerSync extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;
        Bundle extras = getIntent().getExtras();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(BufferActivity.this);
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet(urls[0]);
                httppost.setHeader("Content-Type", "application/json");

                httppost.setHeader("Authorization", "JWT " + prefs.getString("token", ""));

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add (new BasicNameValuePair("key",extras.getString("_scanResult")));


                AbstractHttpEntity ent =  new UrlEncodedFormEntity(params, HTTP.UTF_8);


                HttpResponse response = httpclient.execute(httppost);
                HttpEntity resEntityGet = response.getEntity();


                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();


                Log.i("info1", response.getStatusLine().getStatusCode()+" -");


                return true;


                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();


            if(result == false) {
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
            }

            Intent intent= new Intent(BufferActivity.this,StoreDetails.class);
            ArrayList<Stores> storesList = (ArrayList<Stores>) getIntent().getSerializableExtra("_storesList");
            intent.putExtra("_storesList", storesList);
            intent.putExtra("_position", prefs.getInt("lastMyPlaces",0));
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }



}



