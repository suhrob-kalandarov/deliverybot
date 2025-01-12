package org.example.buttons;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import org.example.User;
import org.example.services.Constants;
import org.example.Product;

public class InlineButton {

    public static InlineKeyboardMarkup generateMiniBigButton(User user, Product selectedProduct) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton("Mini").callbackData("mini" + selectedProduct.getId()),
                new InlineKeyboardButton("Big").callbackData("big" + selectedProduct.getId())
        );
        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton("Orqaga↩️").callbackData("inBack_" + selectedProduct.getId())
        );
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup generateProductButton(User user, Product selectedProduct) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton("-").callbackData("minus" + selectedProduct.getId()),
                new InlineKeyboardButton(user.getCounter().toString()).callbackData("zero" + selectedProduct.getId()),
                new InlineKeyboardButton("+").callbackData("plus" + selectedProduct.getId())
        );
        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton(Constants.ADD_TO_CART).callbackData("approved_" + selectedProduct.getId())
        );
        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton("Orqaga↩️").callbackData("inBack_" + selectedProduct.getId())
        );
        return inlineKeyboardMarkup;
    }

    public static Keyboard generateCartButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton("Tasdiqlash✅").callbackData("confirmCart"),
                new InlineKeyboardButton("Bekor qilish❌").callbackData("cancelCart")
        );
        return inlineKeyboardMarkup;
    }

    public static Keyboard generateOrderedProductsButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton("💳Buyurtma berish").callbackData("placeOrder"),
                new InlineKeyboardButton("❌Hammasini bekor qilish").callbackData("cancelOrder")
        );
        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton("Orqaga↩️").callbackData("backCategories")
        );
        return inlineKeyboardMarkup;

    }
}