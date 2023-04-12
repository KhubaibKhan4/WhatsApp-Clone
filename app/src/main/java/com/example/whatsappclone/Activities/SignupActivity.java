package com.example.whatsappclone.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.whatsappclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;

public class SignupActivity extends AppCompatActivity {
    CountryCodePicker countryCodePicker;
    EditText edtPhoneNo;
    Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);
        edtPhoneNo = (EditText) findViewById(R.id.edt_mobile_no);
        countryCodePicker.registerCarrierNumberEditText(edtPhoneNo);

        sendBtn = (Button) findViewById(R.id.sendBtn);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, OTPActivity.class);
                intent.putExtra("phone_no", countryCodePicker.getFullNumberWithPlus().replace(" ", ""));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(SignupActivity.this,MainActivity.class));
        }
    }
}