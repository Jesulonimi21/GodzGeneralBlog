package com.ambgen.godzgeneralblog.adapters;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.ambgen.godzgeneralblog.R;

import java.util.ArrayList;
import java.util.List;

public class OnboardAdapter extends PagerAdapter {

    List<String> images=new ArrayList<String>();
    List<String> texts=new ArrayList<>();
    public OnboardAdapter() {
      images.add(String.valueOf(R.drawable.onboard_img_i));
    images.add(String.valueOf(R.drawable.onboard_img_ii));
    images.add(String.valueOf(R.drawable.onboard_img_iii));

    texts.add("Find the latest news around the globe");
    texts.add("Get to know more important news around you");
    texts.add("Easily start unique cnversations about the news");
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view= LayoutInflater.from(container.getContext()).inflate(R.layout.onboard_i,container,false);
        ImageView onboardImage=view.findViewById(R.id.onboard_image);
        TextView onboardText=view.findViewById(R.id.onboard_text);
        onboardImage.setImageResource(Integer.parseInt(images.get(position)));
        onboardText.setText(texts.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==((ConstraintLayout)object);
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
