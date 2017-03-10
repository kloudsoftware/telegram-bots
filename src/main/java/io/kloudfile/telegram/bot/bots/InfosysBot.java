package io.kloudfile.telegram.bot.bots;

import io.kloudfile.telegram.bot.BotContainer;
import io.kloudfile.telegram.bot.dto.callbackDTO.ResponseDTO;
import io.kloudfile.telegram.bot.query.Query;
import io.kloudfile.telegram.infosys.InfosysMessageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class InfosysBot extends AbsBot {

    private final String key;

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

        for (Integer integer : fileService.getChatIdSet()) {
            Query.sendMessage(this, integer, message);
        }

    }

    public String getBotToken() {
        return key;
    }

    @Override
    public void exec(List<String> command, ResponseDTO responseDTO) {
        if (command.get(0).equalsIgnoreCase("hello")) {
            Query.sendMessage(this, responseDTO.getMessage().getChat().getId(), "Hallo");
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
