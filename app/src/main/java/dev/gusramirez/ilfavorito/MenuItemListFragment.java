package dev.gusramirez.ilfavorito;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dev.gusramirez.ilfavorito.databinding.FragmentMenuItemListBinding;

public class MenuItemListFragment extends Fragment implements Searchable {

    public interface OnMenuItemSelectedListener {
        void onMenuItemSelected(MenuData.MenuItem item);
    }

    private static final String ARG_CATEGORY_NAME = "CATEGORY_NAME";
    private static final String ARG_RESTAURANT_NAME = "RESTAURANT_NAME";
    private OnMenuItemSelectedListener listener;
    private FragmentMenuItemListBinding binding;
    private ListView listView;
    private MenuItemArrayAdapter arrayAdapter;
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnMenuItemSelectedListener) {
            listener = (OnMenuItemSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnMenuItemSelectedListener");
        }
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

        List<MenuData.MenuItem> menuItems = MenuData.getItems(restaurantName, categoryName);

        arrayAdapter = new MenuItemArrayAdapter(requireContext(), R.layout.menu_item_layout, new ArrayList<>(menuItems));
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuData.MenuItem item = menuItems.get(position);

                listener.onMenuItemSelected(item);
            }
        });


        return binding.getRoot();
    }
    @Override
    public void onSearch(String query){
        List<MenuData.MenuItem> newItems = MenuData.getItems(restaurantName, categoryName)
                .stream().filter(v -> v.name().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());

        updateList(newItems);
    }

    @Override
    public void onSearchCleared(){
        updateList(MenuData.getItems(restaurantName, categoryName));
    }

    private void updateList(List<MenuData.MenuItem> list){
        arrayAdapter.clear();
        arrayAdapter.addAll(list);
        arrayAdapter.notifyDataSetChanged();
    }

}