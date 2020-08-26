package com.ambgen.godzgeneralblog.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AllNewsModel {
    //author,title, image, excerpt,date,jetpack_featured_media_url


    public AllNewsModel() {
    }

    public AllNewsModel(int author, Title title, Excerpt excerpt, String date, String jetpack_featured_media_url,
                        long[] categories, int id, boolean isLoved, boolean isBookMarked,Content content) {
        this.author = author;
        this.title = title;
        this.excerpt = excerpt;
        this.date = date;
        this.jetpack_featured_media_url = jetpack_featured_media_url;
        this.categories = categories;
        this.id=id;
        this.isLoved=isLoved;
        this.isBookMarked = isBookMarked;
        this.content=content;
    }


    public int getId() {
        return id;
    }

    @PrimaryKey
    int id;
    int author;
    //object
    Title title;
    //object
    Excerpt excerpt;
    String date;
    String jetpack_featured_media_url;
    long[] categories;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    Content content;
    boolean isLoved;
   public boolean isBookMarked;
    public void setId(int id) {
        this.id = id;
    }

    public boolean isLoved() {
        return isLoved;
    }

    public void setLoved(boolean loved) {
        isLoved = loved;
    }

    public long[] getCategories() {
        return categories;
    }

    public void setCategories(long[] categories) {
        this.categories = categories;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Excerpt getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(Excerpt excerpt) {
        this.excerpt = excerpt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getJetpack_featured_media_url() {
        return jetpack_featured_media_url;
    }

    public void setJetpack_featured_media_url(String jetpack_featured_media_url) {
        this.jetpack_featured_media_url = jetpack_featured_media_url;
    }

    public boolean isBookMarked() {
        return isBookMarked;
    }

    public void setBookMarked(boolean bookMarked) {
        isBookMarked = bookMarked;
    }

    @NonNull
    @Override
    public String toString() {
        String string=
                "id :"+this.getId()+"\n"+
                 "title : "+this.getTitle().getRendered()+"\n"+
                 "category : "+this.getCategories()[0]+"\n"+
                 "Excerpt : "+this.getExcerpt().getRendered()+"\n"+
                 "Date : " +this.getDate()+"\n"+
                 "media url "+this.getJetpack_featured_media_url()+"\n"+
                  "isBookMarked"+this.isBookMarked +"\n"+
                "isLoved : "+this.isLoved;

        return string;
    }
}
