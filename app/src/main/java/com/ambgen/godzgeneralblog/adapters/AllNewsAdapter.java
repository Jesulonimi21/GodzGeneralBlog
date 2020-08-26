package com.ambgen.godzgeneralblog.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ambgen.godzgeneralblog.HomeActivity;
import com.ambgen.godzgeneralblog.R;
import com.ambgen.godzgeneralblog.models.AllNewsModel;
import com.ambgen.godzgeneralblog.repository.NewsRepository;
import com.ambgen.godzgeneralblog.ui.dashboard.BookmarksFragment;
import com.ambgen.godzgeneralblog.ui.notifications.LovedNewsFragment;
import com.ambgen.godzgeneralblog.utils.Helper;
import com.ambgen.godzgeneralblog.viewholder.UnifiedNativeAdViewHolder;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TITLEARGKEY="TITLEARGKEY";
    public static final String DESCRIPTIONARGKEY="DESCRIPTIONARGKEY";
    public static final String IMAGURLARGKEY="IMAGURLARGKEY";

    List<Object> allNews;
    int LOADER_VIEWTYPE=234;
    String callerTag;
    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;
    Activity activity;
    public AllNewsAdapter(Activity activity,List<Object> allNews,String callerTag) {

        this.allNews = allNews;
        this.callerTag=callerTag;
        this.activity=activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_all_news,parent,false);
        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                View unifiedNativeLayoutView = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.ad_unified,
                        parent, false);
                return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);
        }
        if(viewType ==LOADER_VIEWTYPE){
            Log.d("Jesulonimi"," Returned Loader View type in oncreateview");
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_news_item,parent,false);
        }
        return new AllNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int viewType=getItemViewType(position);
        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                UnifiedNativeAd nativeAd = (UnifiedNativeAd) allNews.get(position);
                populateNativeAdView(nativeAd, ((UnifiedNativeAdViewHolder) viewHolder).getAdView());
                return;
        }
    AllNewsModel allNewsModel=(AllNewsModel) allNews.get(position);

        if(position==(getItemCount()-1)&&callerTag!=LovedNewsFragment.TAG&callerTag!=BookmarksFragment.TAG){
          Log.d("Jesulonimi","Last item reachd");
          return;
        }

        Object unknownObj=allNews.get(position);
        if(allNewsModel.getTitle()==null){
            return;
        }
       AllNewsViewHolder holder=(AllNewsViewHolder)viewHolder;
        Log.d("LovedNewsFragment","in adapter  "+allNewsModel.getTitle().getRendered());
        if(!allNewsModel.getJetpack_featured_media_url().isEmpty()){
            Picasso.get().load(allNewsModel.getJetpack_featured_media_url()).into(holder.allNewsImage);
        }
        holder.newsTitle.setText(allNewsModel.getTitle().getRendered());
        String category= Helper.returnCategoryName(allNewsModel.getCategories()[0]);
        holder.newsCategory.setText(category);
        Log.d(AllNewsAdapter.class.getSimpleName(),allNewsModel.getDate());
        String monthInteger=allNewsModel.getDate().substring(5,7);
        String dayInteger=allNewsModel.getDate().substring(8,10);
        Log.d(AllNewsAdapter.class.getSimpleName(),"Month : "+monthInteger+" Day "+dayInteger);
        String month=Helper.getMonth(Integer.parseInt(monthInteger));
        holder.dateTextView.setText(month+" "+dayInteger);
        if(!allNewsModel.isLoved()) {
            holder.favouriteImage.setImageResource(R.drawable.ic_favourite);
        }else{
            holder.favouriteImage.setImageResource(R.drawable.ic_fav_painted);
        }
        Log.d(AllNewsAdapter.class.getSimpleName(),"Actual value: "+allNewsModel.isBookMarked());
        if(!allNewsModel.isBookMarked()) {
            holder.bookMarkImage.setImageResource(R.drawable.ic_star_border_black_24dp);
        }else{
            holder.bookMarkImage.setImageResource(R.drawable.ic_star_black_24dp);

        }
        holder.bookMarkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsRepository newsRepository=new NewsRepository(view.getContext());
                if(!allNewsModel.isBookMarked()){
                    Log.d(AllNewsAdapter.class.getSimpleName(),"Prior value updated: "+allNewsModel.isBookMarked());
                    allNewsModel.setBookMarked(true);
                    newsRepository.update(allNewsModel);
                    Log.d(AllNewsAdapter.class.getSimpleName(),"SAVED: "+allNewsModel.isBookMarked());

                    holder.bookMarkImage.setImageResource(R.drawable.ic_star_black_24dp);
                }else{
                    Log.d(AllNewsAdapter.class.getSimpleName(),"Prior Value Deleted: "+allNewsModel.isBookMarked());
                    allNewsModel.setBookMarked(false);
                    newsRepository.update(allNewsModel);
                    Log.d(AllNewsAdapter.class.getSimpleName(),"Deleted: "+allNewsModel.isBookMarked());
                    holder.bookMarkImage.setImageResource(R.drawable.ic_star_border_black_24dp);
                    if(AllNewsAdapter.this.callerTag.equals(BookmarksFragment.TAG)){
                        allNews.remove(allNewsModel);
                        AllNewsAdapter.this.notifyDataSetChanged();
                    }
                }
            }
        });
        holder.favouriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsRepository newsRepository=new NewsRepository(view.getContext());
                if(!allNewsModel.isLoved()){
                    allNewsModel.setLoved(true);
                    newsRepository.update(allNewsModel);
                    holder.favouriteImage.setImageResource(R.drawable.ic_fav_painted);

                    Log.d(AllNewsAdapter.class.getSimpleName(),"Deleted: "+allNewsModel.getTitle().getRendered());
                }else{
                    allNewsModel.setLoved(false);
                    newsRepository.update(allNewsModel);
                    holder.favouriteImage.setImageResource(R.drawable.ic_favourite);
                    if(AllNewsAdapter.this.callerTag.equals(LovedNewsFragment.TAG)){
                        allNews.remove(allNewsModel);
                        AllNewsAdapter.this.notifyDataSetChanged();
                    }
                }

            }
        });

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
    public int getItemViewType(int position) {
        Object recyclerViewItem = allNews.get(position);
        if (recyclerViewItem instanceof UnifiedNativeAd) {
            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        }
        if(position==(getItemCount()-1)&callerTag.equals("UpdateTag")){
            Log.d("Jesulonimi"," Returned Loader View type");
            updateListInterface.fetchMoreItems();
            return LOADER_VIEWTYPE;
        }

        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
//        return allNews.size();
        return allNews.size();
    }


    public class  AllNewsViewHolder extends RecyclerView.ViewHolder{

        ImageView allNewsImage;
        TextView newsTitle;
        ImageView favouriteImage;
        TextView newsCategory;
        TextView dateTextView;
        ImageView bookMarkImage;
        public AllNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            allNewsImage=itemView.findViewById(R.id.all_news_image);
            newsTitle=itemView.findViewById(R.id.all_news_title);
            favouriteImage=itemView.findViewById(R.id.all_news_favourite);
            newsCategory=itemView.findViewById(R.id.all_news_category);
            dateTextView=itemView.findViewById(R.id.all_news_date);
            bookMarkImage=itemView.findViewById(R.id.star_post);
        }
    }
    public int dpToPixels(int dp, Context context){
        return (int)(dp*context.getResources().getDisplayMetrics().density+0.5);
    }

    public void updateList(List<Object> allNewsModels,  boolean shouldReplace){
        Log.d("jesulonimi","enterred update list");
        if((this.allNews.size()-1)!=-1){
            this.allNews.remove(this.allNews.size()-1);
        }

        int formerListSize=this.allNews.size();
        if(!shouldReplace){
            this.allNews.addAll(allNewsModels);
            notifyItemInserted(formerListSize);
        }else{
            this.allNews.clear();
            this.allNews.addAll(allNewsModels);
            notifyDataSetChanged();
        }


    }

    public void setUpdateListInterface(updateListInterface updateListInterface){
        this.updateListInterface=updateListInterface;
    }
    updateListInterface updateListInterface;
    public interface  updateListInterface{
         void fetchMoreItems();
    }

    private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }

}
