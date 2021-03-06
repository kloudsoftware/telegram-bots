
package io.kloudfile.telegram.bot.dto.infosys.callbackDTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDTO {

    @SerializedName("update_id")
    @Expose
    private Integer updateId;
    @SerializedName("message")
    @Expose
    private Message message;

    public Integer getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
