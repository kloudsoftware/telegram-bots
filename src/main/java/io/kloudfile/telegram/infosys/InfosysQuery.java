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
    private static final String BASE_URL = "http://splan.hs-el.de/mobile_test/index.php/json/messages/%23SPLUSD82745/1353";
    private final CloseableHttpClient closeableHttpClient;
    private final InfosysParser parser = new InfosysParser();
    private final Logger logger = Logger.getLogger(this.getClass());
    private long lastDate;
    @Autowired
    private InfosysBot infosysBot;

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
                logger.info("Found new messages, broadcasting them");
                infosysBot.update(messagesToBroadcast);
            }

        } catch (IOException e) {
            // FIXME: 07/03/2017 Error Handling
            e.printStackTrace();
        }
    }

    private List<InfosysMessageBean> getMessages() throws IOException {
        HttpGet httpget = new HttpGet(BASE_URL);
        httpget.setHeader("http.protocol.content-charset", "UTF-8");

        return parser.getAllMessages(closeableHttpClient.execute(httpget));
    }

    private List<InfosysMessageBean> reverseList(List<InfosysMessageBean> beanList) {
        List<InfosysMessageBean> returnList = new ArrayList<>();

        for (int i = beanList.size() - 1; i >= 0; i--) {
            returnList.add(beanList.get(i));
        }

        return returnList;
    }
}
