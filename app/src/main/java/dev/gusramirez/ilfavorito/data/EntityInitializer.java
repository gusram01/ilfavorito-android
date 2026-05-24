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

        public static final String SQL_SEED_STATEMENT =
                "INSERT INTO " + TABLE_NAME + " (" + COL_REST_NAME + ") VALUES " +
                "('Trattoria da Luigi')," +
                "('Pizzeria Bella Napoli')," +
                "('Ristorante Il Gusto')," +
                "('Cucina di Nonna')," +
                "('Italiannissimo');";
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

        public static final String SQL_SEED_STATEMENT =
                "INSERT INTO " + TABLE_NAME + " (" +
                COL_FOOD_NAME + "," + COL_FOOD_PRICE + "," + COL_FOOD_DESCRIPTION + "," +
                COL_FOOD_REST_ID + "," + COL_FOOD_CAT_ID + ") VALUES " +
                "('Lasagna',260,'Delicious layers of pasta, meat sauce, and cheese.',1,1)," +
                "('Fettuccine Alfredo',280,'Creamy parmesan and butter sauce over fresh fettuccine.',1,1)," +
                "('Penne all''Arrabbiata',240,'Penne pasta in a spicy tomato and garlic sauce.',1,1)," +
                "('Ravioli di Carne',290,'Meat-stuffed ravioli topped with rich marinara.',1,1)," +
                "('Margherita Pizza',220,'Classic pizza with tomato sauce, mozzarella, and basil.',2,1)," +
                "('Diavola Pizza',260,'Spicy salami and chili flakes on a mozzarella base.',2,1)," +
                "('Quattro Formaggi',270,'Mozzarella, gorgonzola, parmesan, and provolone blend.',2,1)," +
                "('Calzone Classico',240,'Folded pizza stuffed with ricotta, salami, and mozzarella.',2,1)," +
                "('Spaghetti Carbonara',240,'Traditional Roman pasta with eggs, cheese, pancetta, and pepper.',3,1)," +
                "('Bucatini all''Amatriciana',250,'Thick spaghetti in a savory pancetta and tomato sauce.',3,1)," +
                "('Cacio e Pepe',220,'Simple traditional pasta tossed with pecorino romano and black pepper.',3,1)," +
                "('Saltimbocca alla Romana',340,'Veal lined with prosciutto and sage, cooked in white wine.',3,1)," +
                "('Risotto ai Funghi',280,'Creamy risotto with mushrooms and Parmesan cheese.',4,1)," +
                "('Ossobuco alla Milanese',380,'Braised veal shanks served with saffron risotto.',4,1)," +
                "('Gnocchi al Pesto',260,'Soft potato dumplings in fresh basil pesto cream.',4,1)," +
                "('Polenta al Sugo',230,'Creamy polenta topped with slow-simmered meat ragù.',4,1)," +
                "('Bistecca alla Fiorentina',490,'Thick-cut grilled T-bone steak seasoned with olive oil and herbs.',5,1)," +
                "('Linguine allo Scoglio',390,'Seafood pasta with mussels, clams, shrimp, and calamari.',5,1)," +
                "('Tiramisu',140,'Classic Italian dessert with layers of coffee-soaked ladyfingers and mascarpone cream.',5,1)," +
                "('Panna Cotta',120,'Silky vanilla cream pudding topped with mixed berry coulis.',5,1)," +
                "('Chianti Classico',180,'A robust red wine from Tuscany.',1,2)," +
                "('Pinot Grigio',160,'Crisp and refreshing white wine from Northern Italy.',1,2)," +
                "('Acqua Panna',70,'Premium still natural mineral water.',1,2)," +
                "('Sangiovese',190,'Earthy red wine with cherry and herbal notes.',1,2)," +
                "('Limoncello',120,'A sweet lemon liqueur from the Amalfi Coast.',2,2)," +
                "('Birra Moretti',90,'Classic Italian pale lager.',2,2)," +
                "('Peroni Nastro Azzurro',100,'Premium Italian pilsner, crisp and refreshing.',2,2)," +
                "('Aranciata',60,'Sparkling orange beverage made from Italian oranges.',2,2)," +
                "('Prosecco',160,'A sparkling white wine from Veneto.',3,2)," +
                "('Aperol Spritz',170,'Aperol, Prosecco, and a splash of soda water.',3,2)," +
                "('Bellini',180,'Muddled peach purée blended with Prosecco.',3,2)," +
                "('Moscato d''Asti',150,'Sweet, lightly sparkling white wine.',3,2)," +
                "('Espresso',60,'A strong and rich Italian coffee.',4,2)," +
                "('Cappuccino',80,'Espresso topped with steamed milk and thick foam.',4,2)," +
                "('Macchiato',70,'Espresso marked with a dash of frothy milk.',4,2)," +
                "('Chinotto',60,'Bittersweet dark carbonated beverage.',4,2)," +
                "('Negroni',200,'A classic cocktail made with gin, vermouth, and Campari.',5,2)," +
                "('Americano',180,'Campari, sweet vermouth, and club soda.',5,2)," +
                "('Grappa',140,'Traditional Italian fragrant grape-based pomace brandy.',5,2)," +
                "('Campari Soda',150,'A bitter, refreshing pre-mixed aperitif.',5,2)," +
                "('Bruschetta',120,'Grilled bread topped with tomatoes, garlic, and basil.',1,3)," +
                "('Focaccia al Rosmarino',90,'Oven-baked flatbread topped with olive oil and sea salt.',1,3)," +
                "('Zuppa di Minestrone',130,'Hearty Italian vegetable stew with beans and pasta.',1,3)," +
                "('Prosciutto e Melone',180,'Sweet cantaloupe wrapped in savory prosciutto crudo.',1,3)," +
                "('Garlic Knots',100,'Soft dough knots brushed with garlic butter.',2,3)," +
                "('Arancini',140,'Fried rice balls stuffed with ragù and mozzarella.',2,3)," +
                "('Supplì',120,'Roman-style fried rice croquettes filled with mozzarella.',2,3)," +
                "('Fritto Misto',200,'Assorted fried seafood and vegetables.',2,3)," +
                "('Caprese Salad',140,'Fresh mozzarella, tomatoes, and basil drizzled with olive oil.',3,3)," +
                "('Insalata Mista',110,'Mixed field greens tossed with balsamic vinaigrette.',3,3)," +
                "('Carpaccio di Manzo',220,'Thinly sliced raw beef topped with arugula and parmesan.',3,3)," +
                "('Burrata al Tartufo',230,'Creamy burrata cheese infused with black truffle essence.',3,3)," +
                "('Olive Tapenade',120,'A spread made from olives, capers, and anchovies.',4,3)," +
                "('Polpette della Nonna',150,'Classic homemade beef and pork meatballs in tomato sauce.',4,3)," +
                "('Melanzane alla Parmigiana',170,'Baked eggplant layered with mozzarella and parmesan.',4,3)," +
                "('Peperonata',120,'Stewed bell peppers with onions and tomatoes.',4,3)," +
                "('Parmesan Cheese',100,'Aged Parmesan cheese for grating over dishes.',5,3)," +
                "('Grissini',70,'Crispy and thin traditional Italian breadsticks.',5,3)," +
                "('Bresaola con Rucola',200,'Air-dried salted beef topped with fresh arugula and lemon.',5,3)," +
                "('Olive Ascolane',140,'Fried green olives stuffed with seasoned minced meat.',5,3);";
    }
}
