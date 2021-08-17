package com.cm.cmrl.arraylist;

/**
 * Created by Iddinesh.
 */

public class DeparmentSpinnerList {

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

    private String status, message,id, code, name;

        public DeparmentSpinnerList(String status, String message, String id, String code, String name) {


                this.status = status;

                this.message = message;

                this.id = id;

                this.code = code;

                this.name = name;

        }
}
