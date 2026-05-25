package dev.gusramirez.ilfavorito;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import dev.gusramirez.ilfavorito.data.RestaurantRepository;
import dev.gusramirez.ilfavorito.databinding.FragmentMenuCategoriesBinding;
import dev.gusramirez.ilfavorito.domain.Category;
import dev.gusramirez.ilfavorito.domain.Restaurant;

public class MenuCategoriesFragment extends Fragment implements Searchable {

    private RestaurantRepository repository;

    private FragmentMenuCategoriesBinding binding;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private int selectedRestaurantId;
    private int selectedCategoryTabIndex;
    private Restaurant restaurant;

    public MenuCategoriesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            selectedRestaurantId = getArguments().getInt("RESTAURANT_ID");
            selectedCategoryTabIndex = getArguments().getInt("CATEGORY_TAB_INDEX");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        repository = new RestaurantRepository(requireContext());
        binding = FragmentMenuCategoriesBinding.inflate(inflater, container, false);
        viewPager = binding.categoriesViewPager;
        tabLayout = binding.categoriesTabsLabels.getRoot();

        List<Category> categories = repository.getAllCategories();
        restaurant = repository.getOneRestaurantById(selectedRestaurantId);

        PagerAdapter pagerAdapter = new MenuItemsByCategoryPageAdapter(
                getChildFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                selectedRestaurantId,
                categories
        );
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        if (getArguments() != null) {
            viewPager.setCurrentItem(selectedCategoryTabIndex);
        }

        return binding.getRoot();
    }

    private void forwardSearch(String query, boolean clear) {
        int currentPosition = viewPager.getCurrentItem();
        String tag = "android:switcher:" + viewPager.getId() + ":" + currentPosition;
        Fragment currentTab = getChildFragmentManager().findFragmentByTag(tag);
        if (currentTab instanceof Searchable) {
            if (clear) ((Searchable) currentTab).onSearchCleared();
            else ((Searchable) currentTab).onSearch(query);
        }
    }

    @Override
    public void onSearch(String query) {
        forwardSearch(query, false);
    }

    @Override
    public void onSearchCleared() {
        forwardSearch(null, true);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null && ((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(restaurant != null ? restaurant.name() : "Lista de restaurantes");
        }
    }
}