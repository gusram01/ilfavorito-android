package dev.gusramirez.ilfavorito;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

import dev.gusramirez.ilfavorito.databinding.ActivityMainBinding;

public class MainActivity extends
        AppCompatActivity implements
        RestaurantListFragment.OnRestaurantSelectedListener,
        MenuItemListFragment.OnMenuItemSelectedListener {
    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private FragmentContainerView fragmentContainerView;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!isTaskRoot()){
            finish();
        }

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        fragmentManager = getSupportFragmentManager();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        fragmentContainerView = binding.mainFragmentContainerView;
        toolbar = binding.appToolbar.getRoot();

        setSupportActionBar(toolbar);

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                boolean canGoBack = fragmentManager.getBackStackEntryCount() > 0;

                if(getSupportActionBar() != null){
                    getSupportActionBar().setDisplayHomeAsUpEnabled(canGoBack);
                }
            }
        });

        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .replace(fragmentContainerView.getId(), new RestaurantListFragment())
                    .setReorderingAllowed(true)
                    .commit();
        }
    }

    @Override
    public void onRestaurantSelected(String key) {
        MenuCategoriesFragment fragment = new MenuCategoriesFragment();

        Bundle args = new Bundle();
        args.putString("RESTAURANT_NAME", key);
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

    @Override
    public boolean onSupportNavigateUp() {
        Boolean canGoBack = fragmentManager.getBackStackEntryCount() > 0;

        if(canGoBack){
            fragmentManager.popBackStack();
            return true;
        }

        return super.onSupportNavigateUp();
    }
}