package org.example.services;

import com.pengrad.telegrambot.model.Location;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.Main;
import org.example.State;
import org.example.User;
import org.example.buttons.ReplyButton;

import static org.example.database.DB.LOCATIONS;


public class LocationService {

    public static void getLocation(User user, Long chatId) {
        user.setState(State.GET_LOCATION);
        SendMessage sendMessage = new SendMessage(chatId, Constants.SHARE_LOCATION);
        sendMessage.replyMarkup(ReplyButton.generateLocationButton());
        Main.telegramBot.execute(sendMessage);
    }

    public static void setLocation(User user, Location location){
        user.setLongitude(location.longitude());
        user.setLatitude(location.latitude());
        LOCATIONS.add(location);
    }
}