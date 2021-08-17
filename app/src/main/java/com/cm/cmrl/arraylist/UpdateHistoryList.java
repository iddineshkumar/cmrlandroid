package com.cm.cmrl.arraylist;

/**
 * Created by Iddinesh.
 */

public class UpdateHistoryList {

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTicket_id() {
        return ticket_id;
    }

    public String getTicket_status() {
        return ticket_status;
    }

    public void setTicket_status(String ticket_status) {
        this.ticket_status = ticket_status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPhotos() {
        return photos;
    }

    public String getUpdated_by_id() {
        return updated_by_id;
    }

    public String getUpdated_by_name() {
        return updated_by_name;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    private String status;
    private String message;
    private String ticket_id;
    private String ticket_status;
    private String remarks;
    private String photos;
    private String updated_by_id;
    private String updated_by_name;
    private String updated_at;

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriority_name() {
        return priority_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String priority_name;
    private String description;
    private String location;

    public UpdateHistoryList(String status, String message, String ticket_id, String ticket_status, String remarks, String updated_by_id, String photos, String updated_by_name, String updated_at, String title, String priority_name,
                             String description, String location) {


                this.status = status;

                this.message = message;

                this.ticket_id = ticket_id;

        this.ticket_status = ticket_status;

                this.remarks = remarks;

                this.photos = photos;

                this.updated_by_id = updated_by_id;

                this.updated_by_name = updated_by_name;

                this.updated_at = updated_at;

            this.title = title;

            this.priority_name = priority_name;

                this.description = description;

                this.location = location;

    }
}
