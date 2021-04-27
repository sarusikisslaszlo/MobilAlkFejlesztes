package com.example.healthcareservice;

public class ServiceItem {

    private String name;
    private String providedBy;
    private String[] caterory;
    private boolean active;
    private String comment;
    private final int imageResource;

    public ServiceItem(String name, String providedBy, String[] caterory, boolean active, String comment, int imageResource) {
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

    public String[] getCaterory() {
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

    public void setCaterory(String[] caterory) {
        this.caterory = caterory;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
