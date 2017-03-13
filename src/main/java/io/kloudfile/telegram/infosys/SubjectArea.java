package io.kloudfile.telegram.infosys;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubjectArea {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("hostKey")
    @Expose
    private String hostKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostKey() {
        return hostKey;
    }

    public void setHostKey(String hostKey) {
        this.hostKey = hostKey;
    }

}