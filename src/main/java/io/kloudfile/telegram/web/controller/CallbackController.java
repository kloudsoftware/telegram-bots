package io.kloudfile.telegram.web.controller;

import com.google.gson.Gson;
import io.kloudfile.telegram.bot.dto.ResponseDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CallbackController {

    private final Gson GSON = new Gson();

    @RequestMapping(value = "/telegram/callback/update", method = RequestMethod.POST)
    public void getId(@RequestBody String json) {
        ResponseDTO res = GSON.fromJson(json, ResponseDTO.class);

    }

}
