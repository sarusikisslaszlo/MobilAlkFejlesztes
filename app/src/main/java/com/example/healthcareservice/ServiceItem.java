package com.example.healthcareservice;

import java.util.List;

public class ServiceItem {

    private String id;
    private String name;
    private String providedBy;
    private List<String> caterory;
    private boolean active;
    private String comment;
    private int photo;
    private String extraDetails;
    private boolean appointmentRequired;

    public ServiceItem() {
    }

    public ServiceItem(String name, String providedBy, List<String> caterory, boolean active, String comment, int imageResource) {
        this.name = name;
        this.providedBy = providedBy;
        this.caterory = caterory;
        this.active = active;
        this.comment = comment;
        this.photo = imageResource;
        this.extraDetails = "";
        this.appointmentRequired = false;
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
        return photo;
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
        this.photo = imageResource;
    }

    public String _getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExtraDetails() {
        return extraDetails;
    }

    public void setExtraDetails(String extraDetails) {
        this.extraDetails = extraDetails;
    }

    public boolean isAppointmentRequired() {
        return appointmentRequired;
    }

    public void setAppointmentRequired(boolean appointmentRequired) {
        this.appointmentRequired = appointmentRequired;
    }
}
