package com.irate.nguyet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.irate.nguyet.feature.manage.RestaurantRatingFragment;
import com.irate.nguyet.feature.manage.RestaurantSearchFragment;

public final class SearchViewPagerAdapter extends FragmentStateAdapter {

    public int getItemCount() {
        return 2;
    }

    @NonNull
    public Fragment createFragment(int position) {
        Fragment fragment;
        if (position == 1) {
            fragment = new RestaurantRatingFragment();
        } else {
            fragment = new RestaurantSearchFragment();
        }
        return fragment;
    }

    public SearchViewPagerAdapter(@NonNull AppCompatActivity activity) {
        super(activity);
    }
}
