package dev.gusramirez.ilfavorito;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MenuItemsByCategoryPageAdapter extends FragmentPagerAdapter {
    private List<MenuData.Category> categories;
    private MenuData.Restaurant restaurant;

    public MenuItemsByCategoryPageAdapter(@NonNull FragmentManager fm, int behavior, MenuData.Restaurant restaurantArg, List<MenuData.Category> categoriesArg) {
        super(fm, behavior);
        restaurant = restaurantArg;
        categories = categoriesArg;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        MenuData.Category category = categories.get(position);
        return MenuItemListFragment.newInstance(restaurant, category);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position).toString();
    }
}
