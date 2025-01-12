package org.example.services;

import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import org.example.*;
import org.example.buttons.InlineButton;
import org.example.buttons.ReplyButton;
import org.example.order.OrderStatus;
import org.example.utils.Utils;

import java.io.File;
import java.util.Objects;
import java.util.regex.Pattern;

import static org.example.buttons.ReplyButton.*;
import static org.example.database.DB.*;
import static org.example.utils.Utils.generateCartProductList;
import static org.example.utils.Utils.generateOrderedProductList;

public class TelegramService {
    public static void handle(Update update) {
        try {
            if (update.message() != null) {
                Long chatId = update.message().chat().id();
                String text = update.message().text();
                User user = generateOrCreateUser(chatId);

                if (text != null && text.equals("/start")) {
                    SendMessage sendMessage = new SendMessage(chatId, Constants.SHARE_CONTACT);
                    sendMessage.replyMarkup(ReplyButton.generateContactButton());
                    Main.telegramBot.execute(sendMessage);
                    user.setState(State.GET_CONTACT);

                    System.out.println(update.message().contact());

                } else if (user.getState().equals(State.GET_CONTACT)) {
                    Contact contact = update.message().contact();
                    user.setContact(contact.toString());
                    LocationService.getLocation(user, chatId);


                } else if (user.getState().equals(State.GET_LOCATION)) {
                    LocationService.setLocation(user, update.message().location());

                    SendMessage sendMessage = new SendMessage(chatId, Constants.CHOOSE_MENU);
                    sendMessage.replyMarkup(ReplyButton.generateMainMenuButtons());
                    Main.telegramBot.execute(sendMessage);
                    user.setState(State.CABINET);

                } else if (Objects.equals(text, "Ortga↩️")) {
                    SendMessage sendMessage = new SendMessage(chatId, Constants.PRODUCT_SELECTING_CANCELLED);
                    sendMessage.replyMarkup(generateMainMenuButtons());
                    Main.telegramBot.execute(sendMessage);
                    user.setState(State.CABINET);

                } else if (Objects.equals(text, "🍴Menyu")) {
                    //System.out.println("DB.CATEGORIES - " + CATEGORIES.size() + " ta");
                    //System.out.println("DB.PRODUCTS - " + PRODUCTS.size() + " ta");

                    if (CATEGORIES.isEmpty()) {
                        SendMessage sendMessage = new SendMessage(chatId, "Hozircha bo'lim mavjud emas!");
                        Main.telegramBot.execute(sendMessage);
                    } else {
                        SendMessage sendMessage = new SendMessage(chatId, Constants.CHOOSE_MENU);
                        sendMessage.replyMarkup(generateCategoryButtons());
                        Main.telegramBot.execute(sendMessage);
                        user.setState(State.SHOW_CATEGORIES);
                    }

                } else if (Objects.equals(text, "⚙️Sozlamalar")) {
                    SendMessage sendMessage = new SendMessage(chatId, Constants.TECHNICAL_WORK);
                    sendMessage.replyMarkup(new ReplyKeyboardMarkup("Ortga↩️").resizeKeyboard(true));
                    Main.telegramBot.execute(sendMessage);
                    user.setState(State.CABINET);

                } else if (Objects.equals(text, "🛒Savat")) {
                    String msg;
                    if (CART.isEmpty()) msg = Constants.EMPTY_CART;
                    else msg = generateCartProductList();
                    SendMessage sendMessage = new SendMessage(chatId, msg);
                    if (!CART.isEmpty()){
                        sendMessage.replyMarkup(InlineButton.generateCartButtons());
                    }
                    user.setState(State.COUNTING);
                    Main.telegramBot.execute(sendMessage);

                } else if (Objects.equals(text, "📦Buyurtmalarim")) {
                    Utils.moveCartToOrders(chatId);
                    String msg;
                    if (ORDERS.isEmpty()) {
                        msg = Constants.EMPTY_ORDER;
                        user.setState(State.CABINET);
                    } else msg = generateOrderedProductList(chatId);

                    SendMessage sendMessage = new SendMessage(chatId, msg);
                    if (!ORDERS.isEmpty()) {
                        sendMessage.replyMarkup(InlineButton.generateOrderedProductsButtons());
                    }
                    Main.telegramBot.execute(sendMessage);
                    user.setState(State.COUNTING);

                } else if (user.getState().equals(State.SHOW_CATEGORIES)) {
                    if (text != null) {
                        Category selectedCategory = Utils.getSelectedCategory(text);
                        user.setCategory(selectedCategory);
                        String categoryName = selectedCategory.getName().substring(0, selectedCategory.getName().length()-2);

                        String pathName = "files/%s/%s.png".formatted(categoryName.toLowerCase(), categoryName);
                        SendPhoto sendPhoto = new SendPhoto(chatId, new File(pathName));
                        SendResponse response = Main.telegramBot.execute(sendPhoto);
                        user.setMessageId(response.message().messageId());

                        SendMessage sendMessage = new SendMessage(chatId, Constants.CHOOSE_MENU);
                        sendMessage.replyMarkup(generateProductBtn(selectedCategory));
                        Main.telegramBot.execute(sendMessage);
                        user.setState(State.SHOW_PRODUCT);
                    } else {
                        System.out.println("NULL");
                    }

                } else if (user.getState().equals(State.SHOW_PRODUCT)) {
                    Product selectedProduct = Utils.getSelectedProduct(text);
                    String productName = null;
                    if (selectedProduct != null) {
                        productName = selectedProduct.getName();
                    } else {
                        System.out.println("selectedProduct = NULL");
                    }
                    String categoryName = Utils.getCategoryName(selectedProduct);

                    if (productName != null){
                        SendMessage sendMessage = new SendMessage(chatId, " Buyurtmani rasmiylashtiring!");
                        sendMessage.replyMarkup(new ReplyKeyboardRemove());
                        Main.telegramBot.execute(sendMessage);
                        String pathName = "files/%s/%s.png".formatted(Objects.requireNonNull(categoryName).substring(0, categoryName.length()-2), productName);
                        SendPhoto sendPhoto = new SendPhoto(chatId, new File(pathName))
                                .caption("""
                                Nomi : \t%s
                                Narxi : %d so'm
                                """
                                .formatted(productName, selectedProduct.getPrice()));

                        sendPhoto.replyMarkup(InlineButton.generateProductButton(user, selectedProduct));
                        SendResponse response = Main.telegramBot.execute(sendPhoto);
                        user.setMessageId(response.message().messageId());
                        user.setState(State.COUNTING);
                    } else {
                        SendMessage sendMessage = new SendMessage(chatId, "Hozirda bu mahsulot mavjud emas!");
                        Main.telegramBot.execute(sendMessage);
                    }
                }

                ///INLINE BUTTONS DATA
            } else if (update.callbackQuery() != null) {
                String data = update.callbackQuery().data();
                Long chatId = update.callbackQuery().from().id();
                User user = generateOrCreateUser(chatId);

                if (data.equals("cancelCart") && user.getState().equals(State.COUNTING) && !CART.isEmpty()) {
                    SendMessage sendMessage = new SendMessage(chatId, Constants.CART_CANCELED);
                    CART.clear();
                    sendMessage.replyMarkup(generateCategoryButtons());
                    Main.telegramBot.execute(sendMessage);
                    user.setState(State.CHOOSE_CATEGORY);

                } else if (data.equals("confirmCart") && user.getState().equals(State.COUNTING)) {
                    SendMessage sendMessage = new SendMessage(chatId, Constants.ADD_TO_ORDERS);
                    sendMessage.replyMarkup(generateCategoryButtons());
                    Main.telegramBot.execute(sendMessage);
                    user.setState(State.CHOOSE_CATEGORY);
                    //Utils.moveCartToOrders(user.getChatId());
                    /// Xatolik bor!!!

                } else if (data.equals("placeOrder") && user.getState().equals(State.COUNTING)) {
                    SendMessage sendMessage = new SendMessage(chatId, Constants.ORDER_PLACED);
                    sendMessage.replyMarkup(generateCategoryButtons());
                    ORDERS.get(Utils.getOrderId(chatId) - 1).setStatus(OrderStatus.PREPARING);
                    Main.telegramBot.execute(sendMessage);
                    user.setState(State.CHOOSE_CATEGORY);

                } else if (data.equals("cancelOrder") && user.getState().equals(State.COUNTING)) {
                    SendMessage sendMessage = new SendMessage(chatId, Constants.ORDER_CANCELLED);
                    sendMessage.replyMarkup(generateCategoryButtons());
                    Main.telegramBot.execute(sendMessage);
                    user.setState(State.CHOOSE_CATEGORY);
                    ORDERS.get(Utils.getOrderId(chatId) - 1).setStatus(OrderStatus.CANCELLED);

                } else if (data.equals("backCategories") && user.getState().equals(State.COUNTING)) {
                    SendMessage sendMessage = new SendMessage(chatId, Constants.BACK_TO_CATEGORIES);
                    sendMessage.replyMarkup(generateCategoryButtons());
                    Main.telegramBot.execute(sendMessage);
                    user.setState(State.CHOOSE_CATEGORY);

                } else if (user.getState().equals(State.COUNTING)) {
                    Integer productId = Integer.parseInt(data.substring(data.length() - 1)) - 1;
                    Product selectedProduct = Utils.getSelectedProductById(productId);

                    if (data.startsWith("inBack")) {
                        user.setCounter(1);
                        SendMessage sendMessage = new SendMessage(chatId, Constants.PRODUCT_BACK);
                        Category category = Utils.getCategory(selectedProduct);
                        sendMessage.replyMarkup(ReplyButton.generateProductBtn(Objects.requireNonNull(category)));
                        Main.telegramBot.execute(sendMessage);
                        user.setState(State.SHOW_PRODUCT);

                    } else if (data.startsWith("approved")) {
                        int productCount = user.getCounter();
                        CART.put(selectedProduct, productCount);
                        user.setCounter(1);
                        SendMessage sendMessage = new SendMessage(chatId,
                                "\nMahsulot: " +selectedProduct.getName()
                                        + "\nMiqdori: " + productCount + " ta"
                                        + "\nSavatga qo'shildi ✅"
                        );
                        sendMessage.replyMarkup(new ReplyKeyboardMarkup("Ortga↩️", "🛒Savat").resizeKeyboard(true));
                        Main.telegramBot.execute(sendMessage);
                        user.setState(State.SHOW_PRODUCT);

                    } else {
                        if (user.getCounter() != 0 && data.startsWith("minus")) {
                            user.setCounter(user.getCounter() - 1);
                        } else if (data.startsWith("plus")) {
                            user.setCounter(user.getCounter() + 1);
                        }
                        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup(chatId, user.getMessageId());
                        editMessageReplyMarkup.replyMarkup(InlineButton.generateProductButton(user, selectedProduct));
                        Main.telegramBot.execute(editMessageReplyMarkup);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static User generateOrCreateUser(Long chatId) {
        for (User user : USERS) {
            if (user.getChatId().equals(chatId)) {
                System.out.println("DB: "+ user);
                return user;
            }
        }
        User user = new User();
        user.setChatId(chatId);
        USERS.add(user);
        System.out.println("CREATED: " + user);
        return user;
    }
}