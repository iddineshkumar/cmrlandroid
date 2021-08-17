package com.cm.cmrl.arraylist;

/**
 * Created by Iddinesh.
 */

public class IssueStatusCountList {

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

    public String getTicket_status() {
        return ticket_status;
    }

    public void setTicket_status(String ticket_status) {
        this.ticket_status = ticket_status;
    }

    public String getTickets_count() {
        return tickets_count;
    }

    private String status,  message,  ticket_status,  tickets_count;

        public IssueStatusCountList(String status, String message, String ticket_status, String tickets_count) {
                this.status = status;

                this.message = message;

                this.ticket_status = ticket_status;

                this.tickets_count = tickets_count;
    }
}
