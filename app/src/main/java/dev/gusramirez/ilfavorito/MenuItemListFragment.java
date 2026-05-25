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

import dev.gusramirez.ilfavorito.data.RestaurantRepository;
import dev.gusramirez.ilfavorito.databinding.FragmentMenuItemListBinding;
import dev.gusramirez.ilfavorito.domain.Food;

public class MenuItemListFragment extends Fragment implements Searchable {

    public interface OnMenuItemSelectedListener {
        void onMenuItemSelected(Food item);
    }

    private static final String ARG_CATEGORY_ID = "CATEGORY_ID";
    private static final String ARG_RESTAURANT_ID = "RESTAURANT_ID";
    private RestaurantRepository repository;
    private OnMenuItemSelectedListener listener;
    private FragmentMenuItemListBinding binding;
    private ListView listView;
    private MenuItemArrayAdapter arrayAdapter;
    private int categoryId;
    private int restaurantId;

    private MenuItemListFragment() {
    }

    public static MenuItemListFragment newInstance(int restaurantIdArg, int categoryIdArg) {
        MenuItemListFragment fragment = new MenuItemListFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_RESTAURANT_ID, restaurantIdArg);
        args.putInt(ARG_CATEGORY_ID, categoryIdArg);
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

        if (getArguments() != null) {
            restaurantId = getArguments().getInt(ARG_RESTAURANT_ID);
            categoryId = getArguments().getInt(ARG_CATEGORY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        repository = new RestaurantRepository(requireContext());
        binding = FragmentMenuItemListBinding.inflate(inflater, container, false);
        listView = binding.menuItemsList;

        List<Food> menuItems = repository.getAllFoodsByRestaurantIdAndCategoryId(restaurantId, categoryId);

        arrayAdapter = new MenuItemArrayAdapter(requireContext(), R.layout.menu_item_layout, menuItems);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food item = menuItems.get(position);

                listener.onMenuItemSelected(item);
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onSearch(String query) {
        List<Food> newItems = repository.getAllFoodsByRestaurantIdAndCategoryId(restaurantId, categoryId)
                .stream().filter(v -> v.name().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());

        updateList(newItems);
    }

    @Override
    public void onSearchCleared() {
        updateList(repository.getAllFoodsByRestaurantIdAndCategoryId(restaurantId, categoryId));
    }

    private void updateList(List<Food> list) {
        arrayAdapter.clear();
        arrayAdapter.addAll(list);
        arrayAdapter.notifyDataSetChanged();
    }

}