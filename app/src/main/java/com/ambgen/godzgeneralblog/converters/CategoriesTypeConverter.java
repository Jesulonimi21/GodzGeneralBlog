package com.ambgen.godzgeneralblog.converters;

import android.util.Log;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CategoriesTypeConverter {
    @TypeConverter
    public static long[] restoreCategories(String category){
        return new Gson().fromJson(String.valueOf(category), new TypeToken<long[]>() {}.getType());
    }

    @TypeConverter
    public static String saveList(long[] arrayOfInt) {
        String json=new Gson().toJson(arrayOfInt);
        return new Gson().toJson(arrayOfInt);
    }

    public static String getInteger(String unformattedCategory){
        Log.d("nimiBoss",unformattedCategory);
       return unformattedCategory.substring(1,unformattedCategory.length()-1);
    }
}
