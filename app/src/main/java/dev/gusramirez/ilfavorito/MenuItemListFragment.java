package dev.gusramirez.ilfavorito;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import dev.gusramirez.ilfavorito.data.RestaurantRepository;
import dev.gusramirez.ilfavorito.databinding.FragmentMenuItemListBinding;
import dev.gusramirez.ilfavorito.domain.Food;

public class MenuItemListFragment extends Fragment
        implements Searchable, Manageable<Food> {

    public interface OnMenuItemSelectedListener {
        void onMenuItemSelected(Food item);
    }

    public interface OnEditMenuItemListener {
        void onEditMenuItem(Fragment formFragment);
    }

    private static final String ARG_CATEGORY_ID = "CATEGORY_ID";
    private static final String ARG_RESTAURANT_ID = "RESTAURANT_ID";
    private static final int MENU_EDIT_ID = 2000;

    private RestaurantRepository repository;
    private OnMenuItemSelectedListener listener;
    private OnEditMenuItemListener editMenuItemListener;
    private FragmentMenuItemListBinding binding;
    private ListView listView;
    private MenuItemArrayAdapter arrayAdapter;
    private List<Food> menuItems;
    private int categoryId;
    private int restaurantId;
    private int selectedPosition = -1;

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

        Fragment parent = getParentFragment();
        if (parent instanceof OnEditMenuItemListener) {
            editMenuItemListener = (OnEditMenuItemListener) parent;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        repository = new RestaurantRepository(requireContext());
        binding = FragmentMenuItemListBinding.inflate(inflater, container, false);
        listView = binding.menuItemsList;

        loadItems();

        registerForContextMenu(listView);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Food item = menuItems.get(position);
            listener.onMenuItemSelected(item);
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listView != null) loadItems();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v,
                                    @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (menuInfo instanceof AdapterView.AdapterContextMenuInfo) {
            selectedPosition = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        }

        menu.add(Menu.NONE, MENU_EDIT_ID, Menu.NONE, "Editar");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (selectedPosition < 0) return super.onContextItemSelected(item);

        if (item.getItemId() == MENU_EDIT_ID) {
            Fragment formFragment = onEditEntity(menuItems.get(selectedPosition));
            if (editMenuItemListener != null) {
                editMenuItemListener.onEditMenuItem(formFragment);
            }
            return true;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onSearch(String query) {
        List<Food> filtered = repository.getAllFoodsByRestaurantIdAndCategoryId(restaurantId, categoryId);
        filtered.removeIf(v -> !v.name().toLowerCase().contains(query.toLowerCase()));
        updateList(filtered);
    }

    @Override
    public void onSearchCleared() {
        updateList(repository.getAllFoodsByRestaurantIdAndCategoryId(restaurantId, categoryId));
    }

    @Override
    public Fragment onCreateEntity() {
        return MenuItemFormFragment.newInstance(restaurantId, categoryId);
    }

    @Override
    public Fragment onEditEntity(Food item) {
        return MenuItemFormFragment.newInstance(
                item._id(), item.name(), item.price(), item.description(),
                item.restaurantId(), item.categoryId());
    }

    @Override
    public void onDeleteEntity(Food item) {
    }

    private void loadItems() {
        menuItems = repository.getAllFoodsByRestaurantIdAndCategoryId(restaurantId, categoryId);
        arrayAdapter = new MenuItemArrayAdapter(requireContext(), R.layout.menu_item_layout, menuItems);
        listView.setAdapter(arrayAdapter);
    }

    private void updateList(List<Food> list) {
        menuItems = list;
        arrayAdapter.clear();
        arrayAdapter.addAll(list);
        arrayAdapter.notifyDataSetChanged();
    }
}
