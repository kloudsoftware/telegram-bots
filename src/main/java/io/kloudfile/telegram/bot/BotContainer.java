package io.kloudfile.telegram.bot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by larsgrahmann on 10/03/2017.
 */
public class BotContainer {

    private List<Bot> botList = new ArrayList<>();

    private static BotContainer ourInstance = new BotContainer();

    public static BotContainer getInstance() {
        return ourInstance;
    }

    private BotContainer() {
    }

    public List<Bot> getBotList() {
        return botList;
    }

    public void register(AbsBot absBot) {
        botList.add(absBot);

    }
}
