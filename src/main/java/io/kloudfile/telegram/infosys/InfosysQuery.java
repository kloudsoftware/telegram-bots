package io.kloudfile.telegram.infosys;

import io.kloudfile.telegram.bot.bots.InfosysBot;
import io.kloudfile.telegram.persistence.entities.SubjectArea;
import io.kloudfile.telegram.persistence.repos.SubjectAreaRepository;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public final class InfosysQuery {

    private final CloseableHttpClient closeableHttpClient;
    private String BASE_URL;
    private static final String SUBJECT_AREA = "%23SPLUSD82745";
    private final InfosysParser parser = new InfosysParser();
    private long lastMessageTimestamp = System.currentTimeMillis() / 1000;

    @Autowired
    private InfosysBot infosysBot;

    private final Logger logger = Logger.getLogger(this.getClass());
    private List<SubjectArea> subjectAreas;

    @Autowired
    public InfosysQuery(Environment env, SubjectAreaRepository subjectAreaRepository) {
        closeableHttpClient = HttpClients.createDefault();

        final boolean isDebug = Boolean.parseBoolean(env.getProperty("app.debug"));
        if (isDebug) {
            BASE_URL = env.getProperty("infosys.debug.url");
        } else {
            BASE_URL = env.getProperty("infosys.url");
        }

        subjectAreas = subjectAreaRepository.findAll();

    }

    @Scheduled(fixedRate = 300000)
    public void run() {
        logger.info("Run infosys query");

        final Map<SubjectArea, List<InfosysMessageBean>> keyMessageMap = new HashMap<>();

        subjectAreas.forEach(subjectArea -> {
            try {
                List<InfosysMessageBean> messagesBuffer = reverseList(getMessages(subjectArea));

                final List<InfosysMessageBean> messagesToBroadcast = new ArrayList<>();

                for (InfosysMessageBean messageCandidate : messagesBuffer) {
                    final long candidateDate = messageCandidate.getCreated();
                    if (candidateDate > lastMessageTimestamp) {
                        messagesToBroadcast.add(messageCandidate);
                    }
                }

                if (messagesToBroadcast.isEmpty()) {
                    return;
                }

                keyMessageMap.put(subjectArea, messagesToBroadcast);

            } catch (IOException e) {
                // FIXME: 07/03/2017 Error Handling
                e.printStackTrace();
            }
        });

        lastMessageTimestamp = System.currentTimeMillis() / 1000;
        if (!keyMessageMap.isEmpty()) {
            logger.info("Found new messages, broadcasting them");
            infosysBot.update(keyMessageMap);
        }
    }

    private List<InfosysMessageBean> getMessages(SubjectArea subjectArea) throws IOException {
        final String url = BASE_URL + subjectArea.getHostkey() + "/0";
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("http.protocol.content-charset", "UTF-8");

        return parser.getAllMessages(closeableHttpClient.execute(httpget));
    }

    private List<InfosysMessageBean> reverseList(List<InfosysMessageBean> beanList) {
        List<InfosysMessageBean> returnList = new ArrayList<>();

        if (null == beanList || beanList.isEmpty()) {
            return returnList;
        }


        for (int i = beanList.size() - 1; i >= 0; i--) {
            returnList.add(beanList.get(i));
        }

        return returnList;
    }
}
