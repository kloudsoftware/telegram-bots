package io.kloudfile.telegram.web.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.kloudfile.telegram.bot.BotContainer;
import io.kloudfile.telegram.bot.bots.Bot;
import io.kloudfile.telegram.infosys.SubjectAreaDTO;
import io.kloudfile.telegram.persistence.entities.SubjectArea;
import io.kloudfile.telegram.persistence.repos.SubjectAreaRepository;
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

    private final SubjectAreaRepository subjectAreaRepository;
    private Logger logger = Logger.getLogger(this.getClass());
    private String BASE_URL;
    private CloseableHttpClient closeableHttpClient;
    private List<SubjectAreaDTO> subjectAreaDTOS;


    @Autowired
    public SubjectAreaController(Environment env, SubjectAreaRepository subjectAreaRepository) {
        this.subjectAreaRepository = subjectAreaRepository;
        closeableHttpClient = HttpClients.createDefault();

        BASE_URL = env.getProperty("infosys.subject_areas.url");
    }

    @PostConstruct
    public void fetchSubjectAreas() {
        final String url = BASE_URL;
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("http.protocol.content-charset", "UTF-8");

        Gson gson = new Gson();
        Type type = new TypeToken<List<SubjectAreaDTO>>() {}.getType();
        try (CloseableHttpResponse response = closeableHttpClient.execute(httpget)) {
            subjectAreaDTOS = gson.fromJson(EntityUtils.toString(response.getEntity()), type);

            subjectAreaDTOS.forEach(subjectAreaDTO -> {
                if ("#Institut Ipro-L".equals(subjectAreaDTO.getName())) {
                    return;
                }

                if (subjectAreaRepository.findByHostkey(subjectAreaDTO.getHostKey()).isPresent()) {
                    return;
                }
                SubjectArea subjectArea = new SubjectArea();
                subjectArea.setHostkey(subjectAreaDTO.getHostKey());
                subjectArea.setName(subjectAreaDTO.getName());
                subjectAreaRepository.save(subjectArea);
            });
            logger.info("Loaded subjectAreaDTOS");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
