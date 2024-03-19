package com.papersaccul.paperffeditor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VideoSettingTable {
    private final StringProperty setting;
    private final StringProperty input;
    private final StringProperty output;

    public VideoSettingTable(String setting, String input, String output) {
        this.setting = new SimpleStringProperty(setting);
        this.input = new SimpleStringProperty(input);
        this.output = new SimpleStringProperty(output);
    }

    public StringProperty settingProperty() {
        return setting;
    }

    public StringProperty inputProperty() {
        return input;
    }

    public StringProperty outputProperty() {
        return output;
    }
}