package dev.gusramirez.ilfavorito;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.ColorInt;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import dev.gusramirez.ilfavorito.data.EntityInitializer;
import dev.gusramirez.ilfavorito.data.RestaurantRepository;
import dev.gusramirez.ilfavorito.databinding.FragmentRestaurantFormBinding;
import dev.gusramirez.ilfavorito.domain.Restaurant;

public class RestaurantFormFragment extends Fragment {

    public interface OnManageableEventListener {
        void onCreateItem();
    }

    private static final String RESTAURANT_NAME = "restaurantArg";
    private RestaurantRepository repository;
    private FragmentRestaurantFormBinding binding;
    private FragmentManager fragmentManager;
    private String restaurantName;
    private EditText inputName;
    private MaterialButton createButton;
    private MaterialButton editButton;
    private MaterialButton deleteButton;
    private OnManageableEventListener listener;

    private RestaurantFormFragment() {
    }

    public static Fragment newInstance(String param1) {
        RestaurantFormFragment fragment = new RestaurantFormFragment();
        Bundle args = new Bundle();
        args.putString(RESTAURANT_NAME, param1);
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

        editButton.setVisibility(restaurantName != null ? View.VISIBLE : View.GONE);
        deleteButton.setVisibility(restaurantName != null ? View.VISIBLE : View.GONE);


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputName.getText() == null || inputName.getText().toString().isEmpty()) {
                    Snackbar snack = Snackbar.make(getView(), "El nombre no puede estar vacio", BaseTransientBottomBar.LENGTH_SHORT);

                    snack.setBackgroundTint(ContextCompat.getColor(requireContext(), android.R.color.holo_orange_dark));
                    snack.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
                    snack.show();
                    return;
                }

                Restaurant restaurant = new Restaurant(null, inputName.getText().toString());

                try {

                    repository.addOneRestaurant(restaurant);

                    listener.onCreateItem();


                } catch (Throwable t) {
                    Snackbar snack = Snackbar.make(getView(), t.getMessage(), BaseTransientBottomBar.LENGTH_SHORT);

                    snack.setBackgroundTint(ContextCompat.getColor(requireContext(), android.R.color.holo_orange_dark));
                    snack.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
                    snack.show();
                }

            }
        });

        return binding.getRoot();
    }
}