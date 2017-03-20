package io.kloudfile.telegram.bot.bots;

import io.kloudfile.telegram.bot.BotContainer;
import io.kloudfile.telegram.bot.dto.infosys.callbackDTO.ResponseDTO;
import io.kloudfile.telegram.bot.query.Query;
import io.kloudfile.telegram.infosys.InfosysMessageBean;
import io.kloudfile.telegram.persistence.entities.SubjectArea;
import io.kloudfile.telegram.persistence.entities.User;
import io.kloudfile.telegram.persistence.repos.SubjectAreaRepository;
import io.kloudfile.telegram.persistence.repos.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InfosysBot extends AbsBot {


    @Autowired
    private SubjectAreaRepository subjectAreaRepository;

    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public InfosysBot(Environment env, UserRepository userRepository) {
        this.key = env.getProperty("infosys.bot.key");
        BotContainer.getInstance().register(this);
    }

    public void update(Map<SubjectArea, List<InfosysMessageBean>> subjectAreaListMap) {

        final List<User> users = userRepository.findAll();

        users.forEach(user -> user.getSubjectAreaList().forEach(subjectArea -> {

            final List<InfosysMessageBean> messages = subjectAreaListMap.get(subjectArea);

            if (null == messages) {
                return;
            }

            logger.info(messages.size());
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
            Query.sendMessage(this, responseDTO.getMessage().getChat().getId(),
                   "Hallo " + (responseDTO.getMessage().getFrom().getUsername()
                           != null ? responseDTO.getMessage().getFrom().getUsername(): ""));
        }

        if(command.equalsIgnoreCase("aboutInfoHEL")) {
            String msg = "Hallo, ich bin HEL9000, ich versende Infosysnachrichten in Telegram-Chats.";
            msg += "\n Mein Sourcecode liegt auf github: https://github.com/probE466/telegram-bots.";
            msg += "\n\n Ich werde von @probE466 und @fr3d63 entwickelt.";

            Query.sendMessage(this, responseDTO.getMessage().getChat().getId(), msg);
        }

        if (command.contains("Subject")) {
            handelSubjectAreaCommand(command, joinArguments(args), responseDTO);
        }

        if (command.equalsIgnoreCase("help")) {
            sendHelp(responseDTO);
        }
    }

    private void sendHelp(ResponseDTO responseDTO) {
        final StringBuilder messageBuilder = new StringBuilder();

        messageBuilder.append("Hallo, ich bin HEL9000, ich versende Infosysnachrichten in Telegram-Chats.");
        messageBuilder.append('\n');

        messageBuilder.append("Ich unterstütze folgende Fachbereiche:");
        messageBuilder.append('\n');

        subjectAreaRepository.findAll().forEach(subjectArea -> {
            messageBuilder.append('\n');
            messageBuilder.append(subjectArea.getName());
        });
        messageBuilder.append('\n');
        messageBuilder.append('\n');
        messageBuilder.append("Zum hinzufügen von einem Fachbereich, schreibe: /addFachbereich NAME");
        messageBuilder.append('\n');
        messageBuilder.append("Zum löschen von einem Fachbereich, schreibe: /removeFachbereich NAME");

        messageBuilder.append('\n');
        messageBuilder.append("Für weitere Informationen schreibe: /aboutInfoHEL");

        Query.sendMessage(this, responseDTO.getMessage().getChat().getId(), messageBuilder.toString());

    }

    private String joinArguments(List<String> arguments) {
        String joinedArguments = "";

        for (String argument : arguments) {
            joinedArguments += " " + argument;
        }

        return joinedArguments.trim();
    }

    private void handelSubjectAreaCommand(String command, String targetSubject, ResponseDTO responseDTO) {
        final int chatid = responseDTO.getMessage().getChat().getId();

        final Optional<User> userOptional = userRepository.findByChatId(chatid);

        if (!userOptional.isPresent()) {
            Query.sendMessage(this, chatid, "Ein Fehler ist aufgetreten");
            return;
        }

        final User user = userOptional.get();

        final Optional<SubjectArea> subjectAreaOptional = subjectAreaRepository.findByName(targetSubject);

        if (!subjectAreaOptional.isPresent()) {
            Query.sendMessage(this, chatid, "Fachbereich konnte nicht gefunden werden");
            return;
        }

        final SubjectArea subjectArea = subjectAreaOptional.get();

        if (command.equalsIgnoreCase("addSubject")) {
            addSubjectArea(targetSubject, chatid, user, subjectArea);
            return;
        }

        removeSubjectArea(targetSubject, chatid, user, subjectArea);

    }

    private void removeSubjectArea(String targetSubject, int chatid, User user, SubjectArea subjectArea) {
        if (!user.hasSubscribed(subjectArea)) {
            Query.sendMessage(this, chatid, "Fachbereich " + targetSubject + " ist nicht abboniert");
            return;
        }
        user.removeSubjectArea(subjectArea);
        userRepository.save(user);
        Query.sendMessage(this, chatid, "Fachbereich " + targetSubject + " gelöscht");
    }

    private void addSubjectArea(String targetSubject, int chatid, User user, SubjectArea subjectArea) {
        if (user.hasSubscribed(subjectArea)) {
            Query.sendMessage(this, chatid, "Fachbereich " + targetSubject + " ist bereits abboniert");
            return;
        }
        user.addSubjectArea(subjectArea);
        userRepository.save(user);
        Query.sendMessage(this, chatid, "Fachbereich " + targetSubject + " abboniert");
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
