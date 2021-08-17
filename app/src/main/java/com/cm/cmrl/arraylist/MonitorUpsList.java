package com.cm.cmrl.arraylist;

/**
 * Created by Iddinesh.
 */

public class MonitorUpsList {

    public MonitorUpsList() {

    }

    public MonitorUpsList(String id, String name) {
        this.name = name;

        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

   private String id ="",  name="";

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    private int ID;

    public MonitorUpsList(int ID, String id, String name) {


            this.name = name;

            this.id = id;

            this.ID = ID;


    }
}
