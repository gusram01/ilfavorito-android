package dev.gusramirez.ilfavorito;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import dev.gusramirez.ilfavorito.domain.Restaurant;

public class RestaurantItemArrayAdapter extends ArrayAdapter<Restaurant> {

    public RestaurantItemArrayAdapter(@NonNull Context context, int resource, @NonNull List<Restaurant> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Restaurant item = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.restaurant_item_layout, parent, false);
        }

        MaterialTextView name = convertView.findViewById(R.id.restaurantCustomItem01);
        name.setText(item.name());

        return convertView;
    }
}
