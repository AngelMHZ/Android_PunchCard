package com.herokuapp.punchcard_app.punchd;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.session.MediaSession;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.security.KeyChain;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {

    ArrayList<Stores> storesList;
    StoreAdapter adapter;
    SharedPreferences prefs;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setBottomButtons();

        storesList = new ArrayList<Stores>();
        prefs = getSharedPreferences("PrefsFile", MODE_PRIVATE);

        getServerSync();

        ListView listview = (ListView)findViewById(R.id.list);
        adapter = new StoreAdapter(getApplicationContext(), R.layout.row, storesList);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long id) {
                //when an item is clicked
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), storesList.get(position).getName(), Toast.LENGTH_LONG).show();
                Intent intent= new Intent(MainActivity.this,StoreDetails.class);
                intent.putExtra("_storesList", storesList);
                intent.putExtra("_position", position);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("lastMyPlaces", position);
                editor.apply();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }
        });

    }

    private void setBottomButtons() {
        Button myPlacesBtn = (Button) this.findViewById(R.id.toolbar_titleBottom3);
        Button punchBtn = (Button) this.findViewById(R.id.toolbar_titleBottom2);

        myPlacesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,StoreDetails.class);
                intent.putExtra("_storesList", storesList);
                intent.putExtra("_position", prefs.getInt("lastMyPlaces",0));
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        punchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BarcodeScanner.class);
                intent.putExtra("_storesList", storesList);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }


    private void getServerSync() {
       new JSONServerSync().execute("https://punchcard-app.herokuapp.com/api/businesses/");
    }

    class JSONServerSync extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                //------------------>>
                HttpGet httppost = new HttpGet(urls[0]);
                httppost.setHeader("Content-Type", "application/json");

                httppost.setHeader("Authorization","JWT " + prefs.getString("token",""));

                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);


                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();
                Log.i("info", "STATUS CODE: " + Integer.toString(status));
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    Log.i("info", data);

                    JSONArray jarray = new JSONArray(data);

                    Log.i("info", jarray.toString());

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);
                        //Log.i("info1", object.getString("name"));
                        JSONArray offerArray = new JSONArray(jarray.getJSONObject(i).getString("offer_set"));
                        JSONObject offerObject = offerArray.getJSONObject(0);

                        Stores store = new Stores();

                        store.setID(object.getString("id"));
                        store.setURL(object.getString("url"));
                        store.setName(object.getString("name"));
                        store.setAddress(object.getString("address"));
                        store.setLink(object.getString("link"));

                        //Log.i("info", offerObject.getString("name"));
                        store.setOfferName(offerObject.getString("name"));
                        store.setOfferDesc(offerObject.getString("description"));
                        store.setOfferPunchReq(offerObject.getString("punch_total_required"));
                        store.setOfferPunchCur(offerObject.getString("max_instances"));


                        storesList.add(store);
                    }
                    return true;
                }

                //------------------>>

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();
            adapter.notifyDataSetChanged();
            if(result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }


}
