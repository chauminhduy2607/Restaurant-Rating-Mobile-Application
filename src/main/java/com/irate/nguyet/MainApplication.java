package com.irate.nguyet;

import android.app.Application;

import androidx.room.Room;

import com.irate.nguyet.data.db.AppDatabase;
import com.irate.nguyet.data.repo.RestaurantRepo;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class MainApplication extends Application {

    public static class SearchParam
    {
        private String keyword;
        private String type = "All";
        public  String getKeyword(){
            return  this.keyword;
        }
        public  String getType(){
            return this.type;
        }

        public void setKeyword(String kw){
            this.keyword = kw;
        }

        public  void setType(String type){
            this.type = type;
        }
    }
    public  static SearchParam searchObj = new SearchParam();

    public static final DateFormat dateTimeFormat = DateFormat.getDateTimeInstance();

    public static final List<String> RESTAURANT_TYPES = Arrays.asList("Grill", "Fast food", "Sea food", "Cafe", "Pub", "Casual Dining");

    public  static final  List<String> GetSearchResType()
    {
        List<String> resType = new ArrayList<>();
        resType.add("All");
        resType.addAll(RESTAURANT_TYPES);
        return  resType;
    }

    private static MainApplication instance;

    private RestaurantRepo repository;

    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "data.db").build();
        repository = new RestaurantRepo(db);
    }

    public RestaurantRepo getRepository() {
        return repository;
    }

    public NumberFormat getPriceFormat() {
        return DecimalFormat.getCurrencyInstance(Locale.US);
    }
}
