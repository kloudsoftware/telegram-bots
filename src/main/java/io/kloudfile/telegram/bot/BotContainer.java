package io.kloudfile.telegram.bot;

import io.kloudfile.telegram.bot.bots.AbsBot;
import io.kloudfile.telegram.bot.bots.Bot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by larsgrahmann on 10/03/2017.
 */
public final class BotContainer {

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
