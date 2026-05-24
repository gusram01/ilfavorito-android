package dev.gusramirez.ilfavorito.data;

import android.provider.BaseColumns;

public class EntityInitializer {
    public static class RestaurantEntity implements BaseColumns {
        public static final String TABLE_NAME = "restaurant";
        public static final String COL_REST_NAME = "name";

        public static final String SQL_CREATE_STATEMENT = "" +
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                RestaurantEntity._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_REST_NAME + " TEXT NOT NULL)";
        public static final String SQL_DROP_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class CategoryEntity implements BaseColumns {
        public static final String TABLE_NAME = "category";
        public static final String COL_CAT_TYPE = "type";

        public static final String SQL_CREATE_STATEMENT = "" +
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                CategoryEntity._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CAT_TYPE + " TEXT NOT NULL)";

        public static final String SQL_DROP_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String SQL_SEED_STATEMENT = "" +
                "INSERT INTO " + TABLE_NAME + " (" + COL_CAT_TYPE + ") VALUES " +
                "('food'), " +
                "('drink'), " +
                "('complement');";
    }

    public static class FoodEntity implements BaseColumns {
        public static final String TABLE_NAME = "food";
        public static final String COL_FOOD_NAME = "name";
        public static final String COL_FOOD_PRICE = "price";
        public static final String COL_FOOD_DESCRIPTION = "description";
        public static final String COL_FOOD_REST_ID = "restaurant_id";
        public static final String COL_FOOD_CAT_ID = "category_id";

        public static final String SQL_CREATE_STATEMENT = "" +
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                FoodEntity._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FOOD_NAME + " TEXT NOT NULL, " +
                COL_FOOD_PRICE + " DOUBLE NOT NULL, " +
                COL_FOOD_DESCRIPTION + " TEXT NOT NULL, " +
                COL_FOOD_REST_ID + " INTEGER NOT NULL, " +
                COL_FOOD_CAT_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + COL_FOOD_REST_ID + ") REFERENCES " +
                RestaurantEntity.TABLE_NAME + "(" + RestaurantEntity._ID + "), " +
                "FOREIGN KEY(" + COL_FOOD_CAT_ID + ") REFERENCES " +
                CategoryEntity.TABLE_NAME + "(" + CategoryEntity._ID + "))";

        public static final String SQL_DROP_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
