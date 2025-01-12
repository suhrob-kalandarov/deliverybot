package org.example.buttons;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import org.example.Category;
import org.example.database.DB;
import org.example.services.Constants;
import org.example.Product;
import org.example.utils.Utils;

public class ReplyButton {

    public static Keyboard generateContactButton() {
        return new ReplyKeyboardMarkup(new KeyboardButton(Constants.CONTACT_BUTTON).requestContact(true)).resizeKeyboard(true);
    }

    public static Keyboard generateLocationButton() {
        return new ReplyKeyboardMarkup(new KeyboardButton(Constants.LOCATION_BUTTON).requestLocation(true)).resizeKeyboard(true);
    }

    public static Keyboard generateMainMenuButtons() {
        String[][] strings = new String[][]{
                {"🍴Menyu", "🛒Savat"},
                {"📦Buyurtmalarim", "⚙️Sozlamalar"}
        };
        return new ReplyKeyboardMarkup(strings).resizeKeyboard(true);
    }

    public static Keyboard generateCategoryButtons() {
        if (DB.CATEGORIES.isEmpty()) {
            return new ReplyKeyboardMarkup(new String[][]{{"Hozircha bo'lim mavjud emas"}}).resizeKeyboard(true);
        }

        int rows = (int) Math.ceil(DB.CATEGORIES.size() / 2.0);
        String[][] buttons = new String[rows][2];
        String[] btn = {"Ortga↩️", "🛒Savat"};

        int row = 0, col = 0;
        for (Category category : DB.CATEGORIES) {
            buttons[row][col++] = category.getName();
            if (col == 2) {
                row++;
                col = 0;
            }
        }
        return new ReplyKeyboardMarkup(buttons).addRow(btn).resizeKeyboard(true);
    }

    public static Keyboard generateProductBtn(Category category) {
        //Integer categoryId = Utils.getCategoryId(categoryName);
        Integer productAmount = Utils.getProductAmount(category.getId());
        String[] btn = {"Ortga↩️", "🛒Savat"};
        String[][] buttons = new String[(int) Math.ceil(productAmount / 2.0)][2];
        int row = 0;
        int column = 0;
        for (Product product : DB.PRODUCTS) {
            if (product.getCategoryId().equals(category.getId())) {
                if (column == 2) {
                    column = 0;
                    row += 1;
                }
                buttons[row][column] = product.getName();
                column++;
            }
        }
        return new ReplyKeyboardMarkup(buttons).addRow(btn).resizeKeyboard(true);
    }
}