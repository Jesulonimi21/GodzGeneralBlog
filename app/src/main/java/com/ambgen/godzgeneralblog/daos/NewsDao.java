package com.ambgen.godzgeneralblog.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ambgen.godzgeneralblog.models.AllNewsModel;
import com.ambgen.godzgeneralblog.models.CategoriesModel;

import java.util.List;

@Dao
public abstract class NewsDao {
    @Insert
    public abstract Long insert(AllNewsModel allNewsModel);

    @Insert
    public   abstract   Long[] insert(List<AllNewsModel> allNewsModel);

    @Query("SELECT * FROM `AllNewsModel` ORDER BY `id` ASC")
   public abstract  List<AllNewsModel> getAllNews();

    @Query("SELECT * FROM `AllNewsModel` WHERE `id` =:id")
    public  abstract  AllNewsModel getNews(int id);

    @Query("SELECT * FROM `AllNewsModel` WHERE `isLoved`=:isLovedNews")
    public  abstract  List<AllNewsModel>  getLovedNews(boolean isLovedNews);


    @Query("SELECT * FROM `AllNewsModel` WHERE `isBookMarked`=:isBookMarkedNews")
    public  abstract  List<AllNewsModel>  getBookmarkedNews(boolean isBookMarkedNews);

    @Update
    public abstract  void update(AllNewsModel newsModel);

    @Delete
    public abstract  void delete(AllNewsModel newsModel);

    @Query("Select * from AllNewsModel where id=:id")
    public  abstract   List<AllNewsModel> getItemById(int id);


   public   void insertOrIgnore(AllNewsModel newsModel){
        List<AllNewsModel> allNewsModels=getItemById(newsModel.getId());
        if(allNewsModels.isEmpty()){
            insert(newsModel);
        }
    }
}
