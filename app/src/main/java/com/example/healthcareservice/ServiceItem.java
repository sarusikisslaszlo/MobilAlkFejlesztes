package com.example.healthcareservice;

import java.util.List;

public class ServiceItem {

    private String name;
    private String providedBy;
    private List<String> caterory;
    private boolean active;
    private String comment;
    private int imageResource;

    public ServiceItem() {
    }

    public ServiceItem(String name, String providedBy, List<String> caterory, boolean active, String comment, int imageResource) {
        this.name = name;
        this.providedBy = providedBy;
        this.caterory = caterory;
        this.active = active;
        this.comment = comment;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public String getProvidedBy() {
        return providedBy;
    }

    public List<String> getCaterory() {
        return caterory;
    }

    public boolean isActive() {
        return active;
    }

    public String getComment() {
        return comment;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProvidedBy(String providedBy) {
        this.providedBy = providedBy;
    }

    public void setCaterory(List<String> caterory) {
        this.caterory = caterory;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
