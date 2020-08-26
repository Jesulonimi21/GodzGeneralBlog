package com.ambgen.godzgeneralblog.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ambgen.godzgeneralblog.R;
import com.ambgen.godzgeneralblog.models.AllNewsModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.ambgen.godzgeneralblog.adapters.AllNewsAdapter.DESCRIPTIONARGKEY;
import static com.ambgen.godzgeneralblog.adapters.AllNewsAdapter.IMAGURLARGKEY;
import static com.ambgen.godzgeneralblog.adapters.AllNewsAdapter.TITLEARGKEY;

public class PromoteNewsAdapter extends RecyclerView.Adapter<PromoteNewsAdapter.AllNewsViewHolder> {


    List<AllNewsModel> allNews;
    Activity activity;
    public PromoteNewsAdapter(Activity activity,
                              List<AllNewsModel> allNews) {
        this.activity=activity;
        this.allNews = allNews;
    }

    @NonNull
    @Override
    public AllNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.promote_items_layout,parent,false);
        return new AllNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllNewsViewHolder holder, int position) {
    AllNewsModel allNewsModel=allNews.get(position);
        if(!allNewsModel.getJetpack_featured_media_url().isEmpty()){
            Picasso.get().load(allNewsModel.getJetpack_featured_media_url()).into(holder.allNewsImage);
        }

        holder.newsTitle.setText(allNewsModel.getTitle().getRendered());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString(TITLEARGKEY,allNewsModel.getTitle().getRendered());
                bundle.putString(DESCRIPTIONARGKEY,allNewsModel.getContent().getRendered());
                bundle.putString(IMAGURLARGKEY,allNewsModel.getJetpack_featured_media_url());
                Navigation.findNavController(activity,R.id.nav_host_fragment_i).navigate(R.id.fullPostFragment,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
//        return allNews.size();
        return allNews.size();
    }


    public class  AllNewsViewHolder extends RecyclerView.ViewHolder{

        ImageView allNewsImage;
        TextView newsTitle;
        public AllNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            allNewsImage=itemView.findViewById(R.id.news_image);
            newsTitle=itemView.findViewById(R.id.news_title);
        }
    }
    public int dpToPixels(int dp, Context context){
        return (int)(dp*context.getResources().getDisplayMetrics().density+0.5);
    }

}
