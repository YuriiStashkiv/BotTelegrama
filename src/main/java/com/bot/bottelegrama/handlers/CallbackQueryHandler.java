package com.bot.bottelegrama.handlers;

import com.bot.bottelegrama.messagesender.MessageSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class CallbackQueryHandler implements Handler<CallbackQuery>{

    private final MessageSender messageSender;

    public CallbackQueryHandler(MessageSender messageSender) {
        this.messageSender = messageSender;
    }


    @Override
    public void choose(CallbackQuery callbackQuery) {
        if (callbackQuery.getData().equals("IT-T")) {
            Integer messageId = callbackQuery.getMessage().getMessageId();
            var editMessageText = new EditMessageText();
            editMessageText.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
            editMessageText.setMessageId(messageId);
            editMessageText.setText("Обери рік твого навчання...");
            var inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            keyboard.add(
                    Collections.singletonList(
                            InlineKeyboardButton.builder()
                                    .text("1-ий рік")
                                    .callbackData("One")
                                    .build()
                    ));
            keyboard.add(
                    Collections.singletonList(
                            InlineKeyboardButton.builder()
                                    .text("2-ий рік")
                                    .callbackData("Two")
                                    .build()
                    ));
            keyboard.add(
                    Collections.singletonList(
                            InlineKeyboardButton.builder()
                                    .text("3-ій рік")
                                    .callbackData("Three")
                                    .build()
                    ));
            keyboard.add(
                    Collections.singletonList(
                            InlineKeyboardButton.builder()
                                    .text("4-ий рік")
                                    .callbackData("Four")
                                    .build()
                    ));
            keyboard.add(
                    Collections.singletonList(
                            InlineKeyboardButton.builder()
                                    .text("5-ий рік")
                                    .callbackData("Five")
                                    .build()
                    ));
            keyboard.add(
                    Collections.singletonList(
                            InlineKeyboardButton.builder()
                                    .text("6-ий рік")
                                    .callbackData("Six")
                                    .build()
                    ));
            inlineKeyboardMarkup.setKeyboard(keyboard);
            editMessageText.setReplyMarkup(inlineKeyboardMarkup);
            messageSender.sendEditMessage(editMessageText);
        }
    }
}
