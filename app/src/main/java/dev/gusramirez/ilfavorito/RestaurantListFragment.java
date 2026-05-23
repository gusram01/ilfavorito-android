package dev.gusramirez.ilfavorito;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
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

    private OnRestaurantSelectedListener listener;
    private FragmentRestaurantListBinding binding;
    private ListView listView;
    private ArrayAdapter<MenuData.Restaurant> arrayAdapter;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnRestaurantSelectedListener) {
            listener = (OnRestaurantSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnRestaurantSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRestaurantListBinding.inflate(inflater, container,false);
        listView = binding.restaurantList;

        List<MenuData.Restaurant> items = MenuData.RESTAURANTS;

        arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, new ArrayList<>(items));
        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onRestaurantSelected(items.get(position).name());
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