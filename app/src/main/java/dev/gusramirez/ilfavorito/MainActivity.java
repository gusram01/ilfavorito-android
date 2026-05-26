package dev.gusramirez.ilfavorito;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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

import dev.gusramirez.ilfavorito.data.RestaurantRepository;
import dev.gusramirez.ilfavorito.databinding.ActivityMainBinding;
import dev.gusramirez.ilfavorito.domain.Category;
import dev.gusramirez.ilfavorito.domain.Food;

public class MainActivity extends
        AppCompatActivity implements
        RestaurantListFragment.OnRestaurantSelectedListener,
        MenuItemListFragment.OnMenuItemSelectedListener,
        RestaurantListFragment.OnCategorySelectedListener,
        RestaurantListFragment.OnEditRestaurantListener,
        RestaurantFormFragment.OnManageableEventListener {
    private RestaurantRepository repository;
    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private FragmentContainerView fragmentContainerView;
    private Toolbar toolbar;
    private android.widget.SearchView searchView;
    private MenuItem searchItem;
    private MenuItem newItemButton;
    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!isTaskRoot()) {
            finish();
        }

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        fragmentManager = getSupportFragmentManager();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = new RestaurantRepository(getApplicationContext());

        fragmentContainerView = binding.mainFragmentContainerView;
        toolbar = binding.appToolbar.getRoot();

        setSupportActionBar(toolbar);
        categories = repository.getAllCategories();

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                boolean canGoBack = fragmentManager.getBackStackEntryCount() > 0;

                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(canGoBack);
                }

                updateSearchVisibility();
                updateNewButtonVisibility();
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
    public void onRestaurantSelected(int restaurantId) {
        MenuCategoriesFragment fragment = new MenuCategoriesFragment();

        Bundle args = new Bundle();
        args.putInt("RESTAURANT_ID", restaurantId);
        args.putInt("CATEGORY_TAB_INDEX", 0);
        fragment.setArguments(args);

        fragmentManager.beginTransaction()
                .replace(fragmentContainerView.getId(), fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onCategorySelected(int restaurantId, int targetTabIndex) {
        MenuCategoriesFragment fragment = new MenuCategoriesFragment();


        Bundle args = new Bundle();
        args.putInt("RESTAURANT_ID", restaurantId);
        args.putInt("CATEGORY_TAB_INDEX", targetTabIndex);
        fragment.setArguments(args);

        fragmentManager.beginTransaction()
                .replace(fragmentContainerView.getId(), fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onMenuItemSelected(Food item) {
        String type = repository.getCategoryTypeById(item.categoryId());

        int imgResourceId = switch (type) {
            case "food":
                yield R.mipmap.ic_food_placeholder;
            case "drink":
                yield R.mipmap.ic_beverage_placeholder;
            case "complement":
            default:
                yield R.mipmap.ic_complement_placeholder;
        };

        MenuDetailFragment fragment = MenuDetailFragment.newInstance(
                item.name(),
                item.price(),
                item.description(),
                imgResourceId
        );

        fragmentManager.beginTransaction()
                .replace(fragmentContainerView.getId(), fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public boolean onSupportNavigateUp() {
        Boolean canGoBack = fragmentManager.getBackStackEntryCount() > 0;

        if (canGoBack) {
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
        searchItem = menu.findItem(R.id.app_bar_search);
        newItemButton = menu.findItem(R.id.app_new_item);

        newItemButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                Fragment current = fragmentManager.findFragmentById(fragmentContainerView.getId());


                if (current instanceof Manageable<?>) {
                    Fragment frag = ((Manageable<?>) current).onCreateEntity();
                    fragmentManager.beginTransaction()
                            .replace(fragmentContainerView.getId(), frag)
                            .addToBackStack(null)
                            .setReorderingAllowed(true)
                            .commit();
                }

                return true;
            }
        });

        searchView = (android.widget.SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Fragment current = fragmentManager.findFragmentById(fragmentContainerView.getId());
                if (current instanceof Searchable) {
                    ((Searchable) current).onSearch(query);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    Fragment current = fragmentManager.findFragmentById(fragmentContainerView.getId());
                    if (current instanceof Searchable) {
                        ((Searchable) current).onSearchCleared();
                    }
                }

                return true;
            }
        });

        updateSearchVisibility();
        return true;
    }

    @Override
    public void onEditRestaurant(Fragment destiny) {
        fragmentManager.beginTransaction()
                .replace(fragmentContainerView.getId(), destiny)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onCreateItem() {
        RestaurantListFragment fragment = new RestaurantListFragment();
        fragmentManager.beginTransaction()
                .replace(fragmentContainerView.getId(), fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onEditItem() {
        fragmentManager.popBackStack();
    }

    @Override
    public void onDeleteItem() {
        fragmentManager.popBackStack();
    }

    private void updateSearchVisibility() {
        if (searchItem == null) return;
        Fragment current = fragmentManager.findFragmentById(fragmentContainerView.getId());
        searchItem.setVisible(current instanceof Searchable);
    }

    private void updateNewButtonVisibility() {
        Fragment current = fragmentManager.findFragmentById(fragmentContainerView.getId());
        newItemButton.setVisible(current instanceof Manageable<?>);
    }
}