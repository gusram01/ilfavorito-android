package dev.gusramirez.ilfavorito;


import java.util.List;
import java.util.Map;

public final class MenuData {

    public enum Restaurant {
        TRATTORIA_DA_LUIGI("Trattoria da Luigi"),
        PIZZERIA_BELLA_NAPOLI("Pizzeria Bella Napoli"),
        RISTORANTE_IL_GUSTO("Ristorante Il Gusto"),
        CUCINA_DI_NONNA("Cucina di Nonna"),
        ITALIANNISSIMO("Italiannissimo");

        public final String displayName;

        Restaurant(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() { return displayName; }
    }

    public enum Category {
        MEAL("Comida"),
        BEVERAGE("Bebida"),
        COMPLEMENTS("Complementos");

        public final String displayName;

        Category(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() { return displayName; }
    }

    public record MenuItem(
            String name,
            int price,
            String description,
            int imageId
    ) {}

    public static final Map<Restaurant, List<Category>> CATS_BY_RESTAURANT = Map.of(
            Restaurant.TRATTORIA_DA_LUIGI,   List.of(Category.MEAL, Category.BEVERAGE, Category.COMPLEMENTS),
            Restaurant.PIZZERIA_BELLA_NAPOLI, List.of(Category.MEAL, Category.BEVERAGE, Category.COMPLEMENTS),
            Restaurant.RISTORANTE_IL_GUSTO,  List.of(Category.MEAL, Category.BEVERAGE, Category.COMPLEMENTS),
            Restaurant.CUCINA_DI_NONNA,      List.of(Category.MEAL, Category.BEVERAGE, Category.COMPLEMENTS),
            Restaurant.ITALIANNISSIMO,       List.of(Category.MEAL, Category.BEVERAGE, Category.COMPLEMENTS)
    );

    public static final Map<Category, Map<Restaurant, List<MenuItem>>> MENU_BY_CATEGORY_AND_RESTAURANT =
            Map.of(
                    Category.MEAL, Map.of(
                            Restaurant.TRATTORIA_DA_LUIGI, List.of(
                                    new MenuItem("Lasagna",              260, "Delicious layers of pasta, meat sauce, and cheese.",                          R.mipmap.ic_food_placeholder),
                                    new MenuItem("Fettuccine Alfredo",   280, "Creamy parmesan and butter sauce over fresh fettuccine.",                    R.mipmap.ic_food_placeholder),
                                    new MenuItem("Penne all'Arrabbiata", 240, "Penne pasta in a spicy tomato and garlic sauce.",                            R.mipmap.ic_food_placeholder),
                                    new MenuItem("Ravioli di Carne",     290, "Meat-stuffed ravioli topped with rich marinara.",                            R.mipmap.ic_food_placeholder)
                            ),
                            Restaurant.PIZZERIA_BELLA_NAPOLI, List.of(
                                    new MenuItem("Margherita Pizza",  220, "Classic pizza with tomato sauce, mozzarella, and basil.",         R.mipmap.ic_food_placeholder),
                                    new MenuItem("Diavola Pizza",     260, "Spicy salami and chili flakes on a mozzarella base.",             R.mipmap.ic_food_placeholder),
                                    new MenuItem("Quattro Formaggi",  270, "Mozzarella, gorgonzola, parmesan, and provolone blend.",          R.mipmap.ic_food_placeholder),
                                    new MenuItem("Calzone Classico",  240, "Folded pizza stuffed with ricotta, salami, and mozzarella.",      R.mipmap.ic_food_placeholder)
                            ),
                            Restaurant.RISTORANTE_IL_GUSTO, List.of(
                                    new MenuItem("Spaghetti Carbonara",       240, "Traditional Roman pasta with eggs, cheese, pancetta, and pepper.", R.mipmap.ic_food_placeholder),
                                    new MenuItem("Bucatini all'Amatriciana",  250, "Thick spaghetti in a savory pancetta and tomato sauce.",           R.mipmap.ic_food_placeholder),
                                    new MenuItem("Cacio e Pepe",              220, "Simple traditional pasta tossed with pecorino romano and black pepper.", R.mipmap.ic_food_placeholder),
                                    new MenuItem("Saltimbocca alla Romana",   340, "Veal lined with prosciutto and sage, cooked in white wine.",        R.mipmap.ic_food_placeholder)
                            ),
                            Restaurant.CUCINA_DI_NONNA, List.of(
                                    new MenuItem("Risotto ai Funghi",      280, "Creamy risotto with mushrooms and Parmesan cheese.",         R.mipmap.ic_food_placeholder),
                                    new MenuItem("Ossobuco alla Milanese", 380, "Braised veal shanks served with saffron risotto.",           R.mipmap.ic_food_placeholder),
                                    new MenuItem("Gnocchi al Pesto",       260, "Soft potato dumplings in fresh basil pesto cream.",          R.mipmap.ic_food_placeholder),
                                    new MenuItem("Polenta al Sugo",        230, "Creamy polenta topped with slow-simmered meat ragù.",        R.mipmap.ic_food_placeholder)
                            ),
                            Restaurant.ITALIANNISSIMO, List.of(
                                    new MenuItem("Bistecca alla Fiorentina", 490, "Thick-cut grilled T-bone steak seasoned with olive oil and herbs.",                             R.mipmap.ic_food_placeholder),
                                    new MenuItem("Linguine allo Scoglio",    390, "Seafood pasta with mussels, clams, shrimp, and calamari.",                                      R.mipmap.ic_food_placeholder),
                                    new MenuItem("Tiramisu",                 140, "Classic Italian dessert with layers of coffee-soaked ladyfingers and mascarpone cream.",         R.mipmap.ic_food_placeholder),
                                    new MenuItem("Panna Cotta",              120, "Silky vanilla cream pudding topped with mixed berry coulis.",                                    R.mipmap.ic_food_placeholder)
                            )
                    ),

                    Category.BEVERAGE, Map.of(
                            Restaurant.TRATTORIA_DA_LUIGI, List.of(
                                    new MenuItem("Chianti Classico", 180, "A robust red wine from Tuscany.",                          R.mipmap.ic_beverage_placeholder),
                                    new MenuItem("Pinot Grigio",     160, "Crisp and refreshing white wine from Northern Italy.",     R.mipmap.ic_beverage_placeholder),
                                    new MenuItem("Acqua Panna",       70, "Premium still natural mineral water.",                     R.mipmap.ic_beverage_placeholder),
                                    new MenuItem("Sangiovese",        190, "Earthy red wine with cherry and herbal notes.",           R.mipmap.ic_beverage_placeholder)
                            ),
                            Restaurant.PIZZERIA_BELLA_NAPOLI, List.of(
                                    new MenuItem("Limoncello",            120, "A sweet lemon liqueur from the Amalfi Coast.",            R.mipmap.ic_beverage_placeholder),
                                    new MenuItem("Birra Moretti",          90, "Classic Italian pale lager.",                             R.mipmap.ic_beverage_placeholder),
                                    new MenuItem("Peroni Nastro Azzurro", 100, "Premium Italian pilsner, crisp and refreshing.",          R.mipmap.ic_beverage_placeholder),
                                    new MenuItem("Aranciata",              60, "Sparkling orange beverage made from Italian oranges.",    R.mipmap.ic_beverage_placeholder)
                            ),
                            Restaurant.RISTORANTE_IL_GUSTO, List.of(
                                    new MenuItem("Prosecco",        160, "A sparkling white wine from Veneto.",               R.mipmap.ic_beverage_placeholder),
                                    new MenuItem("Aperol Spritz",   170, "Aperol, Prosecco, and a splash of soda water.",    R.mipmap.ic_beverage_placeholder),
                                    new MenuItem("Bellini",         180, "Muddled peach purée blended with Prosecco.",       R.mipmap.ic_beverage_placeholder),
                                    new MenuItem("Moscato d'Asti",  150, "Sweet, lightly sparkling white wine.",             R.mipmap.ic_beverage_placeholder)
                            ),
                            Restaurant.CUCINA_DI_NONNA, List.of(
                                    new MenuItem("Espresso",    60, "A strong and rich Italian coffee.",                          R.mipmap.ic_beverage_placeholder),
                                    new MenuItem("Cappuccino",  80, "Espresso topped with steamed milk and thick foam.",          R.mipmap.ic_beverage_placeholder),
                                    new MenuItem("Macchiato",   70, "Espresso marked with a dash of frothy milk.",               R.mipmap.ic_beverage_placeholder),
                                    new MenuItem("Chinotto",    60, "Bittersweet dark carbonated beverage.",                     R.mipmap.ic_beverage_placeholder)
                            ),
                            Restaurant.ITALIANNISSIMO, List.of(
                                    new MenuItem("Negroni",       200, "A classic cocktail made with gin, vermouth, and Campari.",   R.mipmap.ic_beverage_placeholder),
                                    new MenuItem("Americano",     180, "Campari, sweet vermouth, and club soda.",                   R.mipmap.ic_beverage_placeholder),
                                    new MenuItem("Grappa",        140, "Traditional Italian fragrant grape-based pomace brandy.",    R.mipmap.ic_beverage_placeholder),
                                    new MenuItem("Campari Soda",  150, "A bitter, refreshing pre-mixed aperitif.",                  R.mipmap.ic_beverage_placeholder)
                            )
                    ),

                    Category.COMPLEMENTS, Map.of(
                            Restaurant.TRATTORIA_DA_LUIGI, List.of(
                                    new MenuItem("Bruschetta",          120, "Grilled bread topped with tomatoes, garlic, and basil.",        R.mipmap.ic_complement_placeholder),
                                    new MenuItem("Focaccia al Rosmarino", 90, "Oven-baked flatbread topped with olive oil and sea salt.",    R.mipmap.ic_complement_placeholder),
                                    new MenuItem("Zuppa di Minestrone", 130, "Hearty Italian vegetable stew with beans and pasta.",          R.mipmap.ic_complement_placeholder),
                                    new MenuItem("Prosciutto e Melone", 180, "Sweet cantaloupe wrapped in savory prosciutto crudo.",         R.mipmap.ic_complement_placeholder)
                            ),
                            Restaurant.PIZZERIA_BELLA_NAPOLI, List.of(
                                    new MenuItem("Garlic Knots",  100, "Soft dough knots brushed with garlic butter.",                   R.mipmap.ic_complement_placeholder),
                                    new MenuItem("Arancini",      140, "Fried rice balls stuffed with ragù and mozzarella.",             R.mipmap.ic_complement_placeholder),
                                    new MenuItem("Supplì",        120, "Roman-style fried rice croquettes filled with mozzarella.",      R.mipmap.ic_complement_placeholder),
                                    new MenuItem("Fritto Misto",  200, "Assorted fried seafood and vegetables.",                         R.mipmap.ic_complement_placeholder)
                            ),
                            Restaurant.RISTORANTE_IL_GUSTO, List.of(
                                    new MenuItem("Caprese Salad",        140, "Fresh mozzarella, tomatoes, and basil drizzled with olive oil.",       R.mipmap.ic_complement_placeholder),
                                    new MenuItem("Insalata Mista",       110, "Mixed field greens tossed with balsamic vinaigrette.",                R.mipmap.ic_complement_placeholder),
                                    new MenuItem("Carpaccio di Manzo",   220, "Thinly sliced raw beef topped with arugula and parmesan.",            R.mipmap.ic_complement_placeholder),
                                    new MenuItem("Burrata al Tartufo",   230, "Creamy burrata cheese infused with black truffle essence.",           R.mipmap.ic_complement_placeholder)
                            ),
                            Restaurant.CUCINA_DI_NONNA, List.of(
                                    new MenuItem("Olive Tapenade",            120, "A spread made from olives, capers, and anchovies.",                      R.mipmap.ic_complement_placeholder),
                                    new MenuItem("Polpette della Nonna",      150, "Classic homemade beef and pork meatballs in tomato sauce.",              R.mipmap.ic_complement_placeholder),
                                    new MenuItem("Melanzane alla Parmigiana", 170, "Baked eggplant layered with mozzarella and parmesan.",                  R.mipmap.ic_complement_placeholder),
                                    new MenuItem("Peperonata",                120, "Stewed bell peppers with onions and tomatoes.",                          R.mipmap.ic_complement_placeholder)
                            ),
                            Restaurant.ITALIANNISSIMO, List.of(
                                    new MenuItem("Parmesan Cheese",    100, "Aged Parmesan cheese for grating over dishes.",                       R.mipmap.ic_complement_placeholder),
                                    new MenuItem("Grissini",            70, "Crispy and thin traditional Italian breadsticks.",                    R.mipmap.ic_complement_placeholder),
                                    new MenuItem("Bresaola con Rucola", 200, "Air-dried salted beef topped with fresh arugula and lemon.",         R.mipmap.ic_complement_placeholder),
                                    new MenuItem("Olive Ascolane",      140, "Fried green olives stuffed with seasoned minced meat.",              R.mipmap.ic_complement_placeholder)
                            )
                    )
            );

    /** All restaurants as an ordered list (preserves source ordering). */
    public static final List<Restaurant> RESTAURANTS = List.of(Restaurant.values());

    /** All categories as an ordered list. */
    public static final List<Category> CATEGORIES = List.of(Category.values());

    /**
     * Returns the menu items for a specific restaurant and category.
     */
    public static List<MenuItem> getItems(Restaurant restaurant, Category category) {
        Map<Restaurant, List<MenuItem>> byRestaurant = MENU_BY_CATEGORY_AND_RESTAURANT.get(category);
        if (byRestaurant == null) return List.of();
        List<MenuItem> items = byRestaurant.get(restaurant);
        return items != null ? items : List.of();
    }

    /**
     * Finds a single MenuItem by restaurant, category, and item name.
     */
    public static MenuItem findItem(Restaurant restaurant, Category category, String name) {
        return getItems(restaurant, category).stream()
                .filter(item -> item.name().equals(name))
                .findFirst()
                .orElse(null);
    }

    private MenuData() {}
}