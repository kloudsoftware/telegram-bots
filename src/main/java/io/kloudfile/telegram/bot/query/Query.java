package io.kloudfile.telegram.bot.query;

import com.google.gson.Gson;
import io.kloudfile.telegram.bot.Bot;
import io.kloudfile.telegram.bot.InfosysBot;
import io.kloudfile.telegram.bot.dto.ResponseDTO;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public final class Query {

    private static final String BASE_API_URL = "https://api.telegram.org/bot";

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    private static final Logger logger = Logger.getLogger(Query.class);

    private static Gson gson = new Gson();

    public static ResponseDTO queryChats(InfosysBot bot, int limit) {
        ResponseDTO responseDTO = null;
        String queryURL = BASE_API_URL + bot.getBotToken();

        HttpGet httpGet = new HttpGet(queryURL + "/getUpdates");
        if(limit > 1) {
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("limit", String.valueOf(limit)));
        }
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            responseDTO = gson.fromJson(EntityUtils.toString(response.getEntity()), ResponseDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseDTO;
    }

    public static void sendMessage(Bot bot, int id, String message) {
        HttpPost httpPost = new HttpPost(BASE_API_URL + bot.getBotToken() + "/sendMessage");
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("chat_id", String.valueOf(id)));
        nameValuePairs.add(new BasicNameValuePair("text", message));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPost);
            logger.info("Sending msg to: " + id);
            closeableHttpResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendImage(Bot bot, int id, File file) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(BASE_API_URL + bot.getBotToken() + "/sendPhoto");
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("chat_id", String.valueOf(id)));
        HttpEntity httpEntity = MultipartEntityBuilder.create().addPart("photo", new FileBody(file))
                .addPart("chat_id", new StringBody(String.valueOf(id))).build();
        try {
            httpPost.setEntity(httpEntity);
            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPost);
            logger.info("Sending img to: " + id);
            closeableHttpResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendDoc(Bot bot, int id, File file) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(BASE_API_URL + bot.getBotToken() + "/sendDocument");
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("chat_id", String.valueOf(id)));
        HttpEntity httpEntity = MultipartEntityBuilder.create().addPart("document", new FileBody(file))
                .addPart("chat_id", new StringBody(String.valueOf(id))).build();
        try {
            httpPost.setEntity(httpEntity);
            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPost);
            logger.info("Sending doc to: " + id);
            closeableHttpResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
