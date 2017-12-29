package com.example.howldevelopment.bcofficial.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.howldevelopment.bcofficial.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wang.avi.AVLoadingIndicatorView;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    private Button btnQRCode;
    private Button btnRegister;
    private EditText etStudentID;
    private EditText etStudentFirstName;
    private EditText etStudentLastName;
    private IntentIntegrator qrScan;
    private AVLoadingIndicatorView avLoadingIndicatorView;


    private static final String QRCODE = "DB83685BB93EBF32EF74129378FA1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Hide the action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        btnQRCode = (Button) findViewById(R.id.btnQRCode);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        etStudentID  = (EditText) findViewById(R.id.etRegisterStudentID);
        etStudentFirstName = (EditText) findViewById(R.id.etRegisterStudentFirstName);
        etStudentLastName  = (EditText) findViewById(R.id.etRegisterStudentLastName);
        //Loader Icon
        avLoadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.AVLoadingIndicatorView2);
        avLoadingIndicatorView.hide();

        // Make the notification bar transparent
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        ViewCompat.setBackgroundTintList(etStudentID, ColorStateList.valueOf(Color.WHITE));
        ViewCompat.setBackgroundTintList(etStudentFirstName, ColorStateList.valueOf(Color.WHITE));
        ViewCompat.setBackgroundTintList(etStudentLastName, ColorStateList.valueOf(Color.WHITE));

        btnRegister.setText("Scan QR Code");
        btnRegister.setEnabled(false);

        btnQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
            }
        });

        qrScan = new IntentIntegrator(this);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avLoadingIndicatorView.smoothToShow();
                
            }
        });



        // QR CODE
        //
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_SHORT).show();
                btnQRCode.setText("Failed");
                btnRegister.setText("Scan QR Code");
                btnRegister.setEnabled(false);
            } else {
                //if qr contains data
                if (result.getContents().equals(QRCODE)) {
                    btnQRCode.setText("Success");
                    btnRegister.setText("Register");
                    btnRegister.setEnabled(true);
                    btnQRCode.setEnabled(false);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
