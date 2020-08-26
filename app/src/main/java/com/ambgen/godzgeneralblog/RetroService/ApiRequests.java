package com.ambgen.godzgeneralblog.RetroService;

import com.ambgen.godzgeneralblog.models.AllNewsModel;
import com.ambgen.godzgeneralblog.models.CategoriesModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiRequests {


    @GET("posts")
    Call<List<AllNewsModel>> allPosts(@Query("_fields[]")ArrayList<String> _fields);

    @GET("posts")
    Call<List<AllNewsModel>> promotedCategory(@Query("categories")String category,@Query("_fields[]")ArrayList<String> _fields);

    @GET("posts")
    Call<List<AllNewsModel>> searchPosts(@Query("search")String search);

    @GET("posts")
    Call<List<AllNewsModel>> getNewsByPage(@Query("page") String page,@Query("_fields[]")ArrayList<String> _fields);

    @GET("categories")
  Call< List<CategoriesModel>> getAllCategories(@Query("_fields[]")ArrayList<String> _fields);

    @GET("posts")
    Call<List<AllNewsModel>> getNewsByCategory(@Query("categories")String category,@Query("_fields[]")ArrayList<String> _fields,@Query("page")String page);

}
