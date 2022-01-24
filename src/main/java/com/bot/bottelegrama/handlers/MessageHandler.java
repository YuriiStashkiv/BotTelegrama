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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
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
                createMessage(
                        message,
                        "Вітаю, " + message.getFrom().getUserName() + "!\n"
                                + "Я можу тобі надіслати розклад заннять з твоєї групи\n"
                                + "Для старту нічого особливого не треба, "
                                + "просто нажими на кнопку, або введи команду /register і ми почнемо",
                        false, 2, 2, null,
                        "/register", "/logout", "All about me\uD83E\uDD2F");
//                var markup = new ReplyKeyboardMarkup();
//                var keyboardRows = new ArrayList<KeyboardRow>();
//                KeyboardRow row1 = new KeyboardRow();
//                KeyboardRow row2 = new KeyboardRow();
//                row1.add("/register");
//                row1.add("/logout");
//                row2.add("All about me\uD83E\uDD2F");
//                keyboardRows.add(row1);
//                keyboardRows.add(row2);
//                markup.setKeyboard(keyboardRows);
//                markup.setResizeKeyboard(true);
//                SendMessage sendMessage = new SendMessage();
//                sendMessage.setText("Вітаю, " + message.getFrom().getUserName() + "!\n"
//                        + "Я можу тобі надіслати розклад заннять з твоєї групи\n"
//                        + "Для старту нічого особливого не треба, "
//                        + "просто нажими на кнопку, або введи команду /register і ми почнемо");
//                sendMessage.setChatId(String.valueOf(message.getChatId()));
//                sendMessage.setReplyMarkup(markup);
//                messageSender.sendMessage(sendMessage);
            }

            if (message.getText().equals("/register")) {
                if (user != null) {
                    createMessage(message,
                            user.getUsername() + ", ти вже зареєструвався, навіщо ти робиш це знову?");
//                    messageSender.sendMessage(
//                            SendMessage.builder()
//                                    .chatId(String.valueOf(message.getChatId()))
//                                    .text(user.getUsername() + ", ти вже зареєструвався, навіщо ти робиш це знову")
//                                    .build()
//                    );
                } else {
                    createMessage(message,
                            "Для старту треба дати згоду на обробку ваших повідомлень",
                            true, 2, 1, null,
                            "Я згоден(згодна)", "Agree", "Я не згоден(згодна)", "Disagree");
//                    messageSender.sendMessage(
//                            SendMessage.builder()
//                                    .text("Для старту треба дати згоду на обробку ваших повідомлень")
//                                    .chatId(String.valueOf(message.getChatId()))
//                                    .replyMarkup(InlineKeyboardMarkup.builder()
//                                            .keyboardRow(List.of(
//                                                    InlineKeyboardButton.builder()
//                                                            .text("Я згоден(згодна)")
//                                                            .callbackData("Agree")
//                                                            .build()))
//                                            .keyboardRow(List.of(
//                                                    InlineKeyboardButton.builder()
//                                                            .text("Я не згоден(згодна)")
//                                                            .callbackData("Disagree")
//                                                            .build()))
//                                            .build())
//                                    .build());
                }
            }

            if (message.getText().equals("/logout")) {
                if (user != null) {
                    createMessage(message,
                            "Ви впевненні? Ця дія є незворотня",
                            true, 2, 1, null,
                            "Так", "logoutYes", "Ні", "logoutNo");
//                    messageSender.sendMessage(
//                            SendMessage.builder()
//                                    .chatId(String.valueOf(message.getChatId()))
//                                    .text("Ви впевненні? Ця дія є незворотня")
//                                    .replyMarkup(InlineKeyboardMarkup.builder()
//                                            .keyboardRow(List.of(
//                                                    InlineKeyboardButton.builder()
//                                                            .text("Так")
//                                                            .callbackData("logoutYes")
//                                                            .build(),
//                                                    InlineKeyboardButton.builder()
//                                                            .text("Ні")
//                                                            .callbackData("logoutNo")
//                                                            .build()
//                                            ))
//                                            .build())
//                                    .build());
                } else {
                    createMessage(message,
                            "Ви не можете вийти, бо ви не ввійшли в систему.\n" +
                                    "Нагадую, щоб ввійти просто введіть команду /register");
//                    messageSender.sendMessage(
//                            SendMessage.builder()
//                                    .chatId(String.valueOf(message.getChatId()))
//                                    .text("Ви не можете вийти, бо ви не ввійшли в систему.\n" +
//                                            "Нагадую, щоб ввійти просто введіть команду /register")
//                                    .build()
//                    );
                }
            }

            if (message.getText().equals("All about me\uD83E\uDD2F")) {
                if (user != null) {
                    createMessage(message,
                            "Ваша група " + user.getYourKurs());
//                    messageSender.sendMessage(
//                            SendMessage.builder()
//                                    .chatId(String.valueOf(message.getChatId()))
//                                    .text("Ваша група " + user.getYourKurs())
//                                    .build());
                    createMessage(message,
                            "Ваш нікнейм в телеграмі " + user.getUsername());
//                    messageSender.sendMessage(
//                            SendMessage.builder()
//                                    .chatId(String.valueOf(message.getChatId()))
//                                    .text("Ваш нікнейм в телеграмі " + user.getUsername())
//                                    .build());
                } else {
                    createMessage(message,
                            "Спочатку пройди реєстаріцію за допомоги команди /register");
//                    messageSender.sendMessage(
//                            SendMessage.builder()
//                                    .chatId(String.valueOf(message.getChatId()))
//                                    .text("Спочатку пройди реєстаріцію за допомоги команди /register")
//                                    .build()
//                    );
                }
            }
        }
    }

    private void createMessage(Message message, String text) {
        messageSender.sendMessage(
                SendMessage.builder()
                        .chatId(String.valueOf(message.getChatId()))
                        .text(text)
                        .build()
        );
    }

    private void createMessage(Message message, String text, boolean isInLine,
                               int columns, int rows, String backData,
                               String... buttonNameAndData) {
        if (isInLine) {
            int b = 0;

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

            if (backData != null){
                var inlineKeyboardButton = new InlineKeyboardButton();
                List<InlineKeyboardButton> row = new ArrayList<>();
                inlineKeyboardButton.setText("\u2B05 Go back");
                inlineKeyboardButton.setCallbackData(backData);
                row.add(inlineKeyboardButton);
                column.add(row);
            }
            inlineKeyboardMarkup.setKeyboard(column);

            var sendMessage = new SendMessage();
            sendMessage.setText(text);
            sendMessage.setChatId(String.valueOf(message.getChatId()));
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            messageSender.sendMessage(sendMessage);
        } else {
            var markup = new ReplyKeyboardMarkup();
            var keyboardColumns = new ArrayList<KeyboardRow>();

            int a = 0;
            for (int ab = columns; ab > 0; ab--) {
                var keyboardRows = new KeyboardRow();
                for (int ad = rows; ad > 0; ad--) {
                    if (a < buttonNameAndData.length) {
                        keyboardRows.add(buttonNameAndData[a]);
                        a++;
                    } else {
                        continue;
                    }
                }
                keyboardColumns.add(keyboardRows);
            }
            markup.setKeyboard(keyboardColumns);
            markup.setResizeKeyboard(true);

            var sendMessage = new SendMessage();
            sendMessage.setText(text);
            sendMessage.setChatId(String.valueOf(message.getChatId()));
            sendMessage.setReplyMarkup(markup);
            messageSender.sendMessage(sendMessage);
        }
    }
}
