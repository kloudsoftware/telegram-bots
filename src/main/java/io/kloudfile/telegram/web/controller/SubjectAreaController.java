package io.kloudfile.telegram.web.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.kloudfile.telegram.infosys.SubjectArea;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@Controller
public class SubjectAreaController {

    private Logger logger = Logger.getLogger(this.getClass());
    private String BASE_URL;
    private CloseableHttpClient closeableHttpClient;
    private List<SubjectArea> subjectAreas;


    @Autowired
    public SubjectAreaController(Environment env) {
        closeableHttpClient = HttpClients.createDefault();

        BASE_URL = env.getProperty("infosys.subject_areas.url");
    }

    @PostConstruct
    public void fetchSubjectAreas() {
        final String url = BASE_URL;
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("http.protocol.content-charset", "UTF-8");

        Gson gson = new Gson();
        Type type = new TypeToken<List<SubjectArea>>() {}.getType();
        try (CloseableHttpResponse response = closeableHttpClient.execute(httpget)) {
            subjectAreas = gson.fromJson(EntityUtils.toString(response.getEntity()), type);
            logger.info("Loaded subjectAreas");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
