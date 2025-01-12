package org.example.database;

import com.pengrad.telegrambot.model.Location;
import org.example.User;
import org.example.order.Order;
import org.example.order.OrderedItem;
import org.example.Category;
import org.example.Product;

import java.util.*;

public interface DB {
    List<User> USERS = new ArrayList<>();
    Map<Long, List<OrderedItem>> LONG_LIST_MAP = new HashMap<>();

    Map<Product, Integer> CART = new HashMap<>();

    List<Location> LOCATIONS = new ArrayList<>();

    List<Order> ORDERS = new ArrayList<>();

    List<Category> CATEGORIES = new ArrayList<>(List.of(
            new Category("Pizzas🍕"),
            new Category("Burgers🍔"),
            new Category("Lavashes🌯"),
            //new Category("Sandwich🥪"),
            new Category("Ichimliklar🥤")
    ));

    List<Product> PRODUCTS = new ArrayList<>(List.of(
            new Product("Pepperoni", 1, 55_000),
            new Product("Meat Lovers", 1, 55_000),
            new Product("Veggie", 1, 55_000),
            new Product("Hawaiian", 1, 55_000),
            new Product("Mac & Cheese", 1, 55_000),
            new Product("BBQ Chicken", 1, 55_000),

            new Product("Cheese Burger", 2, 40_000),
            new Product("Hamburger", 2, 40_000),
            new Product("Fish Burger", 2, 40_000),
            new Product("Double Burger", 2, 45_000),

            new Product("Lavash", 3, 32_000),
            new Product("", 3, 32_000)
    ));
    List<OrderedItem> ORDERED_ITEMS = new ArrayList<>();

    /*static List<Product> getProductsByCategory(Category category) {
        return PRODUCTS.stream().filter(product -> Objects.equals(product.getCategoryId(), category.getId())).collect(Collectors.toList());
    }*/
}