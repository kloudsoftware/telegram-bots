package io.kloudfile.telegram.web.controller;

import com.google.gson.Gson;
import io.kloudfile.telegram.bot.dto.callbackDTO.ResponseDTO;
import io.kloudfile.telegram.persistence.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CallbackController {

    private final Gson GSON = new Gson();

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/telegram/callback/update", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity getId(@RequestBody String json) {
        ResponseDTO res = GSON.fromJson(json, ResponseDTO.class);
        fileService.getChatIdSet().add(res.getMessage().getChat().getId());
        fileService.syncFile();
        return ResponseEntity.ok().build();
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity test() {
        return ResponseEntity.ok().build();
    }
}
