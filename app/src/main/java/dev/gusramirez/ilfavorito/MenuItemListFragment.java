package dev.gusramirez.ilfavorito;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import dev.gusramirez.ilfavorito.databinding.FragmentMenuItemListBinding;

public class MenuItemListFragment extends Fragment {
    private static final String ARG_CATEGORY_NAME = "CATEGORY_NAME";
    private static final String ARG_RESTAURANT_NAME = "RESTAURANT_NAME";
    private FragmentMenuItemListBinding binding;
    private ListView listView;
    private MenuData.Category categoryName;
    private MenuData.Restaurant restaurantName;

    public static MenuItemListFragment newInstance(MenuData.Restaurant restaurantName, MenuData.Category categoryName) {
        MenuItemListFragment fragment = new MenuItemListFragment();

        Bundle args = new Bundle();
        args.putString(ARG_RESTAURANT_NAME, restaurantName.name());
        args.putString(ARG_CATEGORY_NAME, categoryName.name());
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            String restaurantArg =  getArguments().getString(ARG_RESTAURANT_NAME);
            String categoryArg = getArguments().getString(ARG_CATEGORY_NAME);

            restaurantName = MenuData.Restaurant.valueOf(restaurantArg);
            categoryName = MenuData.Category.valueOf(categoryArg);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMenuItemListBinding.inflate(inflater, container, false);
        listView = binding.menuItemsList;

        List<MenuData.MenuItem> items = MenuData.getItems(restaurantName, categoryName);

        MenuItemArrayAdapter adapter = new MenuItemArrayAdapter(requireContext(), R.layout.menu_item_layout, items);
        listView.setAdapter(adapter);


        return binding.getRoot();
    }
}