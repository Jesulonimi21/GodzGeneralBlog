package com.ambgen.godzgeneralblog.RetroService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ServiceBuilder {
  //  public static final String URL="https://www.godzgeneralblog.com/wp-json/wp/v2/";
public static final String URL="https://techcrunch.com/wp-json/wp/v2/";
   public static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static HttpLoggingInterceptor logger=new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    public  static OkHttpClient.Builder okhttp=new OkHttpClient.Builder().addInterceptor(logger);
    public static Retrofit.Builder builder=new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create());
    private static Retrofit retrofit=builder.build();
    public static <S> S buildService(Class<S> serviceType){

        return retrofit.create(serviceType);
    }
}
