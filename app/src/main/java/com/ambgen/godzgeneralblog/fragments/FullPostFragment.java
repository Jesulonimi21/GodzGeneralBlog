package com.ambgen.godzgeneralblog.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ambgen.godzgeneralblog.R;
import com.ambgen.godzgeneralblog.adapters.AllNewsAdapter;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class FullPostFragment extends Fragment {

    public FullPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_full_post, container, false);
        ImageView fullPostImage=root.findViewById(R.id.full_post_image);
        TextView fullPostTitle=root.findViewById(R.id.post_title);
        TextView fullPostDecription=root.findViewById(R.id.full_post_description);
        String imageUrl=getArguments().getString(AllNewsAdapter.IMAGURLARGKEY);
        String newsTitle=getArguments().getString(AllNewsAdapter.TITLEARGKEY);
        String newsDesription=getArguments().getString(AllNewsAdapter.DESCRIPTIONARGKEY);

        if(imageUrl!=null&&!imageUrl.isEmpty())
        Picasso.get().load(imageUrl).into(fullPostImage);
        fullPostTitle.setText(newsTitle);
        fullPostDecription.setText(Html.fromHtml(newsDesription));



        return root;
    }
}
