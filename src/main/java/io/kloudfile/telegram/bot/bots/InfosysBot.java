package io.kloudfile.telegram.bot.bots;

import io.kloudfile.telegram.bot.BotContainer;
import io.kloudfile.telegram.bot.dto.infosys.callbackDTO.ResponseDTO;
import io.kloudfile.telegram.bot.query.Query;
import io.kloudfile.telegram.infosys.InfosysMessageBean;
import io.kloudfile.telegram.persistence.entities.User;
import io.kloudfile.telegram.persistence.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InfosysBot extends AbsBot {


    @Autowired
    public InfosysBot(Environment env) {
        this.key = env.getProperty("infosys.bot.key");
        BotContainer.getInstance().register(this);
    }

    public void update(List<InfosysMessageBean> messages) {
        StringBuilder messageBuilder = new StringBuilder();

        if (messages.size() == 1) {
            messageBuilder.append("Neue Infosys Nachricht:").append("\n").append("\n");
            messageBuilder.append(buildMsg(messages.get(0)));
        } else {
            messageBuilder.append("Neue Infosys Nachrichten:").append("\n").append("\n");
            for (InfosysMessageBean messageBean : messages) {
                messageBuilder.append(buildMsg(messageBean));
            }
        }

        String message = messageBuilder.toString();

        List<Integer> chatIdList = userRepository.findAll().stream().map(User::getChatId).collect(Collectors.toList());
        for (Integer chatID : chatIdList) {
            Query.sendMessage(this, chatID, message);
        }

    }

    public String getBotToken() {
        return key;
    }

    @Override
    public void exec(String command, List<String> args, ResponseDTO responseDTO) {
        if (command.equalsIgnoreCase("hello")) {
            Query.sendMessage(this, responseDTO.getMessage().getChat().getId(),
                   "Hallo " + (responseDTO.getMessage().getFrom().getUsername()
                           != null ? responseDTO.getMessage().getFrom().getUsername(): ""));
        }
    }

    private String buildMsg(InfosysMessageBean messageBean) {
        StringBuilder msgBuilder = new StringBuilder();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm d.M.YYYY");

        msgBuilder.append(messageBean.getTitle()).append("\n").append("\n");
        msgBuilder.append(messageBean.getDescription()).append("\n");
        msgBuilder.append(messageBean.getLink()).append("\n").append("\n");
        msgBuilder.append(messageBean.getCreator()).append("\n").append("\n");
        msgBuilder.append(dateFormat.format(new Date(messageBean.getCreated() * 1000))).append("\n")
                .append("--").append("\n").append("\n");
        return msgBuilder.toString().replaceAll("&quot;", "\"");
    }
}
