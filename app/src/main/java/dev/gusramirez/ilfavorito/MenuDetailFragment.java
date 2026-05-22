package dev.gusramirez.ilfavorito;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import dev.gusramirez.ilfavorito.databinding.FragmentMenuDetailBinding;

public class MenuDetailFragment extends Fragment {

    private static final String ARG_NAME = "param1";
    private static final String ARG_PRICE = "param2";
    private static final String ARG_DESCRIPTION = "param3";
    private static final String ARG_IMAGE = "param4";

    private String itemName;
    private int itemPrice;
    private String itemDesc;
    private int itemImageId;

    private FragmentMenuDetailBinding binding;
    private TextView name;
    private TextView price;
    private TextView description;
    private ImageView image;


    public MenuDetailFragment() {}

    public static MenuDetailFragment newInstance(String name, int price, String description, int imageId) {
        MenuDetailFragment fragment = new MenuDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putInt(ARG_PRICE, price);
        args.putString(ARG_DESCRIPTION, description);
        args.putInt(ARG_IMAGE, imageId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemName = getArguments().getString(ARG_NAME);
            itemPrice = getArguments().getInt(ARG_PRICE);
            itemDesc = getArguments().getString(ARG_DESCRIPTION);
            itemImageId = getArguments().getInt(ARG_IMAGE);
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
        price.setText(String.format("$%d", itemPrice));
        description.setText(itemDesc);
        image.setImageResource(itemImageId);


        return binding.getRoot();
    }
}