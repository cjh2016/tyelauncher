package com.boll.tyelauncher.model;


import java.io.Serializable;

public class K12PushMessage implements Serializable {
    public static final String K12_MSG_TYPE_OPERATIONS = "1";
    private static final long serialVersionUID = 4106780411821018883L;
    public String actionUri;
    public String content;
    public String msgId;
    public String title;
    public String type;
}
