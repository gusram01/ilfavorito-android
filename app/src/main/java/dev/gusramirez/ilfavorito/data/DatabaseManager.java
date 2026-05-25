package dev.gusramirez.ilfavorito.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "il_favorito";
    private static final int DATABASE_VERSION = 3;

    public DatabaseManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EntityInitializer.RestaurantEntity.SQL_CREATE_STATEMENT);
        db.execSQL(EntityInitializer.CategoryEntity.SQL_CREATE_STATEMENT);
        db.execSQL(EntityInitializer.FoodEntity.SQL_CREATE_STATEMENT);

        db.execSQL(EntityInitializer.RestaurantEntity.SQL_SEED_STATEMENT);
        db.execSQL(EntityInitializer.CategoryEntity.SQL_SEED_STATEMENT);
        db.execSQL(EntityInitializer.FoodEntity.SQL_SEED_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handling cascade
        db.execSQL(EntityInitializer.FoodEntity.SQL_DROP_STATEMENT);
        db.execSQL(EntityInitializer.CategoryEntity.SQL_DROP_STATEMENT);
        db.execSQL(EntityInitializer.RestaurantEntity.SQL_DROP_STATEMENT);

        onCreate(db);
    }
}
