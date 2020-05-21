package com.suntelecoms.dynamicform;

/**
 * Created by Djvmil_ on 2020-02-05
 */

public class Galery {
    private String link;
    private String title;

    public Galery(String link, String title) {
        this.link = link;
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
