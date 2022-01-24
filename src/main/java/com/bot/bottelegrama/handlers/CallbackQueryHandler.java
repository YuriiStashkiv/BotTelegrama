package com.bot.bottelegrama.handlers;

import com.bot.bottelegrama.cache.Cache;
import com.bot.bottelegrama.domain.BotUser;
import com.bot.bottelegrama.domain.Position;
import com.bot.bottelegrama.messagesender.MessageSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class CallbackQueryHandler implements Handler<CallbackQuery> {

    private final MessageSender messageSender;
    private final Cache<BotUser> cache;

    public CallbackQueryHandler(MessageSender messageSender, Cache<BotUser> cache) {
        this.messageSender = messageSender;
        this.cache = cache;
    }

    private BotUser yourGroupYearAndKurs(CallbackQuery callbackQuery) {
        BotUser user = new BotUser();
        user.setUsername(callbackQuery.getFrom().getUserName());
        user.setId(callbackQuery.getMessage().getChatId());
        user.setPosition(Position.INPUT_KURS);
        return user;
    }

    @Override
    public void choose(CallbackQuery callbackQuery) {
        BotUser user = cache.findBy(callbackQuery.getMessage().getChatId());
        if (user != null) {
            switch (user.getPosition()) {
                //setting kurs and prepering message for setting year
                case INPUT_KURS:
                    user.setKurs(callbackQuery.getData());
                    user.setPosition(Position.INPUT_YEAR);
                    createMessage(callbackQuery,
                            "Обери рік твого навчання...",
                            3, 2,
                            true, "Go To INPUT_KURS",
                            "1-ий рік", "1", "2-ий рік", "2", "3-ій рік", "3", "4-ий рік", "4",
                            "5-ий рік", "5", "6-ий рік", "6");
                    break;
                //setting year and prepering message for setting group
                case INPUT_YEAR:
                    if (callbackQuery.getData().equals("Go To INPUT_KURS")) {
                        user.setPosition(Position.INPUT_KURS);
                        createMessage(callbackQuery,
                                "Гаразд, тоді повторимо все з початку.\n" +
                                        "Спершу виберіть свій курс...",
                                1, 2,
                                false, null,
                                "КН", "КН", "КБ", "КБ");
                    } else {
                        user.setYear(callbackQuery.getData());
                        user.setPosition(Position.INPUT_GROUP);
                        createMessage(callbackQuery,
                                "Обери номер своєї групи...",
                                1, 2,
                                true, "Go To INPUT_YEAR",
                                "1-а група", "1", "2-а група", "2");
                    }
                    break;
                //setting group and prepering message for checking is it right
                case INPUT_GROUP:
                    if (callbackQuery.getData().equals("Go To INPUT_YEAR")) {
                        user.setPosition(Position.INPUT_YEAR);
                        createMessage(callbackQuery,
                                "Обери рік твого навчання...",
                                3, 2,
                                true, "Go To INPUT_KURS",
                                "1-ий рік", "1", "2-ий рік", "2", "3-ій рік", "3", "4-ий рік", "4",
                                "5-ий рік", "5", "6-ий рік", "6");
                    } else {
                        user.setGroup(callbackQuery.getData());
                        user.setPosition(Position.NONE);
                        createMessage(callbackQuery,
                                "Твоя група - це " + user.getKurs() + user.getYear()
                                + user.getGroup() + "\n" +
                                "Правильно?",
                                1, 2,
                                false, null,
                                "Так", "Yes", "Ні", "No");
                    }
                    break;
                case NONE:
                    //checking if reg is correct
                    switch (callbackQuery.getData()) {
                        case "Yes":
                            user.setYourKurs(user.getKurs() + user.getYear()
                                    + user.getGroup());
                            createMessage(callbackQuery,
                                    "Реєстрація успішна!\u263A");

                            break;
                        //if no we asking if user want to do reg again
                        case "No":
                            createMessage(callbackQuery, "Чи не бажаєте пройти реєстарцію ще раз?",
                                    1, 2,
                                    false, null,
                                    "Так", "yes", "Ні", "no");
                            break;
                    }

                    //if user want to do reg again after checking prev check
                    switch (callbackQuery.getData()) {
                        case "yes":
                            user.setPosition(Position.INPUT_KURS);
                            createMessage(callbackQuery,
                                    "Гаразд, тоді повторимо все з початку.\n" +
                                            "Спершу виберіть свій курс...",
                                    1, 2,
                                    false, null,
                                    "КН", "КН", "КБ", "КБ");
                            break;
                        case "no":
                            createMessage(callbackQuery, "Тоді бувай, " + user.getUsername() + ", гарного дня!");
                            cache.remove(user);
                            break;
                    }

                    //if user want to log out
                    switch (callbackQuery.getData()) {
                        case "logoutYes":
                            createMessage(callbackQuery,
                                    "Тоді до нових зустрічей "
                                    + user.getUsername() + ", я вас не забуду...");
                            cache.remove(user);
                            try {
                                TimeUnit.SECONDS.sleep(5);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            createMessage(callbackQuery,
                                    "Данні користувача видаленні.");
                            break;
                        case "logoutNo":
                            createMessage(callbackQuery,
                                    "Гаразд тоді!");
                            break;
                    }
                    break;
            }
        } else {
            //first checking if user want to do reg
            switch (callbackQuery.getData()) {
                case "Agree":
                    cache.add(yourGroupYearAndKurs(callbackQuery));
                    createMessage(callbackQuery, "Гаразд, тоді почнемо.\n" +
                                    "Спершу виберіть свій курс...",
                            1, 2, false, null,
                            "КН", "КН", "КБ", "КБ");
                    break;
                case "Disagree":
                    createMessage(callbackQuery, "Тоді я працювати не можу, гарного дня, " +
                            callbackQuery.getFrom().getUserName() + "!");
                    break;
            }
        }
    }

    private void createMessage(CallbackQuery callbackQuery, String text) {
        messageSender.sendEditMessage(
                EditMessageText.builder()
                        .chatId(String.valueOf(callbackQuery.getMessage().getChatId()))
                        .messageId(callbackQuery.getMessage().getMessageId())
                        .text(text)
                        .build()
        );
    }


    private void createMessage(CallbackQuery callbackQuery, String text,
                               int columns, int rows, boolean backButton, String dataForBack,
                               String... buttonNameAndData) {
        if (backButton) {
            int a = 0;
            var editMessageText = new EditMessageText();
            editMessageText.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
            editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
            editMessageText.setText(text);

            var inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> column = new ArrayList<>();

            for (int ab = columns; ab > 0; ab--) {
                List<InlineKeyboardButton> row = new ArrayList<>();
                for (int ad = rows; ad > 0; ad--) {
                    var inlineKeyboardButton = new InlineKeyboardButton();
                    inlineKeyboardButton.setText(buttonNameAndData[a]);
                    inlineKeyboardButton.setCallbackData(buttonNameAndData[a + 1]);
                    a = a + 2;
                    row.add(inlineKeyboardButton);
                }
                column.add(row);
            }

            var inlineKeyboardButton = new InlineKeyboardButton();
            List<InlineKeyboardButton> row = new ArrayList<>();
            inlineKeyboardButton.setText("\u2B05 Go back");
            inlineKeyboardButton.setCallbackData(dataForBack);
            row.add(inlineKeyboardButton);
            column.add(row);

            inlineKeyboardMarkup.setKeyboard(column);
            editMessageText.setReplyMarkup(inlineKeyboardMarkup);
            messageSender.sendEditMessage(editMessageText);
        } else {
            int b = 0;
            var editMessageText = new EditMessageText();
            editMessageText.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
            editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
            editMessageText.setText(text);

            var inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> column = new ArrayList<>();

            for (int ab = columns; ab > 0; ab--) {
                List<InlineKeyboardButton> row = new ArrayList<>();
                for (int ad = rows; ad > 0; ad--) {
                    var inlineKeyboardButton = new InlineKeyboardButton();
                    inlineKeyboardButton.setText(buttonNameAndData[b]);
                    inlineKeyboardButton.setCallbackData(buttonNameAndData[b + 1]);
                    b = b + 2;
                    row.add(inlineKeyboardButton);
                }
                column.add(row);
            }
            inlineKeyboardMarkup.setKeyboard(column);
            editMessageText.setReplyMarkup(inlineKeyboardMarkup);
            messageSender.sendEditMessage(editMessageText);
        }
    }
}