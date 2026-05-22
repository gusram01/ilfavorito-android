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

public class MenuItemArrayAdapter extends ArrayAdapter<MenuData.MenuItem> {

    public MenuItemArrayAdapter(@NonNull Context context, int resource, @NonNull List<MenuData.MenuItem> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MenuData.MenuItem item = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.menu_item_layout, parent, false);
        }

        MaterialTextView title = convertView.findViewById(R.id.menuItemCustom01);
        MaterialTextView price = convertView.findViewById(R.id.menuItemCustom02);

        title.setText(item.name());
        price.setText(String.format("$%d", item.price()));

        return  convertView;
    }

}
