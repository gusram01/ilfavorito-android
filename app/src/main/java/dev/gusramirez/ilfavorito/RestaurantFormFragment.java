package dev.gusramirez.ilfavorito;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import dev.gusramirez.ilfavorito.data.EntityInitializer;
import dev.gusramirez.ilfavorito.data.RestaurantRepository;
import dev.gusramirez.ilfavorito.databinding.FragmentRestaurantFormBinding;
import dev.gusramirez.ilfavorito.domain.Restaurant;

public class RestaurantFormFragment extends Fragment {

    public interface OnManageableEventListener {
        void onCreateItem();
        void onEditItem();
        void onDeleteItem();
    }

    private static final String RESTAURANT_NAME = "restaurantArg";
    private static final String RESTAURANT_ID = "restaurantIdArg";
    private RestaurantRepository repository;
    private FragmentRestaurantFormBinding binding;
    private FragmentManager fragmentManager;
    private String restaurantName;
    private int restaurantId = -1;
    private EditText inputName;
    private MaterialButton createButton;
    private MaterialButton editButton;
    private MaterialButton deleteButton;
    private OnManageableEventListener listener;

    private RestaurantFormFragment() {
    }

    public static Fragment newInstance(int id, String name) {
        RestaurantFormFragment fragment = new RestaurantFormFragment();
        Bundle args = new Bundle();
        args.putInt(RESTAURANT_ID, id);
        args.putString(RESTAURANT_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance() {
        RestaurantFormFragment fragment = new RestaurantFormFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof RestaurantFormFragment.OnManageableEventListener) {
            listener = (OnManageableEventListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnRestaurantSelectedListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null && ((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Manejar Restaurant");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            restaurantName = getArguments().getString(RESTAURANT_NAME);
            restaurantId = getArguments().getInt(RESTAURANT_ID, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        repository = new RestaurantRepository(requireContext());
        binding = FragmentRestaurantFormBinding.inflate(inflater, container, false);
        fragmentManager = getParentFragmentManager();
        inputName = binding.restaurantFormInputName;
        createButton = binding.restaurantFormCreateButton;
        editButton = binding.restaurantFormEditButton;
        deleteButton = binding.restaurantFormDeleteButton;

        inputName.setText(restaurantName);

        createButton.setVisibility(restaurantName != null ? View.GONE : View.VISIBLE);
        editButton.setVisibility(restaurantName != null ? View.VISIBLE : View.GONE);
        deleteButton.setVisibility(restaurantName != null ? View.VISIBLE : View.GONE);


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputName.getText() == null || inputName.getText().toString().isEmpty()) {
                    showError("El nombre no puede estar vacio");
                    return;
                }

                Restaurant restaurant = new Restaurant(null, inputName.getText().toString());

                try {
                    repository.addOneRestaurant(restaurant);
                    listener.onCreateItem();
                } catch (Throwable t) {
                    showError(t.getMessage());
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputName.getText() == null || inputName.getText().toString().isEmpty()) {
                    showError("El nombre no puede estar vacio");
                    return;
                }

                Restaurant updated = new Restaurant(restaurantId, inputName.getText().toString());

                try {
                    repository.updateOneRestaurant(updated);
                    listener.onEditItem();
                } catch (Throwable t) {
                    showError(t.getMessage());
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Eliminar restaurante")
                        .setMessage("¿Estás seguro de que deseas eliminar \"" + restaurantName + "\"?")
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            try {
                                repository.deleteOneRestaurant(restaurantId);
                                listener.onDeleteItem();
                            } catch (Throwable t) {
                                showError(t.getMessage());
                            }
                        })
                        .show();
            }
        });

        return binding.getRoot();
    }

    private void showError(String message) {
        Snackbar snack = Snackbar.make(requireView(), message, BaseTransientBottomBar.LENGTH_SHORT);
        snack.setBackgroundTint(ContextCompat.getColor(requireContext(), android.R.color.holo_orange_dark));
        snack.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
        snack.show();
    }
}