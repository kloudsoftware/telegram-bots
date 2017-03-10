package io.kloudfile.telegram.bot;

/**
 * Created by larsg on 07.03.2017.
 */
public interface Bot<T> {

    String getBotToken();

    void exec(String command);

    void post(T data);
}
