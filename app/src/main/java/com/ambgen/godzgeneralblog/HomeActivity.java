package com.ambgen.godzgeneralblog;

import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ambgen.godzgeneralblog.RetroService.ApiRequests;
import com.ambgen.godzgeneralblog.RetroService.ServiceBuilder;
import com.ambgen.godzgeneralblog.adapters.AllNewsAdapter;
import com.ambgen.godzgeneralblog.adapters.PromoteNewsAdapter;
import com.ambgen.godzgeneralblog.models.AllNewsModel;
import com.ambgen.godzgeneralblog.models.CategoriesModel;
import com.ambgen.godzgeneralblog.repository.NewsRepository;
import com.ambgen.godzgeneralblog.ui.home.HomeFragment;
import com.androidstudy.networkmanager.Monitor;
import com.androidstudy.networkmanager.Tovuti;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements  HomeFragment.HomeNewsRecyclerTransferInterface,
        NewsRepository.listOfNewsInterface,
        AllNewsAdapter.updateListInterface{
    RecyclerView allNewsRecycler;
    AllNewsAdapter allNewsAdapter;
    int failureCount=0;
    boolean officialShouldReplaace=true;
    List<AllNewsModel> allNewsModelList=new ArrayList<>();
    ArrayList<String> fields;
    ProgressBar allNewsProgress;
    ApiRequests apiRequests;
    RecyclerView promoteRecycler;
    EditText searchEditText;
    private Snackbar snackbar;
    private NewsRepository newsRepository;
    String TAG=HomeActivity.class.getSimpleName();
    private Call<List<AllNewsModel>> allPostsCall;
    boolean isFirstTimeLoadedForAllNews =true;
    boolean isFirstTimeLoadedForPromoteNews=true;

    private Call<List<AllNewsModel>> promotedNewsCall;
    int pageNumber=1;
    int categoryPage=-1;
    int categoryId=-1;
    Tovuti tovuti;
    List<CategoriesModel> categoriesModelList;
    boolean isCategoryPage;
    public static final int NUMBER_OF_ADS = 5;
    private AdLoader adLoader;
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();



        newsRepository=new NewsRepository(this);
        newsRepository.setListOfNewsInterface(this);
        allNewsRecycler=new RecyclerView(this);
        promoteRecycler=new RecyclerView(this);
        promoteRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        promoteRecycler.setHasFixedSize(true);
        promoteRecycler.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        allNewsRecycler.setLayoutManager(new LinearLayoutManager(this));
        allNewsRecycler.setHasFixedSize(true);
        allNewsRecycler.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));

        fields=new ArrayList<>();
        fields.add("author");
        fields.add("title");
        fields.add("date");
        fields.add("id");
        fields.add("jetpack_featured_media_url");
        fields.add("excerpt");
        fields.add("categories");
        fields.add("content");
        apiRequests= ServiceBuilder.buildService(ApiRequests.class);


        allPostsCall = apiRequests.getNewsByPage("1",fields);
        promotedNewsCall = apiRequests.promotedCategory("576602837",fields);

        Tovuti.from(this).monitor(new Monitor.ConnectivityListener(){
            @Override
            public void onConnectivityChanged(int connectionType, boolean isConnected, boolean isFast){
                String type, speed;
                if (isConnected) {
                    switch (connectionType) {
                        case -1:
                            type = "Any";

                            break;
                        case ConnectivityManager.TYPE_WIFI:
                            type = "Wifi";
                            break;
                        case ConnectivityManager.TYPE_MOBILE:
                            type = "Mobile";
                            break;
                        default:
                            type = "Unknown";
                            break;
                    }


                    if (isFast)
                        speed = "Fast";
                    else if (type.equals("Unknown"))
                        speed = "N/A";
                    else
                        speed = "Slow";
                } else {
                    type = "None";
                    speed = "N/A";
                }
                logToConsole("type :"+type);
                logToConsole("speed: "+speed);
                if(type.equals("none")||speed.equals("N/A")& isFirstTimeLoadedForAllNews){
                   showNoConnectionDialog("You are not presently connected to the internet, would you like to view offline news? ");
                    return;
                }if(isFirstTimeLoadedForAllNews){
                    loadNewsFromInternet(allPostsCall.clone(),false);
                }
                if(isFirstTimeLoadedForPromoteNews){
                    loadPromotedNewsFromInternet();
                }


            }
        });
//        if(this.categoriesModelList==null){
//            getCategories();
//        }

    }

    HomeFragment homeFragment;
    public void updateFragmentFromActivity(HomeFragment homeFragment){
        this.homeFragment=homeFragment;
        homeFragment.inflateFragmentWithRecyclerView(allNewsRecycler,promoteRecycler);
    }



    @Override
    protected void onResume() {
        super.onResume();




    }

    public void logToConsole(String logStatement){
        Log.d(TAG,logStatement);
    }

    public void showDialog(String message, DialogInterface.OnClickListener dialogInterfaceListener ){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("Yes",dialogInterfaceListener).setNegativeButton
                ("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
//                homeFragment.showProgressBar();
//                loadNewsFromInternet(allPostsCall.clone(),officialShouldReplaace);
            }
        });



        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public void updateListWithAdapter(List<AllNewsModel> allNews) {
        homeFragment.hideProgressBar();
        List<Object> modelObjects=new ArrayList<>();
        modelObjects.addAll(allNews);
        allNewsAdapter=new AllNewsAdapter(this,modelObjects,TAG);
        allNewsRecycler.setAdapter(allNewsAdapter);

        List<AllNewsModel> promoteNewsModels=new ArrayList<>();
        int index=0;
        for(AllNewsModel allNewsModel: allNews){
            Log.d(TAG,"Category is not 2 "+index++);
            for(long i:allNewsModel.getCategories()){
                Log.d(TAG,"Category is not 2 "+i);
                if(i==576602837){
                    promoteNewsModels.add(allNewsModel);
                    Log.d(TAG,"Category is 2");
                }
            }
        }
        PromoteNewsAdapter promoteNewsAdapter=new PromoteNewsAdapter(this,promoteNewsModels);

        promoteRecycler.setAdapter(promoteNewsAdapter);



    }

    public void showNoConnectionDialog(String message){
        ++failureCount;
        if(failureCount>=4){
            return;
        }
        DialogInterface.OnClickListener dialogListener=new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                newsRepository.getAllNews();
            }
        };
        showDialog(message,dialogListener);
    }

    public void loadNewsFromInternet(Call<List<AllNewsModel>> allPostsCall,boolean shouldReplace) {

        if(isFirstTimeLoadedForAllNews){  homeFragment.showProgressBar();}
        categoryId=-1;
        categoryPage=-1;
        isCategoryPage=false;
        allPostsCall.enqueue(new Callback<List<AllNewsModel>>() {
            @Override
            public void onResponse(Call<List<AllNewsModel>> call, Response<List<AllNewsModel>> response) {
                isFirstTimeLoadedForAllNews =false;
                List<Object> allNewsModels=new ArrayList<>();
                if(response.body()==null){
                    return;
                }
                       allNewsModels.addAll(response.body()) ;
                if(allNewsModels==null){
                    return;
                }
        Log.d("Jesulonimi","news successfully loaded");
                newsRepository.insert(response.body());
               List<Object> passedNewsMODEL=new ArrayList<>();
               passedNewsMODEL.addAll(allNewsModels);
//               passedNewsMODEL.add(new AllNewsModel());
               if(pageNumber==1){
//                   allNewsAdapter=new AllNewsAdapter(passedNewsMODEL,"UpdateTag");
//                   allNewsAdapter.setUpdateListInterface(HomeActivity.this);
//                   allNewsRecycler.setAdapter(allNewsAdapter);

                   loadNativeAds(passedNewsMODEL,1,false);
               }else if(pageNumber==-2){
                   pageNumber=1;
                   if(allNewsAdapter==null){
                       loadNewsFromInternet(allPostsCall.clone(),officialShouldReplaace);
                        return;
                   }
                       allNewsAdapter.setUpdateListInterface(HomeActivity.this);
                       Log.d(TAG,"trying to update list");
                       allNewsAdapter.updateList(passedNewsMODEL,shouldReplace);
                   homeFragment.hideProgressBar();
               }else{
                   if(allNewsAdapter==null){
                       loadNewsFromInternet(allPostsCall.clone(),officialShouldReplaace);
                       return;
                   }
                   allNewsAdapter.setUpdateListInterface(HomeActivity.this);
                   Log.d(TAG,"trying to update list");
                   loadNativeAds(passedNewsMODEL,pageNumber,shouldReplace);
                   //allNewsAdapter.updateList(passedNewsMODEL,shouldReplace);
               }


              //  homeFragment.hideProgressBar();
                ++pageNumber;


            }

            @Override
            public void onFailure(Call<List<AllNewsModel>> call, Throwable t) {
                DialogInterface.OnClickListener dialogListener=new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        newsRepository.getAllNews();

                    }
                };
                Log.d("Jesulonimi","news failed to load");

                showNoConnectionDialog("For some reason, news could not be loaded, will you like to view online news ? ");
                Toast.makeText(HomeActivity.this, "Error : "+t.getMessage(), Toast.LENGTH_SHORT).show();

                Log.d(TAG,t.getMessage());
            }
        });
    }

    public void loadPromotedNewsFromInternet(){
        promotedNewsCall.clone().enqueue(new Callback<List<AllNewsModel>>() {
            @Override
            public void onResponse(Call<List<AllNewsModel>> call, Response<List<AllNewsModel>> response) {
                PromoteNewsAdapter promoteNewsAdapter=new PromoteNewsAdapter(HomeActivity.this,response.body());
                promoteRecycler.setAdapter(promoteNewsAdapter);
                newsRepository.insert(response.body());
               isFirstTimeLoadedForPromoteNews=false;
            }

            @Override
            public void onFailure(Call<List<AllNewsModel>> call, Throwable t) {
                showNoConnectionDialog("For some reason, news could not be loaded, will you like to view online news ? ");
                Toast.makeText(HomeActivity.this, "Error : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG,t.getMessage());
            }
        });
    }


    @Override
    public void fetchMoreItems() {
        Log.d(TAG,"trying to update list in fetch more");
        if(isCategoryPage&&categoryPage!=-1&&categoryId!=-1){
            updateRecyclerBasedOnCategory(categoryId,false,++categoryPage);
            Log.d("Jesulonimi","wrong post call isCategoryPage : "+isCategoryPage+" categoyId : "+categoryId );
            return;
        }
        Log.d("Jesulonimi","other post call");
        allPostsCall = apiRequests.getNewsByPage(String.valueOf(pageNumber),fields);
        officialShouldReplaace=false;
        loadNewsFromInternet(allPostsCall,officialShouldReplaace);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Tovuti.from(this).stop();
    }

    public void getCategories(){
//        if(this.categoriesModelList!=null){
//            homeFragment.constructSpinner(this.categoriesModelList);
//            return;
//        }
        ArrayList<String> categoryFields=new ArrayList<>();
        categoryFields.add("count");
        categoryFields.add("id");
        categoryFields.add("name");
        Call<List<CategoriesModel>> categoriesCall=apiRequests.getAllCategories(categoryFields);

        categoriesCall.enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                List<CategoriesModel> categoriesModelList=new ArrayList<>();
                int totalCount=0;
                for(CategoriesModel i: response.body()){
                    totalCount+=i.getCount();
                };

            categoriesModelList.add(new CategoriesModel(9006,0,"Categories"));
                categoriesModelList.add(new CategoriesModel(9000,totalCount,"All"));
                HomeActivity.this.categoriesModelList=new ArrayList<>();
                       HomeActivity.this.categoriesModelList.addAll(categoriesModelList) ;
                categoriesModelList.addAll(response.body());
                homeFragment.constructSpinner(categoriesModelList);
            }

            @Override
            public void onFailure(Call<List<CategoriesModel>> call, Throwable t) {

            }
        });

    }
    @Override
    public void updateRecyclerBasedOnCategory(int id,boolean shouldlReplace, int categoryPageNumber) {
        isCategoryPage=true;
        categoryPage=categoryPageNumber;
        categoryId=id;
        Log.d("Jesulonimi"," id : "+id+" should replace "+shouldlReplace+" CategoryPage "+categoryPageNumber);
        Call<List<AllNewsModel>> promotedNewsCall=apiRequests.getNewsByCategory(String.valueOf(id),fields,String.valueOf(categoryPageNumber));
        promotedNewsCall.enqueue(new Callback<List<AllNewsModel>>() {
            @Override
            public void onResponse(Call<List<AllNewsModel>> call, Response<List<AllNewsModel>> response) {
                Log.d(TAG,"Successfully retrieved categoeies");
                if(response.body()==null){return;}
                homeFragment.hideProgressBar();
                List<Object> categoryNewsModels=new ArrayList<>();
                categoryNewsModels.addAll(response.body());
                categoryNewsModels.add(new AllNewsModel());

                allNewsAdapter.updateList(categoryNewsModels,shouldlReplace);
                newsRepository.insert(response.body());
                isFirstTimeLoadedForAllNews =false;
            }

            @Override
            public void onFailure(Call<List<AllNewsModel>> call, Throwable t) {
                homeFragment.hideProgressBar();
                showNoConnectionDialog("For some reason, news could not be loaded, will you like to view online news ? ");
                Toast.makeText(HomeActivity.this, "Error : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG,t.getMessage());
            }
        });
    }

    @Override
    public void updateRecyclerToAllPages() {
        pageNumber=-2;
        categoryPage=-1;
        categoryId=-1;
        isCategoryPage=false;
        allPostsCall = apiRequests.getNewsByPage("1",fields);
        officialShouldReplaace=true;
        loadNewsFromInternet(allPostsCall,true);
        Log.d("Jesulonimi","All pages");
    }

    private void insertAdsInMenuItems(List<Object> myList) {
        if (mNativeAds.size() <= 0) {
            return;
        }
        Log.d("nimiBoss","in insert menu items: "+mNativeAds.size());
        int offset = (myList.size() / mNativeAds.size()) + 1;
        int index = 0;
        for (UnifiedNativeAd ad: mNativeAds) {
            myList.add(index, ad);
            index = index + offset;
        }

    }
    private void loadNativeAds(List<Object> myList, int pageNumber,boolean shouldReplace) {
        adLoader=null;
        mNativeAds.clear();
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.ad_unit_id));
        adLoader = builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // A native ad loaded successfully, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        Log.d("nimiBoss","native add size: "+mNativeAds.size());
                       if(mNativeAds.size()<3){
                           mNativeAds.add(unifiedNativeAd);
                       }else if(mNativeAds.size()==3){
                           mNativeAds.add(unifiedNativeAd);
                           insertAdsInMenuItems(myList);
                           myList.add(new AllNewsModel());
                           if(pageNumber!=1){
                               allNewsAdapter.setUpdateListInterface(HomeActivity.this);
                               allNewsAdapter.updateList(myList,shouldReplace);

                               return;
                           }
                           allNewsAdapter=new AllNewsAdapter(HomeActivity.this,myList,"UpdateTag");
                           allNewsAdapter.setUpdateListInterface(HomeActivity.this);
                           allNewsRecycler.setAdapter(allNewsAdapter);
                           homeFragment.hideProgressBar();
                           adLoader=null;
                       }

                        if (!adLoader.isLoading()) {
                            if(mNativeAds.size()>3){
                                return;
                            }
                            insertAdsInMenuItems(myList);
                            myList.add(new AllNewsModel());
                            if(pageNumber!=1){
                                allNewsAdapter.setUpdateListInterface(HomeActivity.this);
                                allNewsAdapter.updateList(myList,shouldReplace);
                                return;
                            }
                            allNewsAdapter=new AllNewsAdapter(HomeActivity.this,myList,"UpdateTag");
                            allNewsAdapter.setUpdateListInterface(HomeActivity.this);
                            allNewsRecycler.setAdapter(allNewsAdapter);
                            homeFragment.hideProgressBar();
                        }
                    }
                }).withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // A native ad failed to load, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                                + " load another.");
                        if (!adLoader.isLoading()) {
                            Log.d("nimiBoss","on ads Failed native add size: "+mNativeAds.size());
                            insertAdsInMenuItems(myList);
                            myList.add(new AllNewsModel());

                            if(pageNumber!=1){
                                allNewsAdapter.setUpdateListInterface(HomeActivity.this);
                                allNewsAdapter.updateList(myList,shouldReplace);
                                homeFragment.hideProgressBar();
                                return;
                            }
                            allNewsAdapter=new AllNewsAdapter(HomeActivity.this,myList,"UpdateTag");
                            allNewsAdapter.setUpdateListInterface(HomeActivity.this);
                            allNewsRecycler.setAdapter(allNewsAdapter);
                            homeFragment.hideProgressBar();
                        }
                    }
                }).build();

        // Load the Native Express ad.
        adLoader.loadAds(new AdRequest.Builder().build(), 3);
    }

    @NonNull
    @Override
    public String toString() {
        Log.d("Jesulonimi","I am home activity");
        return super.toString();
    }
}
