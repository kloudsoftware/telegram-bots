package io.kloudfile.telegram.bot.bots;

import io.kloudfile.telegram.bot.BotContainer;
import io.kloudfile.telegram.bot.dto.infosys.callbackDTO.ResponseDTO;
import io.kloudfile.telegram.bot.query.Query;
import io.kloudfile.telegram.infosys.InfosysMessageBean;
import io.kloudfile.telegram.persistence.entities.SubjectArea;
import io.kloudfile.telegram.persistence.entities.User;
import io.kloudfile.telegram.persistence.repos.SubjectAreaRepository;
import io.kloudfile.telegram.persistence.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class InfosysBot extends AbsBot {

    private final String key;

    private final UserRepository userRepository;

    @Autowired
    private SubjectAreaRepository subjectAreaRepository;

    @Autowired
    public InfosysBot(Environment env, UserRepository userRepository) {
        this.key = env.getProperty("infosys.bot.key");
        BotContainer.getInstance().register(this);
        this.userRepository = userRepository;
    }

    public void update(Map<SubjectArea, List<InfosysMessageBean>> subjectAreaListMap) {

        final List<User> users = userRepository.findAll();

        users.forEach(user -> user.getSubjectAreaList().forEach(subjectArea -> {

            final List<InfosysMessageBean> messages = subjectAreaListMap.get(subjectArea);

            if (null == messages) {
                return;
            }
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

            Query.sendMessage(this, user.getChatId(), messageBuilder.toString());
        }));


    }

    public String getBotToken() {
        return key;
    }

    @Override
    public void exec(String command, List<String> args, ResponseDTO responseDTO) {
        if (command.equalsIgnoreCase("hello")) {
            Query.sendMessage(this, responseDTO.getMessage().getChat().getId(), "Hallo");
        }

        if (command.equalsIgnoreCase("addsubject")) {
            addSubjectArea(joinArguments(args), responseDTO);
        }

        if (command.equalsIgnoreCase("removesubject")) {
            removeSubjectArea(joinArguments(args), responseDTO);
        }
    }

    private String joinArguments(List<String> arguments) {
        String joinedArguments = "";

        for (String argument : arguments) {
            joinedArguments += " " + argument;
        }

        return joinedArguments.trim();
    }

    private void addSubjectArea(String targetSubject, ResponseDTO responseDTO) {
        final Integer chatID = responseDTO.getMessage().getChat().getId();
        userRepository.findByChatId(chatID).ifPresent(user ->
                subjectAreaRepository.findByName(targetSubject).ifPresent(subjectArea -> {
                    user.addSubjectArea(subjectArea);
                    userRepository.flush();
                    Query.sendMessage(this, chatID, "Fachbereich " + targetSubject + " hinzugefügt");
                }));
    }

    private void removeSubjectArea(String targetSubject, ResponseDTO responseDTO) {
        final Integer chatID = responseDTO.getMessage().getChat().getId();
        userRepository.findByChatId(responseDTO.getMessage().getChat().getId()).ifPresent(user ->
                subjectAreaRepository.findByName(targetSubject).ifPresent(subjectArea -> {
                    user.removeSubjectArea(subjectArea);
                    userRepository.flush();
                    Query.sendMessage(this, chatID, "Fachbereich " + targetSubject + " gelöscht");
                }));
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
