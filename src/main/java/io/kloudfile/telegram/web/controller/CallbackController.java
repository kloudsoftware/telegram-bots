package io.kloudfile.telegram.web.controller;

import com.google.gson.Gson;
import io.kloudfile.telegram.bot.dto.ResponseDTO;
import io.kloudfile.telegram.bot.dto.Result;
import io.kloudfile.telegram.persistence.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CallbackController {

    private final Gson GSON = new Gson();

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/telegram/callback/update", method = RequestMethod.POST)
    public void getId(@RequestBody String json) {
        ResponseDTO res = GSON.fromJson(json, ResponseDTO.class);
        for (Result result : res.getResult()) {
            fileService.getChatIdSet().add(result.getMessage().getChat().getId());
        }
        fileService.syncFile();
        //Process commands
    }

}
