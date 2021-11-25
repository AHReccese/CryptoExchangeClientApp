package com.example.nobintest.AppPages.Language;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nobintest.AppPages.AppActivity;
import com.example.nobintest.AppPages.Security.SecurityActivity;
import com.example.nobintest.AppPages.Security.SetPinActivity;
import com.example.nobintest.DataManegment.AppDataManager;
import com.example.nobintest.R;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    private String userLanguage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        getUserLanguage();
        setRadioGroupSetting();
    }

    private void getUserLanguage() {
        AppDataManager appDataManager = AppDataManager.getInstance(LanguageActivity.this);
        userLanguage = appDataManager.getLanguage();
    }

    private void setUserLanguage(String lang) {
        //Locale locale = new Locale(lang);
        //Locale.setDefault(locale);
        //Configuration configuration = new Configuration();
        //configuration.setLocale(locale);
        //configuration.setLayoutDirection(locale);

        //getApplicationContext().createConfigurationContext(configuration);
        //getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        AppDataManager appDataManager = AppDataManager.getInstance(LanguageActivity.this);
        appDataManager.setLanguage(lang);
        //recreate();
    }

    private void setRadioGroupSetting() {
        RadioGroup radioGroup = findViewById(R.id.language_radio_group);
        if (userLanguage.equals(AppDataManager.ENGLISH_LANGUAGE)) {
            radioGroup.check(R.id.radio_button_english);
        } else if (userLanguage.equals(AppDataManager.PERSIAN_LANGUAGE)) {
            radioGroup.check(R.id.radio_button_persian);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Intent intent;
                switch (checkedId) {
                    case R.id.radio_button_english:
                        setUserLanguage(AppDataManager.ENGLISH_LANGUAGE);
                        finish();
                        intent = new Intent(getApplication(), AppActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.radio_button_persian:
                        setUserLanguage(AppDataManager.PERSIAN_LANGUAGE);
                        finish();
                        intent = new Intent(getApplication(), AppActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(getApplication(), AppActivity.class);
        startActivity(intent);
    }
}
