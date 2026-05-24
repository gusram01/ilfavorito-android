package dev.gusramirez.ilfavorito;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dev.gusramirez.ilfavorito.data.RestaurantRepository;
import dev.gusramirez.ilfavorito.databinding.FragmentRestaurantListBinding;
import dev.gusramirez.ilfavorito.domain.Category;
import dev.gusramirez.ilfavorito.domain.Restaurant;

public class RestaurantListFragment extends Fragment implements Searchable {

    public interface OnRestaurantSelectedListener {
        void onRestaurantSelected(String key);
    }

    public interface OnCategorySelectedListener {
        void onCategorySelected(String key, int targetTabIndex);
    }

    private OnRestaurantSelectedListener restaurantSelectedListener;
    private OnCategorySelectedListener categorySelectedListener;
    private RestaurantRepository repository;
    private FragmentRestaurantListBinding binding;
    private ListView listView;
    private ArrayAdapter<Restaurant> arrayAdapter;
    private List<Restaurant> restaurants;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnRestaurantSelectedListener) {
            restaurantSelectedListener = (OnRestaurantSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnRestaurantSelectedListener");
        }

        if (context instanceof OnCategorySelectedListener) {
            categorySelectedListener = (OnCategorySelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnCategorySelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        repository = new RestaurantRepository(requireContext());
        binding = FragmentRestaurantListBinding.inflate(inflater, container, false);
        listView = binding.restaurantList;

        restaurants = repository.getAllRestaurants();

        arrayAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_list_item_1, restaurants);
        listView.setAdapter(arrayAdapter);

        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                restaurantSelectedListener.onRestaurantSelected(restaurants.get(position).name());
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null && ((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Restaurantes");
        }
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        List<Category> categories = repository.getAllCategories();

        menu.add(0, 0, 0, categories.get(0).type());
        menu.add(0, 1, 1, categories.get(1).type());
        menu.add(0, 2, 2, categories.get(2).type());
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int idx = info.position;

        if (categorySelectedListener != null) {
            categorySelectedListener.onCategorySelected(restaurants.get(idx).name(), item.getItemId());
            return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onSearch(String query) {
        List<Restaurant> newItems = repository.getRestaurantByKindOfName(query);

        updateList(newItems);
    }

    @Override
    public void onSearchCleared() {
        updateList(restaurants);
    }

    private void updateList(List<Restaurant> list) {
        arrayAdapter.clear();
        arrayAdapter.addAll(list);
        arrayAdapter.notifyDataSetChanged();
    }
}