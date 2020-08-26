package com.ambgen.godzgeneralblog.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ambgen.godzgeneralblog.R;
import com.ambgen.godzgeneralblog.RetroService.ApiRequests;
import com.ambgen.godzgeneralblog.RetroService.ServiceBuilder;
import com.ambgen.godzgeneralblog.adapters.AllNewsAdapter;
import com.ambgen.godzgeneralblog.adapters.PromoteNewsAdapter;
import com.ambgen.godzgeneralblog.models.AllNewsModel;
import com.ambgen.godzgeneralblog.models.CategoriesModel;
import com.ambgen.godzgeneralblog.repository.NewsRepository;
import com.ambgen.godzgeneralblog.utils.Helper;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    String TAG=HomeFragment.class.getSimpleName();
    private HomeViewModel homeViewModel;
    RecyclerView allNewsRecycler;
    AllNewsAdapter allNewsAdapter;
    List<AllNewsModel> allNewsModelList=new ArrayList<>();
    ArrayList<String> fields;
    ProgressBar allNewsProgress;
    ApiRequests apiRequests;
    RecyclerView promoteRecycler;
    EditText searchEditText;
    private Snackbar snackbar;
    private NewsRepository newsRepository;
    ConstraintLayout rootLayout;
    Spinner categorySpinner;
    List<CategoriesModel> categoriesModelList;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        fields=new ArrayList<>();
        promoteRecycler=root.findViewById(R.id.promote_news_recycler);
        allNewsProgress=root.findViewById(R.id.progress_all_news);
        allNewsRecycler=root.findViewById(R.id.all_news_recycler);
        searchEditText=root.findViewById(R.id.search_field);
        categorySpinner=root.findViewById(R.id.category_spinner);

        rootLayout=root.findViewById(R.id.fragment_home_layout);

        newsRepository = new NewsRepository(getActivity());
//        newsRepository.getAllNews();

        promoteRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        promoteRecycler.setHasFixedSize(true);
        allNewsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        allNewsRecycler.setHasFixedSize(true);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    Helper.hideKeyboardFrom(getContext(),searchEditText);
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        //author,title, image, excerpt,date,jetpack_featured_media_url
        fields.add("author");
        fields.add("title");
        fields.add("date");
        fields.add("id");
        fields.add("jetpack_featured_media_url");
        fields.add("excerpt");
        fields.add("categories");
        fields.add("content");

        return root;
    }

    HomeNewsRecyclerTransferInterface homeNewsRecyclerTransferInterface;
    public interface HomeNewsRecyclerTransferInterface{
       void  updateFragmentFromActivity(HomeFragment homeFragment);
       void updateRecyclerBasedOnCategory(int id,boolean shoukdReplace,int categoryPageNumber);
       void updateRecyclerToAllPages();
       void getCategories();
    }

    public void setHomeNewsRecyclerTransferInterface(HomeNewsRecyclerTransferInterface homeNewsRecyclerTransferInterface){
        this.homeNewsRecyclerTransferInterface=homeNewsRecyclerTransferInterface;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        context.toString();
        homeNewsRecyclerTransferInterface=(HomeNewsRecyclerTransferInterface) context;

    }

    private void performSearch(){
        allNewsProgress.setVisibility(View.VISIBLE);
        String searchWord=searchEditText.getText().toString().trim();
        if(TextUtils.isEmpty(searchWord)){
            return;
        }
        apiRequests=ServiceBuilder.buildService(ApiRequests.class);
        Call<List<AllNewsModel>> searchListCall=apiRequests.searchPosts(searchWord.toLowerCase());
        searchListCall.enqueue(new Callback<List<AllNewsModel>>() {
            @Override
            public void onResponse(Call<List<AllNewsModel>> call, Response<List<AllNewsModel>> response) {
                allNewsProgress.setVisibility(View.GONE);
                List<Object> allNewsModels=new ArrayList<>();
                       allNewsModels.addAll( response.body());
                if(allNewsModels.size()==0){
                    snackbar = null;
                        snackbar =  Snackbar.make(searchEditText,"No Posts matched your search word",Snackbar.LENGTH_INDEFINITE)
                            .setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                  snackbar.show();
                    return;
                }
                allNewsAdapter=new AllNewsAdapter(getActivity(),allNewsModels,TAG);
                allNewsRecycler.setAdapter(allNewsAdapter);

            }

            @Override
            public void onFailure(Call<List<AllNewsModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Error : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                allNewsProgress.setVisibility(View.GONE);
                Log.d(TAG,t.getMessage());
            }
        });

    }

    @SuppressLint("ResourceType")
    public void inflateFragmentWithRecyclerView(RecyclerView allnewsRecyclerView, RecyclerView promoteNewsRecyclerview){

        //For promote recycler
        this.promoteRecycler=promoteNewsRecyclerview;
        promoteNewsRecyclerview.setId(3001);
        promoteNewsRecyclerview.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dpToPixels(200)));
        ConstraintLayout.LayoutParams promoteRecyclerLayoutParams=((ConstraintLayout.LayoutParams)promoteNewsRecyclerview.getLayoutParams());
        promoteRecyclerLayoutParams.topToBottom=R.id.search_field;
        //rootLayout.removeView(promoteNewsRecyclerview);
        if(promoteNewsRecyclerview.getParent()!=null){
            ((ViewGroup)promoteNewsRecyclerview.getParent()).removeView(promoteNewsRecyclerview);
        }
        rootLayout.addView(promoteNewsRecyclerview);
        //for All news recycler
        this.allNewsRecycler=allnewsRecyclerView;

        allnewsRecyclerView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dpToPixels(0)));
        ConstraintLayout.LayoutParams newsRecyclerViewLayoutParams=((ConstraintLayout.LayoutParams)allnewsRecyclerView.getLayoutParams());
        newsRecyclerViewLayoutParams.topToBottom=promoteNewsRecyclerview.getId();
        newsRecyclerViewLayoutParams.bottomToBottom=R.id.fragment_home_layout;
        if(allnewsRecyclerView.getParent()!=null){
            ((ViewGroup)allnewsRecyclerView.getParent()).removeView(allnewsRecyclerView);
        }
        rootLayout.addView(allnewsRecyclerView);




    }

    public int dpToPixels(int dp){
        return (int)(dp * getActivity().getResources().getDisplayMetrics().density + 0.5);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"was destroyed");
        rootLayout.removeView(allNewsRecycler);
        rootLayout.removeView(promoteRecycler);
    }

    @Override
    public void onResume() {
        super.onResume();
        homeNewsRecyclerTransferInterface.getCategories();
        homeNewsRecyclerTransferInterface.updateFragmentFromActivity(this);
    }
    public void hideProgressBar(){
        allNewsProgress.setVisibility(View.GONE);
    }

    public void showSnackbar(String errorMessage){
        snackbar = null;
        snackbar =  Snackbar.make(searchEditText,"error: "+errorMessage,Snackbar.LENGTH_INDEFINITE)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
        snackbar.show();
    }

    public void constructSpinner(List<CategoriesModel> categoriesList){
        Log.d(TAG,"Categories model called "+categoriesList.size());
        Log.d(TAG,"Categories model called 2 "+categoriesList.size());
         ArrayAdapter<CategoriesModel> arrayAdapter = new ArrayAdapter<CategoriesModel>(
                getContext(),android.R.layout.simple_spinner_item,categoriesList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);

                }
                else {
                    tv.setText(categoriesList.get(position).getName()+" "+categoriesList.get(position).getCount());
                    tv.setTextColor(Color.BLACK);
                }
                return tv;
            }
        };
        // categorySpinner=getView().findViewById(R.id.category_spinner);
        if(this.categorySpinner==null){
            Toast.makeText(getContext(), "Category spinner was null", Toast.LENGTH_SHORT).show();
        }
        this.categorySpinner.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             if(i==0){
                 return;
             }else if(i==1){
                 Log.d(TAG,categoriesList.get(i).getName());
                 allNewsProgress.setVisibility(View.VISIBLE);
                 homeNewsRecyclerTransferInterface.updateRecyclerToAllPages();
                 return;
             }
                Log.d(TAG,categoriesList.get(i).getName());
             allNewsProgress.setVisibility(View.VISIBLE);
             int categoryId=categoriesList.get(i).getId();
             homeNewsRecyclerTransferInterface.updateRecyclerBasedOnCategory(categoryId,true,1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void showProgressBar(){
        allNewsProgress.setVisibility(View.VISIBLE);
    }

}
