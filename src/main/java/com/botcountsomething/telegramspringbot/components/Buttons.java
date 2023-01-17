package com.botcountsomething.telegramspringbot.components;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class Buttons {
    private static final InlineKeyboardButton START_BUTTON = new InlineKeyboardButton("Start");
    private static final InlineKeyboardButton HELP_BUTTON = new InlineKeyboardButton("Help");

    public static InlineKeyboardMarkup inlineMarkup(){
        START_BUTTON.setCallbackData("/start");
        HELP_BUTTON.setCallbackData("/help");

        List<InlineKeyboardButton> rowInLine = List.of(START_BUTTON, HELP_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInLine);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rowsInLine);
        return markup;
    }
}
