package dev.gusramirez.ilfavorito.domain;

public record Food(int _id, String name, double price, String description, int restaurantId, int categoryId) {
}
