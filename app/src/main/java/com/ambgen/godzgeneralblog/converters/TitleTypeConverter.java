package com.ambgen.godzgeneralblog.converters;

import androidx.room.TypeConverter;

import com.ambgen.godzgeneralblog.models.Title;

public class TitleTypeConverter {

    @TypeConverter
    public static Title toTitle(String title){
        return new Title(title);
    }

    @TypeConverter
    public static  String toString(Title title){
        if(title==null){
            return "unknown";
        }
        return  title.getRendered();
    }
}
