package com.ambgen.godzgeneralblog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.ambgen.godzgeneralblog.constants.ConstantValues;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences=getSharedPreferences(ConstantValues.sharedPreferencesName,MODE_PRIVATE);
        boolean firstTime=sharedPreferences.getBoolean(ConstantValues.FIRSTTIME,true);
        if(firstTime){
            Intent intent=new Intent(this,OnboardActivity.class);
            startActivity(intent);
//            CustomIntent.customType(SplashActivity.this,"fadein-to-fadeout");
            return;
        }else{
            Intent intent=new Intent(this,HomeActivity.class);
            Log.d("Jesulonimi","Back to main actiivity");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }

    }


}
