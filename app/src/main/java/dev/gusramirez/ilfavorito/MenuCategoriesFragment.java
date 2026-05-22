package dev.gusramirez.ilfavorito;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import dev.gusramirez.ilfavorito.databinding.FragmentMenuCategoriesBinding;

public class MenuCategoriesFragment extends Fragment {

    public MenuCategoriesFragment() {    }

    private MenuData.Restaurant restaurantName;
    private FragmentMenuCategoriesBinding binding;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            String restaurantArg = getArguments().getString("RESTAURANT_NAME");
            restaurantName = MenuData.Restaurant.valueOf(restaurantArg);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMenuCategoriesBinding.inflate(inflater, container, false);
        viewPager = binding.categoriesViewPager;
        tabLayout = binding.categoriesTabsLabels.getRoot();

        List<MenuData.Category> categories = MenuData.CATEGORIES;

        PagerAdapter pagerAdapter = new MenuItemsByCategoryPageAdapter(
                getChildFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                restaurantName,
                categories
                );
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);


        return binding.getRoot();
    }
}