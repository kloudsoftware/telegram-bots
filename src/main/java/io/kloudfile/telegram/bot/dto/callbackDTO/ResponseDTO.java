
package io.kloudfile.telegram.bot.dto.callbackDTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDTO {

    @SerializedName("update_id")
    @Expose
    private Integer updateId;
    @SerializedName("message")
    @Expose
    private io.kloudfile.telegram.bot.dto.Message message;

    public Integer getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }

    public io.kloudfile.telegram.bot.dto.Message getMessage() {
        return message;
    }

    public void setMessage(io.kloudfile.telegram.bot.dto.Message message) {
        this.message = message;
    }

}
