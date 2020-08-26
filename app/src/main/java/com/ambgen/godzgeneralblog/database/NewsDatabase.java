package com.ambgen.godzgeneralblog.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ambgen.godzgeneralblog.converters.CategoriesTypeConverter;
import com.ambgen.godzgeneralblog.converters.ContentTypeConverter;
import com.ambgen.godzgeneralblog.converters.ExcerptTypeConverter;
import com.ambgen.godzgeneralblog.converters.TitleTypeConverter;
import com.ambgen.godzgeneralblog.daos.NewsDao;
import com.ambgen.godzgeneralblog.models.AllNewsModel;

@Database(entities = {AllNewsModel.class}, version = 1, exportSchema = false)
@TypeConverters({TitleTypeConverter.class, ExcerptTypeConverter.class, CategoriesTypeConverter.class, ContentTypeConverter.class})
public abstract  class NewsDatabase extends RoomDatabase {
    public abstract NewsDao newsDao();
    public static NewsDatabase INSTANCE;

    public static NewsDatabase getAppDatabase(Context context){
        if(INSTANCE==null){
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(), NewsDatabase.class,"news-database").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
