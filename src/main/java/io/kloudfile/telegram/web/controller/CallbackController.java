package io.kloudfile.telegram.web.controller;

import com.google.gson.Gson;
import io.kloudfile.telegram.bot.BotContainer;
import io.kloudfile.telegram.bot.bots.Bot;
import io.kloudfile.telegram.bot.dto.callbackDTO.ResponseDTO;
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

@Controller
public class CallbackController {

    private final Gson GSON = new Gson();

    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/telegram/callback/update", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity getId(@RequestBody String json) {
        ResponseDTO res = GSON.fromJson(json, ResponseDTO.class);

        if(res.getMessage() == null || res.getMessage().getText() == null) {
            return ResponseEntity.ok().build();
        }

        fileService.getChatIdSet().add(res.getMessage().getChat().getId());
        fileService.syncFile();


        String message = res.getMessage().getText();

        if (message.startsWith("/")) {
            String string[] = message.split(" ");
            string[0] = string[0].substring(1);
            List<String> strings = Arrays.asList(string);
            for(Bot bot : BotContainer.getInstance().getBotList()) {
                bot.exec(strings, res);
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
