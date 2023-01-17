package com.botcountsomething.telegramspringbot;

import com.botcountsomething.telegramspringbot.components.BotCommands;
import com.botcountsomething.telegramspringbot.config.BotConfig;
import com.botcountsomething.telegramspringbot.database.User;
import com.botcountsomething.telegramspringbot.database.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramCounterBot extends TelegramLongPollingBot implements BotCommands {
    final BotConfig config;

    @Autowired
    private UserRepository userRepository;

    public TelegramCounterBot(BotConfig config, UserRepository userRepository) {
        this.config = config;
        try {
            this.execute(new SetMyCommands(BOT_COMMANDS_LIST, new BotCommandScopeDefault(), null));        }catch (TelegramApiException e){
            log.error(e.getMessage());
        }
        this.userRepository = userRepository;
    }

    @Override
    public String getBotUsername() { return config.getBotName(); }

    @Override
    public String getBotToken() { return config.getToken(); }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        long chatId = 0;
        long userId = 0;
        String userName = null;
        String receivedMessage;

        if(update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            userId = update.getMessage().getFrom().getId();
            userName = update.getMessage().getFrom().getFirstName();

            if (update.getMessage().hasText()) {
                receivedMessage = update.getMessage().getText();
                botAnswerUtils(receivedMessage, chatId, userName);
            }
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            userId = update.getCallbackQuery().getFrom().getId();
            userName = update.getCallbackQuery().getFrom().getFirstName();
            receivedMessage = update.getCallbackQuery().getData();

            botAnswerUtils(receivedMessage, chatId, userName);
        }

        if(chatId == Long.valueOf(config.getChatId())){
            updateDB(userId, userName);
        }
    }

    private void botAnswerUtils(String receivedMessage, long chatId, String userName) {

        switch (receivedMessage){
            case "/start" -> {
                writeToBot(chatId, userName);
                break;
            }
            case "/help" -> {
                sendHelpText(chatId, HELP_TEXT);
                break;
            }
            default -> log.info("Unexpected message");
        }
    }

    private void sendHelpText(long chatId, String helpText) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(helpText);

        try {
            execute(sendMessage);
            log.info("Sent");
        }catch (TelegramApiException e){
            log.error(e.getMessage());
        }

    }

    private void writeToBot(long chatId, String memberName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Hello, " + memberName + "! I'm a Telegram bot.");

        try {
            execute(message);
            log.info("Sent");
        }catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }

    private void updateDB(long userId, String userName) {
        if(userRepository.findById(userId).isEmpty()){
            User user = new User();
            user.setId(userId);
            user.setName(userName);
            //сразу добавляем в столбец каунтера 1 сообщение
            user.setMsg_numb(1);

            userRepository.save(user);
            log.info("Added to DB: " + user);
        } else {
            userRepository.updateMsgNumberByUserId(userId);
        }
    }

}
