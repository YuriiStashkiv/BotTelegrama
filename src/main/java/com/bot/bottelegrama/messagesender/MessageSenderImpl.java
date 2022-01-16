package com.bot.bottelegrama.messagesender;

import com.bot.bottelegrama.botlogic.BotRunmethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class MessageSenderImpl implements MessageSender {
    private BotRunmethod botRunmethod;

    @Override
    public void sendMessage(SendMessage sendMessage) {
        try {
            botRunmethod.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendEditMessage(EditMessageText editMessageText) {
        try {
            botRunmethod.execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public void setBotRunmethod(BotRunmethod botRunmethod) {
        this.botRunmethod = botRunmethod;
    }
}
