package com.ambgen.godzgeneralblog.ui.notifications;

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

public class LovedNewsFragment extends Fragment  implements NewsRepository.listOfNewsInterface {

    private NotificationsViewModel notificationsViewModel;
    RecyclerView lovedNewsRecycler;
    NewsRepository newsRepository;
    public static final String TAG= LovedNewsFragment.class.getSimpleName();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_loved_news, container, false);
        lovedNewsRecycler=root.findViewById(R.id.loved_news_recycler);
        lovedNewsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        lovedNewsRecycler.setHasFixedSize(true);
        newsRepository = new NewsRepository(getActivity());
        newsRepository.setListOfNewsInterface(this);
        newsRepository.getAllNews();
        return root;
    }
    @Override
    public void updateListWithAdapter(List<AllNewsModel> allNews) {
        Log.d(TAG,"was calld");
//          newsRepository.insert(response.body());
        List<Object> modelObjects=new ArrayList<>();
        modelObjects.addAll(allNews);
        AllNewsAdapter allNewsAdapter=new AllNewsAdapter(getActivity(),modelObjects,TAG);
        lovedNewsRecycler.setAdapter(allNewsAdapter);
    }

}
