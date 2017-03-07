package io.kloudfile.telegram.infosys;

import io.kloudfile.telegram.bot.InfosysBot;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public final class InfosysQuery {
    private final CloseableHttpClient closeableHttpClient;
    private static final String BASE_URL = "http://splan.hs-el.de/mobile_test/index.php/json/messages/%23SPLUSD82745/1353";
    private final InfosysParser parser = new InfosysParser();
    private int lastID = -1;
    private InfosysBot infosysBot;

    private final Logger logger = Logger.getLogger(this.getClass());

    public InfosysQuery() {
        closeableHttpClient = HttpClients.createDefault();
        infosysBot = new InfosysBot(closeableHttpClient);
    }

    @Scheduled(fixedRate = 60000)
    public void run() {
        try {
            List<InfosysMessageBean> messagesBuffer = getMessages();

            final List<InfosysMessageBean> messagesToBroadcast = new ArrayList<>();

            messagesBuffer.forEach(infosysMessageBean -> {
                int tempID = Integer.parseInt(infosysMessageBean.getId());
                if (tempID > lastID) {
                    messagesToBroadcast.add(infosysMessageBean);
                    lastID = tempID;
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
}
