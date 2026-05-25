package dev.gusramirez.ilfavorito;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

import dev.gusramirez.ilfavorito.domain.Category;
import dev.gusramirez.ilfavorito.domain.Restaurant;

public class MenuItemsByCategoryPageAdapter extends FragmentPagerAdapter {
    private List<Category> categories;
    private int selectedRestaurantId;

    public MenuItemsByCategoryPageAdapter(@NonNull FragmentManager fm, int behavior, int restaurantIdArg, List<Category> categoriesArg) {
        super(fm, behavior);
        selectedRestaurantId = restaurantIdArg;
        categories = categoriesArg;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Category category = categories.get(position);
        return MenuItemListFragment.newInstance(selectedRestaurantId, category._id());
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position).type();
    }
}
