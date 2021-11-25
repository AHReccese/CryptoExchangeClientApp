package com.example.nobintest.AppPages.Security;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.nobintest.DataManegment.AppDataManager;
import com.example.nobintest.R;

public class SecurityActivity extends AppCompatActivity {

    private String userSecurityState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        getUserSecuritySate();
        setRadioGroupSetting();
    }


    private void getUserSecuritySate() {
        AppDataManager appDataManager = AppDataManager.getInstance(SecurityActivity.this);
        userSecurityState = appDataManager.getSecurityState();
    }

    private void setRadioGroupSetting() {
        RadioGroup radioGroup = findViewById(R.id.security_radio_group);
        if (userSecurityState.equals(AppDataManager.SECURITY_STATE_PIN)) {
            radioGroup.check(R.id.radio_button_pin);
        } else if (userSecurityState.equals(AppDataManager.SECURITY_STATE_FINGERPRINT)) {
            radioGroup.check(R.id.radio_button_fingerprint);
        } else {
            radioGroup.check(R.id.radio_button_none);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_none:
                        finish();
                        setUserSecurityState(AppDataManager.SECURITY_STATE_NON);
                        break;
                    case R.id.radio_button_pin:
                        setUserSecurityState(AppDataManager.SECURITY_STATE_PIN);
                        startActivity(new Intent(SecurityActivity.this, SetPinActivity.class));
                        break;
                    case R.id.radio_button_fingerprint:
                        handelFingerPrint();
                }
            }
        });
    }

    private void setUserSecurityState(String state) {
        AppDataManager appDataManager = AppDataManager.getInstance(SecurityActivity.this);
        appDataManager.setSecurityState(state);
    }

    private void handelFingerPrint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                Toast.makeText(this, R.string.no_fingerprint_scanner, Toast.LENGTH_LONG).show();
            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_BIOMETRIC)
                != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.fingerprint_permission_not_granted, Toast.LENGTH_LONG).show();
            } else if (!keyguardManager.isKeyguardSecure()) {
                Toast.makeText(this, R.string.add_lock_to_your_phone, Toast.LENGTH_LONG).show();
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                Toast.makeText(this, R.string.add_fingerprint_to_your_phone, Toast.LENGTH_LONG).show();
            } else {
                setUserSecurityState(AppDataManager.SECURITY_STATE_FINGERPRINT);
                startActivity(new Intent(SecurityActivity.this, SetPinActivity.class));
            }
        } else {
            Toast.makeText(this, R.string.low_api_level, Toast.LENGTH_LONG).show();
        }
    }
}
