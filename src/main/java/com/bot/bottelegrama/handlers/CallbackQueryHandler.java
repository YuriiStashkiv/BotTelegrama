package com.bot.bottelegrama.handlers;

import com.bot.bottelegrama.cache.Cache;
import com.bot.bottelegrama.domain.BotUser;
import com.bot.bottelegrama.domain.Position;
import com.bot.bottelegrama.messagesender.MessageSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;

@Component
public class CallbackQueryHandler implements Handler<CallbackQuery>{

    private final MessageSender messageSender;
    private final Cache<BotUser> cache;

    public CallbackQueryHandler(MessageSender messageSender, Cache<BotUser> cache) {
        this.messageSender = messageSender;
        this.cache = cache;
    }

    private BotUser yourGroupYearAndKurs(Message message){
        BotUser user = new BotUser();
        user.setUsername(message.getFrom().getUserName());
        user.setId(message.getChatId());
        user.setPosition(Position.INPUT_KURS);
        return user;
    }

    @Override
    public void choose(CallbackQuery callbackQuery) {
        BotUser user = cache.findBy(callbackQuery.getMessage().getChatId());
        if (user != null) {
            switch (user.getPosition()) {
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
                    break;
                case INPUT_YEAR:
                    user.setYear(callbackQuery.getData());
                    user.setPosition(Position.INPUT_GROUP);
                    Integer messageId1 = callbackQuery.getMessage().getMessageId();
                    var editMessageText1 = new EditMessageText();
                    editMessageText1.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
                    editMessageText1.setMessageId(messageId1);
                    editMessageText1.setText("Обери номер своєї групи...");
                    editMessageText1.setReplyMarkup(
                            InlineKeyboardMarkup.builder()
                                    .keyboardRow(Collections.singletonList(
                                            InlineKeyboardButton.builder()
                                                    .text("1-а група")
                                                    .callbackData("1")
                                                    .build()
                                    ))
                                    .keyboardRow(Collections.singletonList(
                                            InlineKeyboardButton.builder()
                                                    .text("2-а група")
                                                    .callbackData("2")
                                                    .build()
                                    ))
                                    .build());
                    messageSender.sendEditMessage(editMessageText1);
                    break;
                case INPUT_GROUP:
                    user.setGroup(callbackQuery.getData());
                    user.setPosition(Position.NONE);
                    Integer messageId2 = callbackQuery.getMessage().getMessageId();
                    var editMessageText2 = new EditMessageText();
                    editMessageText2.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
                    editMessageText2.setMessageId(messageId2);
                    editMessageText2.setText("Твоя група - це " + user.getKurs() + user.getYear()
                            + user.getGroup()+"\n" +
                            "Правильно?");
                    editMessageText2.setReplyMarkup(InlineKeyboardMarkup.builder()
                            .keyboardRow(Collections.singletonList(
                                    InlineKeyboardButton.builder()
                                            .text("Так")
                                            .callbackData("Yes")
                                            .build()))
                            .keyboardRow(Collections.singletonList(
                                    InlineKeyboardButton.builder()
                                            .text("Ні")
                                            .callbackData("No")
                                            .build()))
                            .build());
                    messageSender.sendEditMessage(editMessageText2);
                    break;
                case NONE:
                    switch (callbackQuery.getData()){
                        case "Yes":

                            break;
                        case "No":
                            Integer messageId3 = callbackQuery.getMessage().getMessageId();
                            var editMessageText3 = new EditMessageText();
                            editMessageText3.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
                            editMessageText3.setMessageId(messageId3);
                            editMessageText3.setText("Чи не бажаєте повторити реєстарцію?");
                            editMessageText3.setReplyMarkup(
                                    InlineKeyboardMarkup.builder()
                                            .keyboardRow(Collections.singletonList(
                                                    InlineKeyboardButton.builder()
                                                            .text("Так")
                                                            .callbackData("Yes")
                                                            .build()
                                            ))
                                            .keyboardRow(Collections.singletonList(
                                                    InlineKeyboardButton.builder()
                                                            .text("Ні")
                                                            .callbackData("No")
                                                            .build()
                                            )).build());
                            messageSender.sendEditMessage(editMessageText3);
                            break;
                    }
                    break;
            }
        } else {
            switch (callbackQuery.getData()){
                case "Agree":
                    cache.add(yourGroupYearAndKurs(callbackQuery.getMessage()));
                    Integer messageId = callbackQuery.getMessage().getMessageId();
                    var editMessageText = new EditMessageText();
                    editMessageText.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
                    editMessageText.setMessageId(messageId);
                    editMessageText.setText("Гаразд, тоді попчнемо\n" +
                            "Спершу виберіть свій курс...");
                    editMessageText.setReplyMarkup(
                    InlineKeyboardMarkup.builder()
                            .keyboardRow(Collections.singletonList(
                                    InlineKeyboardButton.builder()
                                            .text("КН")
                                            .callbackData("КН")
                                            .build()
                            ))
                            .keyboardRow(Collections.singletonList(
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
                    editMessageText2.setText("Тоді я працювати не можу, гарного дня, "+
                            callbackQuery.getFrom().getUserName()+"!");
                    messageSender.sendEditMessage(editMessageText2);
                    break;
            }
        }
    }
}
