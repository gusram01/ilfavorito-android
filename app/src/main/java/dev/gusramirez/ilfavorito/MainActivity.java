package dev.gusramirez.ilfavorito;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import dev.gusramirez.ilfavorito.databinding.ActivityMainBinding;

public class MainActivity extends
        AppCompatActivity implements
        RestaurantListFragment.OnRestaurantSelectedListener,
        MenuItemListFragment.OnMenuItemSelectedListener,
        RestaurantListFragment.OnCategorySelectedListener{
    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private FragmentContainerView fragmentContainerView;
    private Toolbar toolbar;
    private android.widget.SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!isTaskRoot()){
            finish();
        }

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        fragmentManager = getSupportFragmentManager();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentContainerView = binding.mainFragmentContainerView;
        toolbar = binding.appToolbar.getRoot();
//        searchView = toolbar.findViewById(R.id.app_bar_search);

        setSupportActionBar(toolbar);

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                boolean canGoBack = fragmentManager.getBackStackEntryCount() > 0;

                if(getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(canGoBack);
                }

                updateSearchVisibility();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            RestaurantListFragment fragment = new RestaurantListFragment();
            fragmentManager.beginTransaction()
                    .replace(fragmentContainerView.getId(), fragment)
                    .setReorderingAllowed(true)
                    .commit();

        }
    }

    @Override
    public void onRestaurantSelected(String key) {
        MenuCategoriesFragment fragment = new MenuCategoriesFragment();

        Bundle args = new Bundle();
        args.putString("RESTAURANT_NAME", key);
        args.putInt("CATEGORY_INDEX", 0);
        fragment.setArguments(args);

        fragmentManager.beginTransaction()
                .replace(fragmentContainerView.getId(),fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCategorySelected(String key, int targetTabIndex){
        MenuCategoriesFragment fragment = new MenuCategoriesFragment();

        Bundle args = new Bundle();
        args.putString("RESTAURANT_NAME", key);
        args.putInt("CATEGORY_INDEX", targetTabIndex);
        fragment.setArguments(args);

        fragmentManager.beginTransaction()
                .replace(fragmentContainerView.getId(),fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onMenuItemSelected(MenuData.MenuItem item) {
        MenuDetailFragment fragment = MenuDetailFragment.newInstance(
                item.name(),
                item.price(),
                item.description(),
                item.imageId()
        );

        Bundle args = new Bundle();
        args.putString("param1", item.name());
        args.putInt("param2", item.price());
        args.putString("param3", item.description());
        args.putInt("param4", item.imageId());

        fragmentManager.beginTransaction()
                .replace(fragmentContainerView.getId(), fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    private void updateSearchVisibility() {
        Fragment current = fragmentManager.findFragmentById(fragmentContainerView.getId());
        searchView.setVisibility(current instanceof Searchable ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Boolean canGoBack = fragmentManager.getBackStackEntryCount() > 0;

        if(canGoBack){
            fragmentManager.popBackStack();
            return true;
        }

        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(
                R.menu.app_menu,
                menu
        );
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);

        searchView = (android.widget.SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Fragment current = fragmentManager.findFragmentById(fragmentContainerView.getId());
                if(current instanceof Searchable ){
                    ((Searchable) current).onSearch(query);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    Fragment current = fragmentManager.findFragmentById(fragmentContainerView.getId());
                    if(current instanceof Searchable ){
                        ((Searchable) current).onSearchCleared();
                    }
                }

                return true;
            }
        });


        return true;
    }
}