package com.bot.bottelegrama.handlers;

import com.bot.bottelegrama.messagesender.MessageSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class MessageHandler implements Handler<Message> {

    private final MessageSender messageSender;

    public MessageHandler(MessageSender messageSender) {
        this.messageSender = messageSender;
    }


    @Override
    public void choose(Message message) {
        if (message.hasText()){
            if(message.getText().equals("/start")){
                var markup = new ReplyKeyboardMarkup();
                var keyboardRows = new ArrayList<KeyboardRow>();
                KeyboardRow row1 = new KeyboardRow();
                KeyboardRow row2 = new KeyboardRow();
                row1.add("/givemelessens");
                keyboardRows.add(row1);
                markup.setKeyboard(keyboardRows);
                markup.setResizeKeyboard(true);
                markup.setOneTimeKeyboard(true);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Вітаю, "+ message.getFrom().getUserName() +"!\n"
                        + "Я можу тобі надіслати розклад заннять з твоєї групи\n"
                        + "Для старту нічого особливого не треба, " +
                        "просто нажими на кнопку, або введи команду /givemelessens і ми почнемо");
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                sendMessage.setReplyMarkup(markup);
                messageSender.sendMessage(sendMessage);
            }
            if (message.getText().equals("/givemelessens")) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                sendMessage.setText("Вибери свій курс...");
                var inlineKeyboardMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
                keyboard.add(
                        Collections.singletonList(
                                InlineKeyboardButton.builder()
                                        .text("КН")
                                        .callbackData("IT-T")
                                        .build()
                        ));
                keyboard.add(
                        Collections.singletonList(
                                InlineKeyboardButton.builder()
                                        .text("КБ")
                                        .callbackData("CS")
                                        .build()
                        ));
                inlineKeyboardMarkup.setKeyboard(keyboard);
                sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                messageSender.sendMessage(sendMessage);
            }
        }
    }
}
