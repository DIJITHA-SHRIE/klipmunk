package com.telcco.klipmunk;

/**
 * Created by PHD on 1/12/2019.
 */

public class ScreensModel {
    String path,notes,tag;

    public ScreensModel(String path, String notes) {
        this.path = path;
        this.notes = notes;
    }

    public String getPath() {
        return path;
    }

    public String getNotes() {
        return notes;
    }

    public String getTag() {
        return tag;
    }
}
