package io.kloudfile.telegram.web.controller;

import com.google.gson.Gson;
import io.kloudfile.telegram.bot.BotContainer;
import io.kloudfile.telegram.bot.bots.Bot;
import io.kloudfile.telegram.bot.dto.callbackDTO.ResponseDTO;
import io.kloudfile.telegram.persistence.entities.User;
import io.kloudfile.telegram.persistence.repos.UserRepository;
import io.kloudfile.telegram.persistence.services.FileService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class CallbackController {

    private final Gson GSON = new Gson();

    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private FileService fileService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/telegram/callback/update", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity getId(@RequestBody String json) {
        ResponseDTO res = GSON.fromJson(json, ResponseDTO.class);

        if (res.getMessage() == null || res.getMessage().getText() == null) {
            return ResponseEntity.ok().build();
        }


        final int chatID = res.getMessage().getChat().getId();
        final String username = res.getMessage().getFrom().getUsername();

        Optional<User> foundUser = userRepository.findByChatId(chatID);

        if (!foundUser.isPresent()) {
            User user = new User();
            user.setChatId(chatID);
            user.setUsername(username);
            userRepository.save(user);
        }

        String message = res.getMessage().getText();

        if (message.startsWith("/")) {
            String string[] = message.split(" ");
            string[0] = string[0].substring(1);
            List<String> args = Arrays.asList(string);
            String command = args.get(0);
            args.remove(0);
            for (Bot bot : BotContainer.getInstance().getBotList()) {
                bot.exec(command, args, res);
            }
        }


        return ResponseEntity.ok().build();
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity test() {
        logger.info("Test recieved");
        return ResponseEntity.ok().build();
    }
}
