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
            editMessageText.setReplyMarkup(
                    InlineKeyboardMarkup.builder()
                            .keyboardRow(Collections.singletonList(
                                    InlineKeyboardButton.builder()
                                            .text("1-ий рік")
                                            .callbackData("1")
                                            .build()
                            ))
                            .keyboardRow(Collections.singletonList(
                                    InlineKeyboardButton.builder()
                                            .text("2-ий рік")
                                            .callbackData("2")
                                            .build()
                            ))
                            .keyboardRow(Collections.singletonList(
                                    InlineKeyboardButton.builder()
                                            .text("3-ий рік")
                                            .callbackData("3")
                                            .build()
                            ))
                            .keyboardRow(Collections.singletonList(
                                    InlineKeyboardButton.builder()
                                            .text("4-ий рік")
                                            .callbackData("4")
                                            .build()
                            ))
                            .keyboardRow(Collections.singletonList(
                                    InlineKeyboardButton.builder()
                                            .text("5-ий рік")
                                            .callbackData("5")
                                            .build()
                            ))
                            .keyboardRow(Collections.singletonList(
                                    InlineKeyboardButton.builder()
                                            .text("6-ий рік")
                                            .callbackData("6")
                                            .build()
                            ))
                            .build());
            messageSender.sendEditMessage(editMessageText);
        }
    }
}
