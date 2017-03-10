package io.kloudfile.telegram.infosys;

import io.kloudfile.telegram.bot.bots.InfosysBot;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public final class InfosysQuery {
    private final CloseableHttpClient closeableHttpClient;
    private static final String BASE_URL = "http://splan.hs-el.de/mobile_test/index.php/json/messages/";
    private static final String SUBJECT_AREA = "%23SPLUSD82745";
    private String lastMessageID = "0";
    private final InfosysParser parser = new InfosysParser();
    private long lastDate;

    @Autowired
    private InfosysBot infosysBot;

    private final Logger logger = Logger.getLogger(this.getClass());

    public InfosysQuery() {
        closeableHttpClient = HttpClients.createDefault();

        lastDate = System.currentTimeMillis() / 1000;
    }

    @Scheduled(fixedRate = 300000)
    public void run() {
        try {
            List<InfosysMessageBean> messagesBuffer = reverseList(getMessages());

            final List<InfosysMessageBean> messagesToBroadcast = new ArrayList<>();

            messagesBuffer.forEach(messageCandidate -> {
                final long candidateDate = messageCandidate.getCreated();
                if (candidateDate > lastDate) {
                    messagesToBroadcast.add(messageCandidate);
                    lastDate = candidateDate;
                }
            });

            if (!messagesToBroadcast.isEmpty()) {
                this.lastMessageID = messagesToBroadcast.get(messagesToBroadcast.size() - 1).getId();
                logger.info("Found new messages, broadcasting them");
                infosysBot.update(messagesToBroadcast);
            }

        } catch (IOException e) {
            // FIXME: 07/03/2017 Error Handling
            e.printStackTrace();
        }
    }

    private List<InfosysMessageBean> getMessages() throws IOException {
        final String url = BASE_URL + SUBJECT_AREA + "/" + this.lastMessageID;
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("http.protocol.content-charset", "UTF-8");

        return parser.getAllMessages(closeableHttpClient.execute(httpget));
    }

    private List<InfosysMessageBean> reverseList(List<InfosysMessageBean> beanList) {
        List<InfosysMessageBean> returnList = new ArrayList<>();

        if (0 == beanList.size()) {
            return returnList;
        }


        for (int i = beanList.size() - 1; i >= 0; i--) {
            returnList.add(beanList.get(i));
        }

        return returnList;
    }
}
