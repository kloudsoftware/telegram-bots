package io.kloudfile.telegram.bot;

import io.kloudfile.telegram.infosys.InfosysMessageBean;

import java.util.List;

/**
 * Created by larsg on 07.03.2017.
 */
public interface Bot {

    String getBotToken();

    void exec(String command);
}
