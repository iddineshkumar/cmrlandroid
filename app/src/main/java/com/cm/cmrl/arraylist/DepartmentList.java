package com.cm.cmrl.arraylist;

/**
 * Created by Iddinesh.
 */

public class DepartmentList {

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String status;
    private String message;
    private String id;
    private String code;
    private String name;

    public String getOpen_ticket_count() {
        return open_ticket_count;
    }

    public String getInprogress_ticket_count() {
        return inprogress_ticket_count;
    }

    public String getPending_ticket_count() {
        return pending_ticket_count;
    }

    public String getCompleted_ticket_count() {
        return complete_ticket_count;
    }

    public String getClose_ticket_count() {
        return close_ticket_count;
    }

    private String open_ticket_count, close_ticket_count, inprogress_ticket_count, pending_ticket_count, complete_ticket_count;

    public DepartmentList(String status, String message, String id, String code, String name, String open_ticket_count, String inprogress_ticket_count, String pending_ticket_count, String complete_ticket_count, String close_ticket_count) {


        this.status = status;

        this.message = message;

        this.id = id;

        this.code = code;

        this.name = name;
        this.open_ticket_count = open_ticket_count;
        this.inprogress_ticket_count = inprogress_ticket_count;
        this.pending_ticket_count = pending_ticket_count;
        this.complete_ticket_count = complete_ticket_count;
        this.close_ticket_count = close_ticket_count;
    }
}
