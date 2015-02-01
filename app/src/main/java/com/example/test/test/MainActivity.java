package com.example.test.test;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter.CreateBeamUrisCallback;
import android.net.Uri;
import android.nfc.NfcAdapter.CreateBeamUrisCallback;
import java.util.Locale;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.Arrays;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends ActionBarActivity{

    String send;
    NdefRecord beamThis;
    EditText name;
    EditText phone;
    EditText email;

    //byte[] textBytes = send.getBytes();

    //NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
    // NdefRecord.RTD_TEXT, new byte[0], textBytes);


    Button sendInfo;


    public String collectInfo() {
        EditText name = (EditText) findViewById(R.id.name);
        EditText phone = (EditText) findViewById(R.id.phoneNumber);
        EditText email = (EditText) findViewById(R.id.email);

        StringBuilder sb = new StringBuilder();
        sb.append(name.getText().toString() + ";");
        sb.append(phone.getText() + ";");
        sb.append(email.getText() + ";");

        return sb.toString();
    }

    //Translates input string into a byte array
    public NdefRecord createTextRecord(String payload) {
        byte[] textBytes = payload.getBytes();
        byte[] data = new byte[1 + textBytes.length];
        data[0] = (byte) 0;
        System.arraycopy(textBytes, 0, data, 1, textBytes.length);
        NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], data);
        //NdefMessage beamThis = new NdefMessage(record);
        return record;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //send = collectInfo();


        //if (nfcAdapter == null) return;  // NFC not available on this device

        final NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // Defining Buttons
        sendInfo = (Button) findViewById(R.id.sendInfo);

        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send out the Ndef message object on button click
                send = collectInfo();

                beamThis = createTextRecord(send);
                final NdefMessage finalPayload = new NdefMessage(beamThis);

                nfcAdapter.setNdefPushMessage(finalPayload, MainActivity.this);
            }
        };
        sendInfo.setOnClickListener(listener);


        //name = (EditText) findViewById(R.id.name);
        //phone = (EditText) findV
        // iewById(R.id.phoneNumber);
        //email = (EditText) findViewById(R.id.email);

        //phone.setOnClickListener(this);
        //name.setOnClickListener(this);
        //email.setOnClickListener(this);
    }
}
