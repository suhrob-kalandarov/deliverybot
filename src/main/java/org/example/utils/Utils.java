package org.example.utils;

import org.example.Category;
import org.example.database.DB;
import org.example.order.Order;
import org.example.order.OrderStatus;
import org.example.order.OrderedItem;
import org.example.services.Constants;
import org.example.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Utils {

    static Integer getProductAmount(int categoryId) {
        int productsAmount = 0;
        for (Product product : DB.PRODUCTS) {
            if (product.getCategoryId().equals(categoryId)) {
                productsAmount++;
            }
        }
        return productsAmount;
    }

    static Product getSelectedProduct(String productName) {
        for (Product product : DB.PRODUCTS) {
            if (product.getName().equals(productName)) return product;
        } return null;
    }

    static Category getSelectedCategory(String categoryName) {
        Category chosenCategory = null;
        String ctName = categoryName.substring(0, categoryName.length()-1);
        for (Category category : DB.CATEGORIES) {
            if (category.getName().equals(categoryName)){
                chosenCategory = category;
                break;
            }
        }
        return chosenCategory;
    }

    static Integer getCategoryId(String categoryName) {
        Integer categoryId = null;
        for (Category category : DB.CATEGORIES) {
            if (category.getName().equals(categoryName.substring(0, categoryName.length()-1))) {
                categoryId = category.getId();
                break;
            }
        }
        return categoryId;
    }

    static Product getSelectedProductById(Integer productId) {
        return DB.PRODUCTS.get(productId);
    }


    static String getCategoryName(Product product) {
        for (Category category : DB.CATEGORIES) {
            if (category.getId().equals(product.getCategoryId())){
                return category.getName();
            }
        }
        return null;
    }

    static Category getCategory(Product product) {
        for (Category category : DB.CATEGORIES) {
            if (category.getId().equals(product.getCategoryId())){
                return category;
            }
        }
        return null;
    }

    static String generateCartProductList() {
        Map<Product, Integer> cart = DB.CART;
        Set<Product> products = cart.keySet();
        StringBuilder summary = new StringBuilder();
        summary.append("\t" + Constants.CART_PRODUCTS).append("\n");
        double total = 0;
        int i = 0;
        for (Product product : products) {
            summary.append(i + 1).append(". ").append(product.getName()).append("\n")
                    .append("    price : ").append(product.getPrice()).append(" so'm").append("\n")
                    .append("    amount : ").append(cart.get(product)).append("\n\n");
            i++;
            total += product.getPrice() * cart.get(product);
        }

        summary.append("Total : ").append(total).append(" $");
        return summary.toString();
    }

    static void moveCartToOrders(Long chatId) {
        Order order = new Order(chatId);
        DB.ORDERS.add(order);
        for (Product product : DB.CART.keySet()) {
            OrderedItem orderItem = new OrderedItem(
                    order.getId(),
                    product.getId(),
                    DB.CART.get(product)
            );
            DB.ORDERED_ITEMS.add(orderItem);
        }
        DB.CART.clear();
    }

    static String generateOrderedProductList(Long chatId) {
        StringBuilder stringBuilder = new StringBuilder();
        Integer i = getOrderId(chatId);
        OrderStatus status = OrderStatus.NOT_FOUND;
        if (i != null) {
            Order order = DB.ORDERS.get(i - 1);
            status = order.getStatus();
        }
        double total = 0;
        stringBuilder.append("\t" + Constants.ORDERS_LIST + "\n")
                .append("Status : ").append(status)
                .append("\n");
        if (i == null) return stringBuilder.toString();

        List<OrderedItem> orderedItems = getOrderItems(getOrderId(chatId));
        int index = 1;
        for (OrderedItem orderedItem : orderedItems) {
            Product product = DB.PRODUCTS.get(orderedItem.getProductId() - 1);
            stringBuilder.append("\n").append(index).append(". ")
                    .append(product.getName()).append("\n")
                    .append("    price : ").append(product.getPrice()).append("\n")
                    .append("    amount : ").append(orderedItem.getAmount()).append("\n");
            total += product.getPrice() * orderedItem.getAmount();
            index++;
        }
        stringBuilder.append("\n").append("Total : ").append(total).append(" so'm");
        return stringBuilder.toString();
    }

    static List<OrderedItem> getOrderItems(int orderId) {
        List<OrderedItem> orderItems = new ArrayList<>();
        for (OrderedItem orderItem : DB.ORDERED_ITEMS) {
            if (orderItem.getOrderId().equals(orderId)) {
                orderItems.add(orderItem);
            }
        }
        return orderItems;
    }

    static Integer getOrderId(Long chatId) {
        Integer orderId = null;
        for (Order order : DB.ORDERS) {
            if (order.getUserChatId().equals(chatId)) {
                orderId = order.getId();
            }
        }
        return orderId;
    }
}