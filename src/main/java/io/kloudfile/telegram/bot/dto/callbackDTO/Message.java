
package io.kloudfile.telegram.bot.dto.callbackDTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.kloudfile.telegram.bot.dto.From;

public class Message {

    @SerializedName("date")
    @Expose
    private Integer date;
    @SerializedName("chat")
    @Expose
    private io.kloudfile.telegram.bot.dto.Chat chat;
    @SerializedName("message_id")
    @Expose
    private Integer messageId;
    @SerializedName("from")
    @Expose
    private io.kloudfile.telegram.bot.dto.From from;
    @SerializedName("text")
    @Expose
    private String text;

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public io.kloudfile.telegram.bot.dto.Chat getChat() {
        return chat;
    }

    public void setChat(io.kloudfile.telegram.bot.dto.Chat chat) {
        this.chat = chat;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public io.kloudfile.telegram.bot.dto.From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
