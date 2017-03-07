package io.kloudfile.telegram.bot.query;

import com.google.gson.Gson;
import io.kloudfile.telegram.bot.InfosysBot;
import io.kloudfile.telegram.bot.dto.ResponseDTO;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Query {

    private static final String BASE_API_URL = "https://api.telegram.org/bot";

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    private static Gson gson = new Gson();

    public static ResponseDTO queryChats(InfosysBot bot) {
        ResponseDTO responseDTO = null;
        String queryURL = BASE_API_URL + bot.getBotToken();

        HttpGet httpGet = new HttpGet(queryURL + "/getUpdates");
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            responseDTO = gson.fromJson(EntityUtils.toString(response.getEntity()), ResponseDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseDTO;
    }

    public static void sendMessage(InfosysBot bot, int id, String message) {
        HttpPost httpPost = new HttpPost(BASE_API_URL + bot.getBotToken() + "/sendMessage");
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("chat_id", String.valueOf(id)));
        nameValuePairs.add(new BasicNameValuePair("text", message));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPost);
            System.out.println(id);
            closeableHttpResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
