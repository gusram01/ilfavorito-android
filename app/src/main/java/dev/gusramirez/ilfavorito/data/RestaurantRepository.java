package dev.gusramirez.ilfavorito.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import dev.gusramirez.ilfavorito.domain.Category;
import dev.gusramirez.ilfavorito.domain.Restaurant;

public class RestaurantRepository {
    private final DatabaseManager dbManager;

    public RestaurantRepository(Context context) {
        dbManager = new DatabaseManager(context.getApplicationContext());
    }

    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> list = new ArrayList<>();
        SQLiteDatabase db = dbManager.getReadableDatabase();

        Cursor cursor = db.query(EntityInitializer.RestaurantEntity.TABLE_NAME,
                null, null, null, null, null,
                EntityInitializer.RestaurantEntity._ID, null);

        if (cursor != null) {

            try {
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow(EntityInitializer.RestaurantEntity._ID));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(EntityInitializer.RestaurantEntity.COL_REST_NAME));

                        Restaurant restaurant = new Restaurant(id, name);

                        list.add(restaurant);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }

        return list;
    }

    public List<Restaurant> getRestaurantByKindOfName(String partialName) {
        List<Restaurant> restaurants = new ArrayList<>();
        SQLiteDatabase db = dbManager.getReadableDatabase();

        Cursor cursor = db.query(EntityInitializer.RestaurantEntity.TABLE_NAME, null,
                EntityInitializer.RestaurantEntity.COL_REST_NAME + " LIKE ?",
                new String[]{"%" + partialName + "%"},
                null, null,
                EntityInitializer.RestaurantEntity._ID, null);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow(EntityInitializer.RestaurantEntity._ID));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(EntityInitializer.RestaurantEntity.COL_REST_NAME));

                        Restaurant restaurant = new Restaurant(id, name);

                        restaurants.add(restaurant);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }
        return restaurants;
    }

    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        SQLiteDatabase db = dbManager.getReadableDatabase();

        Cursor cursor = db.query(EntityInitializer.CategoryEntity.TABLE_NAME,
                null, null, null, null, null,
                EntityInitializer.CategoryEntity._ID, null);

        if (cursor != null) {

            try {
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow(EntityInitializer.CategoryEntity._ID));
                        String type = cursor.getString(cursor.getColumnIndexOrThrow(EntityInitializer.CategoryEntity.COL_CAT_TYPE));

                        Category category = new Category(id, type);

                        list.add(category);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }

        return list;
    }

}
