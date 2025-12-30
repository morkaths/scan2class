package com.morkath.scan2class.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssetDto {
    private String title;
    private List<String> stylesheets;
    private List<String> scripts;

    public AssetDto() {
        super();
        this.stylesheets = new ArrayList<>();
        this.scripts = new ArrayList<>();
    }
    
    public AssetDto(String title) {
        super();
        this.title = title;
        this.stylesheets = new ArrayList<>();
        this.scripts = new ArrayList<>();
    }

    public AssetDto(String title, List<String> stylesheets, List<String> scripts) {
        super();
        this.title = title;
        this.stylesheets = stylesheets;
        this.scripts = scripts;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getStylesheets() {
        return stylesheets;
    }

    public void setStylesheets(List<String> stylesheets) {
        this.stylesheets = stylesheets;
    }

    public List<String> getScripts() {
        return scripts;
    }

    public void setScripts(List<String> scripts) {
        this.scripts = scripts;
    }

    public void addStylesheets(String... stylesheets) {
        this.stylesheets.addAll(Arrays.asList(stylesheets));
    }

    public void addScripts(String... scripts) {
        this.scripts.addAll(Arrays.asList(scripts));
    }
}
