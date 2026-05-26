package dev.gusramirez.ilfavorito;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import dev.gusramirez.ilfavorito.data.RestaurantRepository;
import dev.gusramirez.ilfavorito.databinding.FragmentMenuItemFormBinding;
import dev.gusramirez.ilfavorito.domain.Category;
import dev.gusramirez.ilfavorito.domain.Food;

public class MenuItemFormFragment extends Fragment {

    public interface OnMenuItemFormEventListener {
        void onMenuItemCreated();
        void onMenuItemEdited();
        void onMenuItemDeleted();
    }

    private static final String ARG_FOOD_ID = "foodIdArg";
    private static final String ARG_FOOD_NAME = "foodNameArg";
    private static final String ARG_FOOD_PRICE = "foodPriceArg";
    private static final String ARG_FOOD_DESCRIPTION = "foodDescriptionArg";
    private static final String ARG_FOOD_RESTAURANT_ID = "foodRestaurantIdArg";
    private static final String ARG_FOOD_CATEGORY_ID = "foodCategoryIdArg";

    private RestaurantRepository repository;
    private FragmentMenuItemFormBinding binding;
    private OnMenuItemFormEventListener listener;

    private int foodId = -1;
    private String foodName;
    private double foodPrice = 0.0;
    private String foodDescription;
    private int restaurantId = -1;
    private int categoryId = -1;

    private List<Category> categories;
    private EditText inputName;
    private EditText inputPrice;
    private EditText inputDescription;
    private Spinner spinnerType;
    private MaterialButton createButton;
    private MaterialButton editButton;
    private MaterialButton deleteButton;

    private MenuItemFormFragment() {
    }

    public static Fragment newInstance(int restaurantId, int categoryId) {
        MenuItemFormFragment fragment = new MenuItemFormFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FOOD_RESTAURANT_ID, restaurantId);
        args.putInt(ARG_FOOD_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance(int id, String name, double price, String description, int restaurantId, int categoryId) {
        MenuItemFormFragment fragment = new MenuItemFormFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FOOD_ID, id);
        args.putString(ARG_FOOD_NAME, name);
        args.putDouble(ARG_FOOD_PRICE, price);
        args.putString(ARG_FOOD_DESCRIPTION, description);
        args.putInt(ARG_FOOD_RESTAURANT_ID, restaurantId);
        args.putInt(ARG_FOOD_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnMenuItemFormEventListener) {
            listener = (OnMenuItemFormEventListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnMenuItemFormEventListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            foodId = getArguments().getInt(ARG_FOOD_ID, -1);
            foodName = getArguments().getString(ARG_FOOD_NAME);
            foodPrice = getArguments().getDouble(ARG_FOOD_PRICE, 0.0);
            foodDescription = getArguments().getString(ARG_FOOD_DESCRIPTION);
            restaurantId = getArguments().getInt(ARG_FOOD_RESTAURANT_ID, -1);
            categoryId = getArguments().getInt(ARG_FOOD_CATEGORY_ID, -1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && ((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            String title = resolveTitle();
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        repository = new RestaurantRepository(requireContext());
        binding = FragmentMenuItemFormBinding.inflate(inflater, container, false);

        inputName = binding.menuItemFormInputName;
        inputPrice = binding.menuItemFormInputPrice;
        inputDescription = binding.menuItemFormInputDescription;
        spinnerType = binding.menuItemFormSpinnerType;
        createButton = binding.menuItemFormCreateButton;
        editButton = binding.menuItemFormEditButton;
        deleteButton = binding.menuItemFormDeleteButton;

        categories = repository.getAllCategories();
        setupSpinner();

        boolean isEditing = foodId != -1;
        if (foodName != null) inputName.setText(foodName);
        if (foodPrice > 0) inputPrice.setText(String.valueOf(foodPrice));
        if (foodDescription != null) inputDescription.setText(foodDescription);

        createButton.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        editButton.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        deleteButton.setVisibility(isEditing ? View.VISIBLE : View.GONE);

        createButton.setOnClickListener(v -> {
            if (!validateInputs()) return;
            Food food = buildFoodFromInputs(0);
            try {
                repository.addOneFood(food);
                listener.onMenuItemCreated();
            } catch (Throwable t) {
                showError(t.getMessage());
            }
        });

        editButton.setOnClickListener(v -> {
            if (!validateInputs()) return;
            Food food = buildFoodFromInputs(foodId);
            try {
                repository.updateOneFood(food);
                listener.onMenuItemEdited();
            } catch (Throwable t) {
                showError(t.getMessage());
            }
        });

        deleteButton.setOnClickListener(v ->
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Eliminar platillo")
                        .setMessage("¿Estás seguro de que deseas eliminar \"" + foodName + "\"?")
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            try {
                                repository.deleteOneFood(foodId);
                                listener.onMenuItemDeleted();
                            } catch (Throwable t) {
                                showError(t.getMessage());
                            }
                        })
                        .show()
        );

        return binding.getRoot();
    }

    private void setupSpinner() {
        String[] typeNames = new String[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            typeNames[i] = categories.get(i).type();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, typeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i)._id() == categoryId) {
                spinnerType.setSelection(i);
                break;
            }
        }
    }

    private Food buildFoodFromInputs(int id) {
        int selectedCategoryId = categories.get(spinnerType.getSelectedItemPosition())._id();
        return new Food(
                id,
                inputName.getText().toString().trim(),
                Double.parseDouble(inputPrice.getText().toString()),
                inputDescription.getText().toString().trim(),
                restaurantId,
                selectedCategoryId
        );
    }

    private boolean validateInputs() {
        if (inputName.getText() == null || inputName.getText().toString().trim().isEmpty()) {
            showError("El nombre no puede estar vacío");
            return false;
        }
        String priceText = inputPrice.getText() == null ? "" : inputPrice.getText().toString();
        if (priceText.isEmpty()) {
            showError("El precio no puede estar vacío");
            return false;
        }
        try {
            double price = Double.parseDouble(priceText);
            if (price < 0) {
                showError("El precio debe ser mayor o igual a cero");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("El precio debe ser un número válido");
            return false;
        }
        if (inputDescription.getText() == null || inputDescription.getText().toString().trim().isEmpty()) {
            showError("La descripción no puede estar vacía");
            return false;
        }
        return true;
    }

    private String resolveTitle() {
        if (categories != null) {
            for (Category c : categories) {
                if (c._id() == categoryId) return c.type();
            }
        }
        return "Platillo";
    }

    private void showError(String message) {
        Snackbar snack = Snackbar.make(requireView(), message, BaseTransientBottomBar.LENGTH_SHORT);
        snack.setBackgroundTint(ContextCompat.getColor(requireContext(), android.R.color.holo_orange_dark));
        snack.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
        snack.show();
    }
}
