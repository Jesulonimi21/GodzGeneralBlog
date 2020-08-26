package com.ambgen.godzgeneralblog.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ambgen.godzgeneralblog.adapters.AllNewsAdapter;
import com.ambgen.godzgeneralblog.daos.NewsDao;
import com.ambgen.godzgeneralblog.database.NewsDatabase;
import com.ambgen.godzgeneralblog.models.AllNewsModel;
import com.ambgen.godzgeneralblog.ui.dashboard.BookmarksFragment;
import com.ambgen.godzgeneralblog.ui.notifications.LovedNewsFragment;

import java.util.ArrayList;
import java.util.List;

public class NewsRepository {
    Context context;
    NewsDao newsDao;
    NewsDatabase newsDatabase;
    List<AllNewsModel> allGoals;
    boolean isLovedNewsFrag=false;
    boolean isBoomarksFrag=false;

    public NewsRepository(Context context){
        this.context=context;
        newsDatabase =NewsDatabase.getAppDatabase(context.getApplicationContext());
        newsDao = newsDatabase.newsDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                allGoals= newsDao.getAllNews();
            }
        }).start();

    }

    public void update(AllNewsModel newsModel){
        Log.d("AllNewsAdapter","newsrep "+newsModel.getTitle().getRendered());
    new updateNewsAsyncTask().execute(newsModel);
    }

    public void insert(AllNewsModel newsModel){
    new InsertNewsAsyncTask().execute(newsModel);
    }

    public void insert(List<AllNewsModel> newsModels){
        new InsertListOfNewsAsyncTask().execute(newsModels);
    }

    public void delete(AllNewsModel newsModel){
        new deleteNewsAsyncTask().execute(newsModel);
    }

    public void get(int position){
        new getSingleNews().execute(position);
    }

    public void getAllNews(){
       new getAllNewsAsyncTask().execute();
    }


        listOfNewsInterface listOfNewsInterface;
    public  interface listOfNewsInterface {
            void updateListWithAdapter(List<AllNewsModel> allNews);
        }


        public void setListOfNewsInterface(listOfNewsInterface activity){
            isLovedNewsFrag=activity instanceof LovedNewsFragment;
            isBoomarksFrag=activity instanceof BookmarksFragment;
            listOfNewsInterface=activity;
        }
    public class InsertNewsAsyncTask extends AsyncTask<AllNewsModel,Void,Void>{

        @Override
        protected Void doInBackground(AllNewsModel... newsModels) {
            newsDao.insert(newsModels[0]);

            return null;
        }
    }

    public class InsertListOfNewsAsyncTask extends AsyncTask<List<AllNewsModel>,Void,Void>{

        @Override
        protected Void doInBackground(List<AllNewsModel>... newsModels) {
            for(AllNewsModel newsModel: newsModels[0]){
                newsDao.insertOrIgnore(newsModel);
            }
            return null;
        }
    }



    public class updateNewsAsyncTask extends  AsyncTask<AllNewsModel,Void,Void>{

        @Override
        protected Void doInBackground(AllNewsModel... newsModels) {
            newsDao.update(newsModels[0]);
            return null;
        }
    }

    public class deleteNewsAsyncTask extends AsyncTask<AllNewsModel,Void,Void>{

        @Override
        protected Void doInBackground(AllNewsModel... newsModels) {
            newsDao.delete(newsModels[0]);
            return null;
        }
    }

    public class getAllNewsAsyncTask extends  AsyncTask<Void,Void, List<AllNewsModel>>{

        @Override
        protected List<AllNewsModel> doInBackground(Void... voids) {


            List<AllNewsModel> allNewsModels=new ArrayList<>();
            if(isLovedNewsFrag){
                allNewsModels= newsDao.getLovedNews(true);

            }else if(isBoomarksFrag){
                allNewsModels=newsDao.getBookmarkedNews(true);

            }else{
                allNewsModels=newsDao.getAllNews();
            }

            if(allNewsModels.size()==0){
                Log.d("nimiBoss","Returned 0 ROOM ITEMS");
            }else{
                for(AllNewsModel allNewsModel:allNewsModels){
                    Log.d("nimiBoss",allNewsModel.toString());
                }
            }

            return allNewsModels;
        }

        @Override
        protected void onPostExecute(List<AllNewsModel> allNewsModels) {
            super.onPostExecute(allNewsModels);
                if(listOfNewsInterface!=null){
                    for(AllNewsModel allNewsModel:allNewsModels){
                        Log.d(AllNewsAdapter.class.getSimpleName(),"Onpostexecute: "+allNewsModel.getTitle().getRendered());
                    }
                listOfNewsInterface.updateListWithAdapter(allNewsModels);
            }
//
//            listOfGoalsInterface.updateListWithAdapter(goals);
        }
    }


    public  class getSingleNews extends  AsyncTask<Integer,Void,AllNewsModel>{

        @Override
        protected AllNewsModel doInBackground(Integer... integers) {

            return newsDao.getNews(integers[0]);
        }
    }
}
