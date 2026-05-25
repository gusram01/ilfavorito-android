package dev.gusramirez.ilfavorito;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import dev.gusramirez.ilfavorito.databinding.FragmentMenuDetailBinding;

public class MenuDetailFragment extends Fragment {

    private static final String ARG_NAME = "param1";
    private static final String ARG_PRICE = "param2";
    private static final String ARG_DESCRIPTION = "param3";
    private static final String ARG_IMG_RESOURCE_ID = "param4";

    private String itemName;
    private double itemPrice;
    private String itemDesc;
    private int itemImgResourceId;

    private FragmentMenuDetailBinding binding;
    private TextView name;
    private TextView price;
    private TextView description;
    private ImageView image;


    public MenuDetailFragment() {}

    public static MenuDetailFragment newInstance(String name, double price, String description, int imageResourceId) {
        MenuDetailFragment fragment = new MenuDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putDouble(ARG_PRICE, price);
        args.putString(ARG_DESCRIPTION, description);
        args.putInt(ARG_IMG_RESOURCE_ID, imageResourceId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemName = getArguments().getString(ARG_NAME);
            itemPrice = getArguments().getDouble(ARG_PRICE);
            itemDesc = getArguments().getString(ARG_DESCRIPTION);
            itemImgResourceId = getArguments().getInt(ARG_IMG_RESOURCE_ID);
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMenuDetailBinding.inflate(inflater, container, false);
        name = binding.menuItemDetailName;
        price = binding.menuItemDetailPrice;
        description = binding.menuItemDetailDescription;
        image = binding.menuItemImagePlaceholder;


        name.setText(itemName);
        price.setText(String.format("$%.2f", itemPrice));
        description.setText(itemDesc);
        image.setImageResource(itemImgResourceId);


        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null && ((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(itemName);
        }
    }
}