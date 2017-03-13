
package io.kloudfile.telegram.bot.dto.infosys;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Entity {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("length")
    @Expose
    private Integer length;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

}
