package com.ambgen.godzgeneralblog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ambgen.godzgeneralblog.adapters.OnboardAdapter;
import com.ambgen.godzgeneralblog.constants.ConstantValues;

import java.util.ArrayList;
import java.util.List;

public class OnboardActivity extends AppCompatActivity {
    ViewPager viewPager;
    List<TextView> mDots;
    LinearLayout linearLayout;
    Button continueButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);
        viewPager=findViewById(R.id.view_pager);
        linearLayout=findViewById(R.id.three_dots);
        continueButton=findViewById(R.id.onboard_continue);
        OnboardAdapter onboardAdapter=new OnboardAdapter();
        viewPager.setAdapter(onboardAdapter);
        addDotsIndicator(0);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addDotsIndicator(position);
                if(position<=1){
                    continueButton.setVisibility(View.GONE);
                }else{
                    continueButton.setVisibility(View.VISIBLE);
                    animateContinueButton(continueButton);
                    continueButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreferences sharedPreferences=getSharedPreferences(ConstantValues.sharedPreferencesName,MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putBoolean(ConstantValues.FIRSTTIME,false);
                            editor.commit();
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    public void editFirstTime(){
        SharedPreferences sharedPreferences=getSharedPreferences(ConstantValues.sharedPreferencesName,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(ConstantValues.FIRSTTIME,false);
    }
    public void addDotsIndicator(int pageNum) {
        if(this!=null){
            mDots = new ArrayList<TextView>();
            linearLayout.removeAllViews();
            for (int i=0; i<3;i++) {

                mDots.add(new TextView(this));
                mDots.get(i).setText(Html.fromHtml("&#8226"));
                float textSize= 36.0F;
                mDots.get(i).setTextSize(textSize);
                mDots.get(i).setTextColor(getResources().getColor(R.color.colorGrey));
                linearLayout.addView(mDots.get(i));
            }
            if (mDots.size() > 0) {
                mDots.get(pageNum).setTextColor(getResources().getColor(R.color.onboardColorBlue));


            }
        }
    }


    public void animateContinueButton(Button button){
        ObjectAnimator buttonAnimator=ObjectAnimator.ofFloat(button,"translationY",200,0);
        buttonAnimator.setDuration(500);
        buttonAnimator.start();
    }

}
