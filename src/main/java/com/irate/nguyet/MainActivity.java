package com.irate.nguyet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public final class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        SearchViewPagerAdapter adapter = new SearchViewPagerAdapter(this);
        final ViewPager2 mainViewPager = findViewById(R.id.main_view_pager);
        bottomNav = findViewById(R.id.bottom_navigation);
        mainViewPager.setAdapter(adapter);
        mainViewPager.registerOnPageChangeCallback(new OnPageChangeCallback() {
            public void onPageSelected(int position) {
                MainActivity.this.updateTitle(position);
                bottomNav.setSelectedItemId(position == 0 ? R.id.mnu_search : R.id.mnu_rate);
            }
        });

        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.mnu_search:
                    MainActivity.this.updateTitle(0);
                    mainViewPager.setCurrentItem(0);
                    return true;
                case R.id.mnu_rate:
                    MainActivity.this.updateTitle(1);
                    mainViewPager.setCurrentItem(1);
                    return true;
                default:
                    return false;
            }
        });
        bottomNav.setSelectedItemId(R.id.mnu_search);
        this.updateTitle(0);
    }

    private void updateTitle(int pageIndex) {
        CharSequence title;
        if (pageIndex == 1) {
            title = "Post rate";
        } else {
            title = "Search";
        }
        this.setTitle(title);
    }

    public static void start(@NonNull Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
