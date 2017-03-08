package io.kloudfile.telegram.bot;

import io.kloudfile.telegram.bot.query.Query;
import io.kloudfile.telegram.infosys.InfosysMessageBean;
import io.kloudfile.telegram.persistence.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class InfosysBot implements Bot {


    @Autowired
    private FileService fileService;

    public void update(List<InfosysMessageBean> messages) {
        StringBuilder messageBuilder = new StringBuilder();

        if (messages.size() == 1) {
            messageBuilder.append("Neue Infosys Nachricht:");
            messageBuilder.append(buildMsg(messages.get(0)));
        } else {
            messageBuilder.append("Neue Infosys Nachrichten:");
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
        return "";
    }

    private String buildMsg(InfosysMessageBean messageBean) {
        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append(new Date(messageBean.getCreated()).toString()).append("\n");
        msgBuilder.append(messageBean.getTitle()).append("\n");
        msgBuilder.append(messageBean.getDescription()).append("\n");
        msgBuilder.append(messageBean.getLink());
        msgBuilder.append(messageBean.getCreator());
        return msgBuilder.toString();
    }
}
