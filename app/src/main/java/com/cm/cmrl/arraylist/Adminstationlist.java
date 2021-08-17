package com.cm.cmrl.arraylist;

/**
 * Created by Iddinesh.
 */

public class Adminstationlist {

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStationType() {
        return station_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getOpen_ticket_count() {
        return open_ticket_count;
    }

    public String getInprogress_ticket_count() {
        return inprogress_ticket_count;
    }

    public String getFavourite() {
        return favourite_status;
    }

    public String getPending_ticket_count() {
        return pending_ticket_count;
    }
    public String getCode() {
        return code;
    }


    public String getCompleted_ticket_count() {
        return completed_ticket_count;
    }

    public String getClosed_ticket_count() {
        return closed_ticket_count;
    }

    private String status,  message,  id,  station_type,  name,  open_ticket_count,
     inprogress_ticket_count,  pending_ticket_count,  completed_ticket_count,  closed_ticket_count,code,favourite_status;

        public Adminstationlist(String status, String message, String id, String code, String station_type, String name, String open_ticket_count, String inprogress_ticket_count, String pending_ticket_count, String completed_ticket_count, String closed_ticket_count, String favourite_status) {


                this.status = status;

                this.message = message;

                this.id = id;

                this.station_type = station_type;

                this.name = name;

                this.open_ticket_count = open_ticket_count;

                this.inprogress_ticket_count = inprogress_ticket_count;

                this.pending_ticket_count = pending_ticket_count;

                this.completed_ticket_count = completed_ticket_count;
                this.code = code;

                this.closed_ticket_count = closed_ticket_count;
                this.favourite_status = favourite_status;


        }
}
