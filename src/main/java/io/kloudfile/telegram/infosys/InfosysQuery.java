package io.kloudfile.telegram.infosys;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.List;

public final class InfosysQuery {
    private static CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
    private static final String BASE_URL = "http://splan.hs-el.de/mobile_test/index.php/json/messages/%23SPLUSD82745/1353";
    private static final InfosysParser parser = new InfosysParser();

    public static List<InfosysMessageBean> getInfosysMessages() {
        HttpGet httpget = new HttpGet(BASE_URL);
        httpget.setHeader("http.protocol.content-charset", "UTF-8");
        try {
            return parser.getAllMessages(closeableHttpClient.execute(httpget));
        } catch (IOException e) {
            // FIXME: 07/03/2017 Error handling
        }

        return null;
    }

    public static List<InfosysMessageBean> getRelevantInfosysMessages(final List<String> keywords) {
        HttpGet httpget = new HttpGet(BASE_URL);
        try {
            return parser.getRelevantMessages(closeableHttpClient.execute(httpget), keywords);
        } catch (IOException e) {
            // FIXME: 07/03/2017 Error handling
        }

        return null;
    }
}
