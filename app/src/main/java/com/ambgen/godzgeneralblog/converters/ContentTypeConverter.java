package com.ambgen.godzgeneralblog.converters;

import androidx.room.TypeConverter;

import com.ambgen.godzgeneralblog.models.Content;


public class ContentTypeConverter {

    @TypeConverter
    public static Content toContent(String renderred){
        return new Content(renderred);
    }

    @TypeConverter
    public static  String toString(Content content){
        return  content.getRendered();
    }

}
