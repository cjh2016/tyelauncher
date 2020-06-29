package com.boll.tyelauncher.push;


import java.io.Serializable;

public class RemotePushMessage implements Serializable {
    private static final long serialVersionUID = -6837722844902858328L;
    private String data;
    private String pkgname;
    private String type;

    public void setType(String type2) {
        this.type = type2;
    }

    public String getType() {
        return this.type;
    }

    public void setPkgname(String pkgname2) {
        this.pkgname = pkgname2;
    }

    public String getPkgname() {
        return this.pkgname;
    }

    public void setData(String data2) {
        this.data = data2;
    }

    public String getData() {
        return this.data;
    }

    public String toString() {
        return "type: " + this.type + "\n" + "pkgname: " + this.pkgname + "\n" + "data: " + this.data;
    }
}