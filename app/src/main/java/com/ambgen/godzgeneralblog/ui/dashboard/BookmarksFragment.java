package com.ambgen.godzgeneralblog.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ambgen.godzgeneralblog.R;
import com.ambgen.godzgeneralblog.adapters.AllNewsAdapter;
import com.ambgen.godzgeneralblog.models.AllNewsModel;
import com.ambgen.godzgeneralblog.repository.NewsRepository;

import java.util.ArrayList;
import java.util.List;

public class BookmarksFragment extends Fragment implements NewsRepository.listOfNewsInterface {

    private DashboardViewModel dashboardViewModel;

    RecyclerView bookmarkedNewsecycler;
    NewsRepository newsRepository;
    public static final String TAG=BookmarksFragment.class.getSimpleName();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        bookmarkedNewsecycler=root.findViewById(R.id.bookmarks_news_recycler);
        bookmarkedNewsecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookmarkedNewsecycler.setHasFixedSize(true);
        newsRepository = new NewsRepository(getActivity());
        newsRepository.setListOfNewsInterface(this);
        newsRepository.getAllNews();
        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void updateListWithAdapter(List<AllNewsModel> allNews) {
        Log.d(TAG,"was calld");


//          newsRepository.insert(response.body());
        List<Object> modelObjects=new ArrayList<>();
        modelObjects.addAll(allNews);
        AllNewsAdapter allNewsAdapter=new AllNewsAdapter(getActivity(),modelObjects,TAG);
        bookmarkedNewsecycler.setAdapter(allNewsAdapter);
    }
}
