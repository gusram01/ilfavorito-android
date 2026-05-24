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

import dev.gusramirez.ilfavorito.databinding.FragmentRestaurantListBinding;

public class RestaurantListFragment extends Fragment implements Searchable {

    public interface OnRestaurantSelectedListener {
        void onRestaurantSelected(String key);
    }

    public interface OnCategorySelectedListener{
        void onCategorySelected(String key, int targetTabIndex);
    }

    private OnRestaurantSelectedListener restaurantSelectedListener;
    private OnCategorySelectedListener categorySelectedListener;
    private FragmentRestaurantListBinding binding;
    private ListView listView;
    private ArrayAdapter<MenuData.Restaurant> arrayAdapter;
    private List<MenuData.Restaurant> restaurants;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnRestaurantSelectedListener) {
            restaurantSelectedListener = (OnRestaurantSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnRestaurantSelectedListener");
        }

        if(context instanceof OnCategorySelectedListener){
            categorySelectedListener = (OnCategorySelectedListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement OnCategorySelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRestaurantListBinding.inflate(inflater, container,false);
        listView = binding.restaurantList;

         restaurants = new ArrayList<>(MenuData.RESTAURANTS);

        arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, new ArrayList<>(restaurants));
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

        menu.add(0,0,0, MenuData.CATEGORIES.get(0).displayName);
        menu.add(0,1,1, MenuData.CATEGORIES.get(1).displayName);
        menu.add(0,2,2, MenuData.CATEGORIES.get(2).displayName);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int idx = info.position;

        if(categorySelectedListener != null){
            categorySelectedListener.onCategorySelected( restaurants.get(idx).name(), item.getItemId());
            return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onSearch(String query){
        List<MenuData.Restaurant> newItems = MenuData.RESTAURANTS.stream()
                .filter(v -> v.toString().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());

        updateList(newItems);
    }

    @Override
    public void onSearchCleared(){
        updateList(new ArrayList<>(MenuData.RESTAURANTS));
    }

    private void updateList(List<MenuData.Restaurant> list){
        arrayAdapter.clear();
        arrayAdapter.addAll(list);
        arrayAdapter.notifyDataSetChanged();
    }
}