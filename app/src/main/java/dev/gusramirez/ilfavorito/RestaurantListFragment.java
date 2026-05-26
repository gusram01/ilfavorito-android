package dev.gusramirez.ilfavorito;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import dev.gusramirez.ilfavorito.data.RestaurantRepository;
import dev.gusramirez.ilfavorito.databinding.FragmentRestaurantListBinding;
import dev.gusramirez.ilfavorito.domain.Category;
import dev.gusramirez.ilfavorito.domain.Restaurant;

public class RestaurantListFragment extends Fragment
        implements Searchable, Manageable<Restaurant> {

    public interface OnRestaurantSelectedListener {
        void onRestaurantSelected(int restaurantId);
    }

    public interface OnCategorySelectedListener {
        void onCategorySelected(int restaurantId, int targetTabIndex);
    }

    public interface OnEditRestaurantListener {
        void onEditRestaurant(Fragment destiny);
    }

    private static final int MENU_EDIT_ID = 1000;
    private static final int SUBMENU_TITLE_ID=1001;

    private OnRestaurantSelectedListener restaurantSelectedListener;
    private OnCategorySelectedListener categorySelectedListener;
    private OnEditRestaurantListener editRestaurantListener;
    private int selectedPosition = -1;
    private RestaurantRepository repository;
    private FragmentRestaurantListBinding binding;
    private ListView listView;
    private RestaurantItemArrayAdapter arrayAdapter;
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

        if (context instanceof OnEditRestaurantListener){
            editRestaurantListener = (OnEditRestaurantListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnEditRestaurantListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        repository = new RestaurantRepository(requireContext());
        binding = FragmentRestaurantListBinding.inflate(inflater, container, false);
        listView = binding.restaurantList;

        restaurants = repository.getAllRestaurants();

        arrayAdapter = new RestaurantItemArrayAdapter(requireContext(), R.layout.restaurant_item_layout, restaurants);
        listView.setAdapter(arrayAdapter);

        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                restaurantSelectedListener.onRestaurantSelected(restaurants.get(position)._id());
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

        if (menuInfo instanceof AdapterView.AdapterContextMenuInfo) {
            selectedPosition = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        }

        List<Category> categories = repository.getAllCategories();

        menu.add(Menu.NONE, MENU_EDIT_ID, Menu.NONE, "Editar");
        SubMenu submenu = menu.addSubMenu(Menu.NONE, SUBMENU_TITLE_ID, Menu.NONE, "Ir a la sección: ");
        submenu.add(0, 0, 0, categories.get(0).type());
        submenu.add(0, 1, 1, categories.get(1).type());
        submenu.add(0, 2, 2, categories.get(2).type());
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (selectedPosition < 0) return super.onContextItemSelected(item);

        int itemId = item.getItemId();

        if (itemId == MENU_EDIT_ID) {
            Fragment formFragment = onEditEntity(restaurants.get(selectedPosition));
            editRestaurantListener.onEditRestaurant(formFragment);
            return true;
        }

        if(itemId == SUBMENU_TITLE_ID){
            return false;
        }

        if (categorySelectedListener != null) {
            categorySelectedListener.onCategorySelected(restaurants.get(selectedPosition)._id(), itemId);
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
        List<Restaurant> newItems = repository.getAllRestaurants();
        updateList(newItems);
    }

    @Override
    public Fragment onCreateEntity() {
        return RestaurantFormFragment.newInstance();
    }

    @Override
    public Fragment onEditEntity(Restaurant item) {
        return RestaurantFormFragment.newInstance(item._id(), item.name());
    }

    @Override
    public void onDeleteEntity(Restaurant item) {

    }

    private void updateList(List<Restaurant> list) {
        arrayAdapter.clear();
        arrayAdapter.addAll(list);
        arrayAdapter.notifyDataSetChanged();
    }
}