package com.ambgen.godzgeneralblog.models;

import androidx.annotation.NonNull;

public class CategoriesModel {
    int id;
    int count;
    String name;

    public CategoriesModel(int id, int count, String name) {
        this.id = id;
        this.count = count;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @NonNull
    @Override
    public String toString() {
        if(this.getId()==9006){
            return "Categories";
        }
        return this.getName()+" ("+this.getCount()+")";
    }
}
