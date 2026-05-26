package dev.gusramirez.ilfavorito.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import dev.gusramirez.ilfavorito.domain.Category;
import dev.gusramirez.ilfavorito.domain.Food;
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
                EntityInitializer.RestaurantEntity._ID + " ASC", null);

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

    public Restaurant getOneRestaurantById(int id) {
        SQLiteDatabase db = dbManager.getReadableDatabase();

        Cursor cursor = db.query(EntityInitializer.RestaurantEntity.TABLE_NAME, null,
                EntityInitializer.RestaurantEntity._ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null, "1");

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return new Restaurant(
                            cursor.getInt(cursor.getColumnIndexOrThrow(EntityInitializer.RestaurantEntity._ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(EntityInitializer.RestaurantEntity.COL_REST_NAME))
                    );
                }
            } finally {
                cursor.close();
            }
        }

        return null;
    }

    public int addOneRestaurant(Restaurant restaurant) throws Throwable {
        SQLiteDatabase db = dbManager.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(EntityInitializer.RestaurantEntity.COL_REST_NAME, restaurant.name());

        long newRowId = db.insert(EntityInitializer.RestaurantEntity.TABLE_NAME, null, contentValues);

        if (newRowId == -1) {
            throw new Throwable("Algo salío mal, intente nuevamente.");
        }

        return (int) newRowId;
    }

    public void updateOneRestaurant(Restaurant restaurant) throws Throwable {
        SQLiteDatabase db = dbManager.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(EntityInitializer.RestaurantEntity.COL_REST_NAME, restaurant.name());

        int rows = db.update(
                EntityInitializer.RestaurantEntity.TABLE_NAME,
                contentValues,
                EntityInitializer.RestaurantEntity._ID + " = ?",
                new String[]{String.valueOf(restaurant._id())}
        );

        if (rows == 0) {
            throw new Throwable("No se pudo actualizar el restaurante.");
        }
    }

    public void deleteOneRestaurant(int id) throws Throwable {
        SQLiteDatabase db = dbManager.getWritableDatabase();

        int rows = db.delete(
                EntityInitializer.RestaurantEntity.TABLE_NAME,
                EntityInitializer.RestaurantEntity._ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        if (rows == 0) {
            throw new Throwable("No se pudo eliminar el restaurante.");
        }
    }

    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        SQLiteDatabase db = dbManager.getReadableDatabase();

        Cursor cursor = db.query(EntityInitializer.CategoryEntity.TABLE_NAME,
                null, null, null, null, null,
                EntityInitializer.CategoryEntity._ID + " ASC");

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

    public String getCategoryTypeById(int id) {
        SQLiteDatabase db = dbManager.getReadableDatabase();

        Cursor cursor = db.query(EntityInitializer.CategoryEntity.TABLE_NAME, null,
                EntityInitializer.CategoryEntity._ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null, "1");

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    return cursor.getString(cursor.getColumnIndexOrThrow(EntityInitializer.CategoryEntity.COL_CAT_TYPE));
                }
            } finally {
                cursor.close();
            }
        }

        return "";
    }

    public int addOneFood(Food food) throws Throwable {
        SQLiteDatabase db = dbManager.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EntityInitializer.FoodEntity.COL_FOOD_NAME, food.name());
        values.put(EntityInitializer.FoodEntity.COL_FOOD_PRICE, food.price());
        values.put(EntityInitializer.FoodEntity.COL_FOOD_DESCRIPTION, food.description());
        values.put(EntityInitializer.FoodEntity.COL_FOOD_REST_ID, food.restaurantId());
        values.put(EntityInitializer.FoodEntity.COL_FOOD_CAT_ID, food.categoryId());

        long newRowId = db.insert(EntityInitializer.FoodEntity.TABLE_NAME, null, values);
        if (newRowId == -1) {
            throw new Throwable("Algo salió mal, intente nuevamente.");
        }
        return (int) newRowId;
    }

    public void updateOneFood(Food food) throws Throwable {
        SQLiteDatabase db = dbManager.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EntityInitializer.FoodEntity.COL_FOOD_NAME, food.name());
        values.put(EntityInitializer.FoodEntity.COL_FOOD_PRICE, food.price());
        values.put(EntityInitializer.FoodEntity.COL_FOOD_DESCRIPTION, food.description());
        values.put(EntityInitializer.FoodEntity.COL_FOOD_REST_ID, food.restaurantId());
        values.put(EntityInitializer.FoodEntity.COL_FOOD_CAT_ID, food.categoryId());

        int rows = db.update(
                EntityInitializer.FoodEntity.TABLE_NAME,
                values,
                EntityInitializer.FoodEntity._ID + " = ?",
                new String[]{String.valueOf(food._id())}
        );
        if (rows == 0) {
            throw new Throwable("No se pudo actualizar el platillo.");
        }
    }

    public void deleteOneFood(int id) throws Throwable {
        SQLiteDatabase db = dbManager.getWritableDatabase();
        int rows = db.delete(
                EntityInitializer.FoodEntity.TABLE_NAME,
                EntityInitializer.FoodEntity._ID + " = ?",
                new String[]{String.valueOf(id)}
        );
        if (rows == 0) {
            throw new Throwable("No se pudo eliminar el platillo.");
        }
    }

    public List<Food> getAllFoodsByRestaurantIdAndCategoryId(int restaurantId, int categoryId) {
        List<Food> list = new ArrayList<>();
        SQLiteDatabase db = dbManager.getReadableDatabase();


        String rawquery = "SELECT f._id as id, f.name as name, f.price as price, f.description as description FROM " +
                EntityInitializer.FoodEntity.TABLE_NAME + " as f " +
                "LEFT JOIN " + EntityInitializer.RestaurantEntity.TABLE_NAME + " as r " +
                "ON f." + EntityInitializer.FoodEntity.COL_FOOD_REST_ID + " = " + "r." + EntityInitializer.RestaurantEntity._ID +
                " LEFT JOIN " + EntityInitializer.CategoryEntity.TABLE_NAME + " as c " +
                "ON f." + EntityInitializer.FoodEntity.COL_FOOD_CAT_ID + " = " + "c." + EntityInitializer.CategoryEntity._ID +
                " WHERE r." + EntityInitializer.RestaurantEntity._ID + " = ?" + " AND c." + EntityInitializer.CategoryEntity._ID + " = ?";


        Cursor cursor = db.rawQuery(rawquery, new String[]{String.valueOf(restaurantId), String.valueOf(categoryId)});

        if (cursor != null) {

            try {
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                        double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                        Food item = new Food(id, name, price, description, restaurantId, categoryId);

                        list.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }

        return list;
    }
}
