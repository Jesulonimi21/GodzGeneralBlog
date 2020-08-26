package com.ambgen.godzgeneralblog.converters;

import androidx.room.TypeConverter;

import com.ambgen.godzgeneralblog.models.Excerpt;

public class ExcerptTypeConverter {
    @TypeConverter
    public static Excerpt toExcerpt(String excerpt){
        return new Excerpt(excerpt);
    }

    @TypeConverter
    public static  String toString(Excerpt excerpt){
        return  excerpt.getRendered();
    }



}
