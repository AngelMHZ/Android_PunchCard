package com.herokuapp.punchcard_app.punchd;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StoreDetails extends AppCompatActivity {

    ArrayList<Stores> storesList;
    int position;

    public TextView storeName;
    public TextView storeAddress;
    public TextView storeRewardDesc;
    public TextView storeRewardCur;
    public TextView storeRewardReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_store_details);
        setToolbar();
        setBottomButtons();
        setExtras();
        setTextView();
        setRedeem();

        storeName.setText("  " + storesList.get(position).getName());
        storeAddress.setText("  " + storesList.get(position).getAddress());
        storeRewardDesc.setText("  " + storesList.get(position).getOfferName());
        storeRewardCur.setText("  " + storesList.get(position).getOfferPunchCur());
        storeRewardReq.setText(storesList.get(position).getOfferPunchReq());

        TextView backText = (TextView) findViewById(R.id.toolbar_backButton);
        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }

        });

    }

    private void setRedeem(){
        int current = Integer.parseInt(storesList.get(position).getOfferPunchCur());
        int required = Integer.parseInt(storesList.get(position).getOfferPunchReq());

        if(current == required){
            Button redeemButton = (Button) findViewById(R.id.RedeemButton);
            redeemButton.setBackgroundTintList(ColorStateList.valueOf(Color.CYAN));
            redeemButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(), "You have redeemed the offer.", Toast.LENGTH_SHORT).show();
                }
            });
        }
       else{
           Button redeemButton = (Button) findViewById(R.id.RedeemButton);
            redeemButton.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }
    }

    private void setBottomButtons() {
        Button allPlacesBtn = (Button) this.findViewById(R.id.toolbar_titleBottom1);
        Button punchBtn = (Button) this.findViewById(R.id.toolbar_titleBottom2);

        allPlacesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        punchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(StoreDetails.this,BarcodeScanner.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);            }
        });
    }

    private void setTextView() {
        storeName = (TextView)findViewById(R.id.storeName);
        storeAddress = (TextView)findViewById(R.id.storeAddress);
        storeRewardDesc = (TextView)findViewById(R.id.rewardDesc);
        storeRewardCur = (TextView)findViewById(R.id.PunchCurrent);
        storeRewardReq = (TextView)findViewById(R.id.PunchRequired);
    }

    private void setExtras() {
        Bundle extras = getIntent().getExtras();
        storesList = (ArrayList<Stores>) getIntent().getSerializableExtra("_storesList");
        position = extras.getInt("_position");
        //Log.i("info", position + "");
        //Log.i("info", storesList.get(position).getAddress());
    }


    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }




}
