package io.kloudfile.telegram.bot;


import io.kloudfile.telegram.bot.dto.ResponseDTO;
import io.kloudfile.telegram.bot.dto.Result;
import io.kloudfile.telegram.bot.query.Query;
import org.apache.http.impl.client.CloseableHttpClient;

public class InfosysBot {

    private CloseableHttpClient httpClient;

    public InfosysBot(CloseableHttpClient closeableHttpClient) {
        this.httpClient = closeableHttpClient;
    }

    public void update(String message) {
        ResponseDTO responseDTO = Query.queryChats(this);
        for (Result result : responseDTO.getResult()) {
            Query.sendMessage(this, result.getMessage().getChat().getId(), "Hallo");
        }
    }

    public String getBotUsername() {
        return "HEL9000_bot";
    }

    public String getBotToken() {
        return "";
    }
}
