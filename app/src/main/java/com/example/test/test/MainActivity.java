package com.example.test.test;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileOutputStream;
import java.io.FileInputStream;

import android.view.WindowManager;
import android.widget.Toast;

import java.util.regex.Pattern;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends ActionBarActivity{

    String send;
    NdefRecord beamThis;
    EditText name;
    EditText phone;
    EditText email;

    String[] PRESERVED = new String[3];

    FileOutputStream fos;
    FileInputStream fis;
    StringBuilder sb;
    SharedPreferences prefs;
    private static final String PREFS_NAME = "mysharedprefs";


    Button sendInfo;

    public String collectInfo(){
        sb = new StringBuilder();
        sb.append(name.getText().toString() + ";");
        sb.append(phone.getText() + ";");
        sb.append(email.getText() + ";");

        PRESERVED[0] = name.getText().toString();
        PRESERVED[1] = phone.getText().toString();
        PRESERVED[2] = email.getText().toString();
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
        prefs = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        //get a value
        PRESERVED[0] = prefs.getString("name", null);
        PRESERVED[1] = prefs.getString("phone", null);
        PRESERVED[2] = prefs.getString("email", null);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //if (nfcAdapter == null) return;  // NFC not available on this device

        final NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        //Initialize views
        name = (EditText)findViewById(R.id.name);
        phone = (EditText)findViewById(R.id.phoneNumber);
        email = (EditText)findViewById(R.id.email);

        PRESERVED[0] = prefs.getString("name", null);
        PRESERVED[1] = prefs.getString("phone", null);
        PRESERVED[2] = prefs.getString("email", null);
        name.setText(PRESERVED[0]);
        phone.setText(PRESERVED[1]);
        email.setText(PRESERVED[2]);

        // Defining Buttons
        sendInfo = (Button) findViewById(R.id.sendInfo);

        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                String test = email.getText().toString();
                Context context = getApplicationContext();

                //Only checks for Email format when the field is not empty
                if(test.length() > 1 && !checkEmail(email.getText().toString())){
                    Toast.makeText(context, "Please check your email format", Toast.LENGTH_LONG).show();
                }
                else {
                    //Send out the Ndef message object on button click
                    send = collectInfo();

                    //Save user info to savedInstanceState everytime the user submits
                    prefs.edit().putString("name", PRESERVED[0]).commit();
                    prefs.edit().putString("phone", PRESERVED[1]).commit();
                    prefs.edit().putString("email", PRESERVED[2]).commit();

                    beamThis = createTextRecord(send);
                    final NdefMessage finalPayload = new NdefMessage(beamThis);
                    //sendInfo.setBackgroundColor(Color.WHITE);
                    nfcAdapter.setNdefPushMessage(finalPayload, MainActivity.this);
                    Toast.makeText(context, "Now tap two phones together!", Toast.LENGTH_LONG).show();
                    name.setCursorVisible(false);
                    phone.setCursorVisible(false);
                    email.setCursorVisible(false);
                }
            }
        };
        sendInfo.setOnClickListener(listener);

    }

    //Email Validator
    //Source:
    //http://stackoverflow.com/questions/1819142/how-should-i-validate-an-e-mail-address
    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }
}
