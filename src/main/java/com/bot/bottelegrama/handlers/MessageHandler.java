package com.bot.bottelegrama.handlers;


import com.bot.bottelegrama.cache.Cache;
import com.bot.bottelegrama.domain.BotUser;
import com.bot.bottelegrama.messagesender.MessageSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class MessageHandler implements Handler<Message> {

    private final MessageSender messageSender;
    private final Cache<BotUser> cache;

    public MessageHandler(MessageSender messageSender, Cache<BotUser> cache) {
        this.messageSender = messageSender;
        this.cache = cache;
    }

    @Override
    public void choose(Message message) {
        BotUser user = cache.findBy(message.getChatId());
        if (message.hasText()) {

            if (message.getText().equals("/start")) {
                var markup = new ReplyKeyboardMarkup();
                var keyboardRows = new ArrayList<KeyboardRow>();
                KeyboardRow row1 = new KeyboardRow();
                KeyboardRow row2 = new KeyboardRow();
                row1.add("/register");
                row1.add("/logout");
                row2.add("All about me\uD83E\uDD2F");
                keyboardRows.add(row1);
                keyboardRows.add(row2);
                markup.setKeyboard(keyboardRows);
                markup.setResizeKeyboard(true);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Вітаю, " + message.getFrom().getUserName() + "!\n"
                        + "Я можу тобі надіслати розклад заннять з твоєї групи\n"
                        + "Для старту нічого особливого не треба, "
                        + "просто нажими на кнопку, або введи команду /register і ми почнемо");
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                sendMessage.setReplyMarkup(markup);
                messageSender.sendMessage(sendMessage);
            }

            if (message.getText().equals("/register")) {
                if (user != null) {
                    messageSender.sendMessage(
                            SendMessage.builder()
                                    .chatId(String.valueOf(message.getChatId()))
                                    .text(user.getUsername()+", ти вже зареєструвався, навіщо ти робиш це знову")
                                    .build()
                    );
                } else {
                    messageSender.sendMessage(
                            SendMessage.builder()
                                    .text("Для старту треба дати згоду на обробку ваших повідомлень")
                                    .chatId(String.valueOf(message.getChatId()))
                                    .replyMarkup(InlineKeyboardMarkup.builder()
                                            .keyboardRow(List.of(
                                                    InlineKeyboardButton.builder()
                                                            .text("Я згоден(згодна)")
                                                            .callbackData("Agree")
                                                            .build()))
                                            .keyboardRow(List.of(
                                                    InlineKeyboardButton.builder()
                                                            .text("Я не згоден(згодна)")
                                                            .callbackData("Disagree")
                                                            .build()))
                                            .build())
                                    .build());
                }
            }

            if (message.getText().equals("/logout")) {
                if (user != null) {
                    messageSender.sendMessage(
                            SendMessage.builder()
                                    .chatId(String.valueOf(message.getChatId()))
                                    .text("Ви впевненні? Ця дія є незворотня")
                                    .replyMarkup(InlineKeyboardMarkup.builder()
                                            .keyboardRow(List.of(
                                                    InlineKeyboardButton.builder()
                                                            .text("Так")
                                                            .callbackData("logoutYes")
                                                            .build(),
                                                    InlineKeyboardButton.builder()
                                                            .text("Ні")
                                                            .callbackData("logoutNo")
                                                            .build()
                                            ))
                                            .build())
                                    .build());
                } else {
                    messageSender.sendMessage(
                            SendMessage.builder()
                                    .chatId(String.valueOf(message.getChatId()))
                                    .text("Ви не можете вийти, бо ви не ввійшли в систему.\n" +
                                            "Нагадую, щоб ввійти просто введіть команду /register")
                                    .build()
                    );
                }
            }

            if (message.getText().equals("All about me\uD83E\uDD2F")) {
                if (user != null) {
                    messageSender.sendMessage(
                            SendMessage.builder()
                                    .chatId(String.valueOf(message.getChatId()))
                                    .text("Ваша група " + user.getYourKurs())
                                    .build());
                    messageSender.sendMessage(
                            SendMessage.builder()
                                    .chatId(String.valueOf(message.getChatId()))
                                    .text("Ваш нікнейм в телеграмі " + user.getUsername())
                                    .build());
                } else {
                    messageSender.sendMessage(
                            SendMessage.builder()
                                    .chatId(String.valueOf(message.getChatId()))
                                    .text("Спочатку пройди реєстаріцію за допомоги команди /register")
                                    .build()
                    );
                }
            }
        }
    }
}
