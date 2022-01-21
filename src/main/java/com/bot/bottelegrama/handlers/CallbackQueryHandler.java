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

import java.util.Collections;
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
                    Integer messageId = callbackQuery.getMessage().getMessageId();
                    var editMessageText = new EditMessageText();
                    editMessageText.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
                    editMessageText.setMessageId(messageId);
                    editMessageText.setText("Обери рік твого навчання...");
                    editMessageText.setReplyMarkup(
                            InlineKeyboardMarkup.builder()
                                    .keyboardRow(List.of(
                                            InlineKeyboardButton.builder()
                                                    .text("1-ий рік")
                                                    .callbackData("1")
                                                    .build(),
                                            InlineKeyboardButton.builder()
                                                    .text("2-ий рік")
                                                    .callbackData("2")
                                                    .build()
                                    ))
                                    .keyboardRow(List.of(
                                            InlineKeyboardButton.builder()
                                                    .text("3-ий рік")
                                                    .callbackData("3")
                                                    .build(),
                                            InlineKeyboardButton.builder()
                                                    .text("4-ий рік")
                                                    .callbackData("4")
                                                    .build()
                                    ))
                                    .keyboardRow(List.of(
                                            InlineKeyboardButton.builder()
                                                    .text("5-ий рік")
                                                    .callbackData("5")
                                                    .build(),
                                            InlineKeyboardButton.builder()
                                                    .text("6-ий рік")
                                                    .callbackData("6")
                                                    .build()
                                    ))
                                    .keyboardRow(List.of(
                                            InlineKeyboardButton.builder()
                                                    .text("\u2B05 Go back")
                                                    .callbackData("Go To INPUT_KURS")
                                                    .build()
                                    ))
                                    .build());
                    messageSender.sendEditMessage(editMessageText);
                    break;
                //setting year and prepering message for setting group
                case INPUT_YEAR:
                    if (callbackQuery.getData().equals("Go To INPUT_KURS")) {
                        user.setPosition(Position.INPUT_KURS);
                        Integer messageId5 = callbackQuery.getMessage().getMessageId();
                        var editMessageText5 = new EditMessageText();
                        editMessageText5.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
                        editMessageText5.setMessageId(messageId5);
                        editMessageText5.setText("Виберіть свій курс...");
                        editMessageText5.setReplyMarkup(
                                InlineKeyboardMarkup.builder()
                                        .keyboardRow(List.of(
                                                InlineKeyboardButton.builder()
                                                        .text("КН")
                                                        .callbackData("КН")
                                                        .build()
                                        ))
                                        .keyboardRow(List.of(
                                                InlineKeyboardButton.builder()
                                                        .text("КБ")
                                                        .callbackData("КБ")
                                                        .build()
                                        ))
                                        .build());
                        messageSender.sendEditMessage(editMessageText5);
                    } else {
                        user.setYear(callbackQuery.getData());
                        user.setPosition(Position.INPUT_GROUP);
                        Integer messageId1 = callbackQuery.getMessage().getMessageId();
                        var editMessageText1 = new EditMessageText();
                        editMessageText1.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
                        editMessageText1.setMessageId(messageId1);
                        editMessageText1.setText("Обери номер своєї групи...");
                        editMessageText1.setReplyMarkup(
                                InlineKeyboardMarkup.builder()
                                        .keyboardRow(List.of(
                                                InlineKeyboardButton.builder()
                                                        .text("1-а група")
                                                        .callbackData("1")
                                                        .build(),
                                                InlineKeyboardButton.builder()
                                                        .text("2-а група")
                                                        .callbackData("2")
                                                        .build()
                                        ))
                                        .keyboardRow(List.of(
                                                InlineKeyboardButton.builder()
                                                        .text("\u2B05 Go back")
                                                        .callbackData("Go To INPUT_YEAR")
                                                        .build()
                                        ))
                                        .build());
                        messageSender.sendEditMessage(editMessageText1);
                    }
                    break;
                //setting group and prepering message for checking is it right
                case INPUT_GROUP:
                    if (callbackQuery.getData().equals("Go To INPUT_YEAR")) {
                        user.setPosition(Position.INPUT_YEAR);
                        Integer messageId4 = callbackQuery.getMessage().getMessageId();
                        var editMessageText4 = new EditMessageText();
                        editMessageText4.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
                        editMessageText4.setMessageId(messageId4);
                        editMessageText4.setText("Обери рік твого навчання...");
                        editMessageText4.setReplyMarkup(
                                InlineKeyboardMarkup.builder()
                                        .keyboardRow(List.of(
                                                InlineKeyboardButton.builder()
                                                        .text("1-ий рік")
                                                        .callbackData("1")
                                                        .build(),
                                                InlineKeyboardButton.builder()
                                                        .text("2-ий рік")
                                                        .callbackData("2")
                                                        .build()
                                        ))
                                        .keyboardRow(List.of(
                                                InlineKeyboardButton.builder()
                                                        .text("3-ий рік")
                                                        .callbackData("3")
                                                        .build(),
                                                InlineKeyboardButton.builder()
                                                        .text("4-ий рік")
                                                        .callbackData("4")
                                                        .build()
                                        ))
                                        .keyboardRow(List.of(
                                                InlineKeyboardButton.builder()
                                                        .text("5-ий рік")
                                                        .callbackData("5")
                                                        .build(),
                                                InlineKeyboardButton.builder()
                                                        .text("6-ий рік")
                                                        .callbackData("6")
                                                        .build()
                                        ))
                                        .keyboardRow(List.of(
                                                InlineKeyboardButton.builder()
                                                        .text("\u2B05 Go back")
                                                        .callbackData("Go To INPUT_KURS")
                                                        .build()
                                        ))
                                        .build());
                        messageSender.sendEditMessage(editMessageText4);
                    } else {
                        user.setGroup(callbackQuery.getData());
                        user.setPosition(Position.NONE);
                        Integer messageId2 = callbackQuery.getMessage().getMessageId();
                        var editMessageText2 = new EditMessageText();
                        editMessageText2.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
                        editMessageText2.setMessageId(messageId2);
                        editMessageText2.setText("Твоя група - це " + user.getKurs() + user.getYear()
                                + user.getGroup() + "\n" +
                                "Правильно?");
                        editMessageText2.setReplyMarkup(InlineKeyboardMarkup.builder()
                                .keyboardRow(List.of(
                                        InlineKeyboardButton.builder()
                                                .text("Так")
                                                .callbackData("Yes")
                                                .build(),
                                        InlineKeyboardButton.builder()
                                                .text("Ні")
                                                .callbackData("No")
                                                .build()))
                                .build());
                        messageSender.sendEditMessage(editMessageText2);
                    }
                    break;
                case NONE:
                    //checking if reg is correct
                    switch (callbackQuery.getData()) {
                        case "Yes":
                            Integer messageId5 = callbackQuery.getMessage().getMessageId();
                            user.setYourKurs(user.getKurs() + user.getYear()
                                    + user.getGroup());
                            messageSender.sendEditMessage(
                                    EditMessageText.builder()
                                            .chatId(String.valueOf(callbackQuery.getMessage().getChatId()))
                                            .messageId(messageId5)
                                            .text("Реєстрація успішна!\u263A")
                                            .build());
                            break;
                        //if no we asking if user want to do reg again
                        case "No":
                            Integer messageId3 = callbackQuery.getMessage().getMessageId();
                            var editMessageText3 = new EditMessageText();
                            editMessageText3.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
                            editMessageText3.setMessageId(messageId3);
                            editMessageText3.setText("Чи не бажаєте пройти реєстарцію ще раз?");
                            editMessageText3.setReplyMarkup(
                                    InlineKeyboardMarkup.builder()
                                            .keyboardRow(List.of(
                                                    InlineKeyboardButton.builder()
                                                            .text("Так")
                                                            .callbackData("yes")
                                                            .build(),
                                                    InlineKeyboardButton.builder()
                                                            .text("Ні")
                                                            .callbackData("no")
                                                            .build()
                                            )).build());
                            messageSender.sendEditMessage(editMessageText3);
                            break;
                    }

                    //if user want to do reg again after checking prev check
                    switch (callbackQuery.getData()) {
                        case "yes":
                            user.setPosition(Position.INPUT_KURS);
                            Integer messageId3 = callbackQuery.getMessage().getMessageId();
                            var editMessageText3 = new EditMessageText();
                            editMessageText3.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
                            editMessageText3.setMessageId(messageId3);
                            editMessageText3.setText("Гаразд, тоді давай повторимо з початку.\n" +
                                    "Спершу виберіть свій курс...");
                            editMessageText3.setReplyMarkup(
                                    InlineKeyboardMarkup.builder()
                                            .keyboardRow(List.of(
                                                    InlineKeyboardButton.builder()
                                                            .text("КН")
                                                            .callbackData("КН")
                                                            .build()
                                            ))
                                            .keyboardRow(List.of(
                                                    InlineKeyboardButton.builder()
                                                            .text("КБ")
                                                            .callbackData("КБ")
                                                            .build()
                                            ))
                                            .build());
                            messageSender.sendEditMessage(editMessageText3);
                            break;
                        case "no":
                            Integer messageId4 = callbackQuery.getMessage().getMessageId();
                            var editMessageText4 = new EditMessageText();
                            editMessageText4.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
                            editMessageText4.setMessageId(messageId4);
                            editMessageText4.setText("Тоді бувай, " +
                                    user.getUsername() + ", гарного дня!");
                            messageSender.sendEditMessage(editMessageText4);
                            cache.remove(user);
                            break;
                    }

                    //if user want to log out
                    switch (callbackQuery.getData()){
                        case "logoutYes":
                            messageSender.sendEditMessage(
                                    EditMessageText.builder()
                                            .chatId(String.valueOf(callbackQuery.getMessage().getChatId()))
                                            .messageId(callbackQuery.getMessage().getMessageId())
                                            .text("Тоді до нових зустрічей "+user.getUsername()+", я вас не забуду...")
                                            .build()
                            );
                            cache.remove(user);
                            try {
                                TimeUnit.SECONDS.sleep(5);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            messageSender.sendEditMessage(
                                    EditMessageText.builder()
                                            .chatId(String.valueOf(callbackQuery.getMessage().getChatId()))
                                            .messageId(callbackQuery.getMessage().getMessageId())
                                            .text("Данні користувача видаленні.")
                                            .build()
                            );
                            break;
                        case "logoutNo":
                            break;
                    }
                    break;
            }
        } else {
            //first checking if user want to do reg
            switch (callbackQuery.getData()) {
                case "Agree":
                    cache.add(yourGroupYearAndKurs(callbackQuery));
                    Integer messageId = callbackQuery.getMessage().getMessageId();
                    var editMessageText = new EditMessageText();
                    editMessageText.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
                    editMessageText.setMessageId(messageId);
                    editMessageText.setText("Гаразд, тоді почнемо.\n" +
                            "Спершу виберіть свій курс...");
                    editMessageText.setReplyMarkup(
                            InlineKeyboardMarkup.builder()
                                    .keyboardRow(List.of(
                                            InlineKeyboardButton.builder()
                                                    .text("КН")
                                                    .callbackData("КН")
                                                    .build()
                                    ))
                                    .keyboardRow(List.of(
                                            InlineKeyboardButton.builder()
                                                    .text("КБ")
                                                    .callbackData("КБ")
                                                    .build()
                                    ))
                                    .build());
                    messageSender.sendEditMessage(editMessageText);
                    break;
                case "Disagree":
                    Integer messageId2 = callbackQuery.getMessage().getMessageId();
                    var editMessageText2 = new EditMessageText();
                    editMessageText2.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
                    editMessageText2.setMessageId(messageId2);
                    editMessageText2.setText("Тоді я працювати не можу, гарного дня, " +
                            callbackQuery.getFrom().getUserName() + "!");
                    messageSender.sendEditMessage(editMessageText2);
                    break;
            }
        }
    }
}