package com.herokuapp.punchcard_app.punchd;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScanner extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("info", rawResult.getText()); // Prints scan results
        Log.v("info", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)


        Intent intent= new Intent(BarcodeScanner.this,BufferActivity.class);
        ArrayList<Stores> storesList = (ArrayList<Stores>) getIntent().getSerializableExtra("_storesList");
        intent.putExtra("_storesList", storesList);
        intent.putExtra("_scanResult", rawResult.getText());
        intent.putExtra("_barcodeResult", rawResult.getBarcodeFormat().toString());
        startActivity(intent);

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }


}
