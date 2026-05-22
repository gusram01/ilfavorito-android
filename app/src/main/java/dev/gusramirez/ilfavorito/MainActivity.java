package dev.gusramirez.ilfavorito;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import dev.gusramirez.ilfavorito.databinding.ActivityMainBinding;

public class MainActivity extends
        AppCompatActivity implements
        RestaurantListFragment.OnRestaurantSelectedListener, MenuCategoriesFragment.OnMenuItemSelectedListener {
    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private FragmentContainerView fragmentContainerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        fragmentManager = getSupportFragmentManager();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        fragmentContainerView = binding.mainFragmentContainerView;


        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fragmentManager.beginTransaction()
                .replace(fragmentContainerView.getId(), new RestaurantListFragment())
                .addToBackStack(null)
                .setReorderingAllowed(true)
                .commit();
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
    public void onMenuItemSelected(String mealName) {
        System.out.println("here menu item selected:::"+mealName);
    }
}