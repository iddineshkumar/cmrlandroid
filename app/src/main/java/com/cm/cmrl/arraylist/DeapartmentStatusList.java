package com.cm.cmrl.arraylist;

/**
 * Created by Iddinesh.
 */

public class DeapartmentStatusList {

    private int status;
    private String message;
    private String ticket_id;
    private String station_id;
    private String title;
    private String department_id;
    private String priority_id;
    private String description;
    private String photos;
    private String location;
    private  String updated_by;
    private String updated_at;
    private String remarks;
    private String ticket_status;
    private String priority_name;
    private String updated_by_name;
    private String tickethistory_id;

    private String fault_title;
    private String fault_type;
    private String trainid;
    private String train_id;
    private String report_datetime;

    public String getFault_title() {
        return fault_title;
    }

    public String getFault_type() {
        return fault_type;
    }

    public String getTrainid() {
        return trainid;
    }

    public String getTrain_id() {
        return train_id;
    }

    public String getReport_datetime() {
        return report_datetime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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

    public String getStation_id() {
        return station_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public String getPriority_id() {
        return priority_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotos() {
        return photos;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTicket_status() {
        return ticket_status;
    }

    public void setTicket_status(String ticket_status) {
        this.ticket_status = ticket_status;
    }


    public String getPriority_name() {
        return priority_name;
    }


    public String getUpdated_by_name() {
        return updated_by_name;
    }

    public String getTickethistory_id() {
        return tickethistory_id;
    }


    public DeapartmentStatusList(int status, String message, String ticket_id, String station_id, String title, String department_id, String priority_id, String description, String photos, String location, String updated_by, String updated_at, String remarks, String ticket_status, String priority_name, String updated_by_name, String tickethistory_id) {


        this.message = message;

        this.status = status;

        this.ticket_id = ticket_id;

        this.station_id = station_id;

        this.title = title;

        this.department_id = department_id;

        this.priority_id = priority_id;

        this.description = description;

        this.photos = photos;

        this.location = location;

        this.updated_by = updated_by;

        this.updated_at = updated_at;

        this.remarks = remarks;

        this.ticket_status = ticket_status;

        this.priority_name = priority_name;

        this.updated_by_name = updated_by_name;

        this.tickethistory_id = tickethistory_id;




    }


    public DeapartmentStatusList(int status, String message, String ticket_id, String station_id, String title, String department_id, String priority_id, String description, String photos, String location, String updated_by, String updated_at, String remarks, String ticket_status, String priority_name, String updated_by_name, String tickethistory_id, String fault_title, String fault_type, String trainid, String train_id, String report_datetime) {


        this.message = message;

        this.status = status;

        this.ticket_id = ticket_id;

        this.station_id = station_id;

        this.title = title;

        this.department_id = department_id;

        this.priority_id = priority_id;

        this.description = description;

        this.photos = photos;

        this.location = location;

        this.updated_by = updated_by;

        this.updated_at = updated_at;

        this.remarks = remarks;

        this.ticket_status = ticket_status;

        this.priority_name = priority_name;

        this.updated_by_name = updated_by_name;

        this.tickethistory_id = tickethistory_id;

        this.fault_title = fault_title;
        this.fault_type = fault_type;
        this.trainid = trainid ;
        this.train_id = train_id;
        this.report_datetime = report_datetime;


    }
}
