package com.cm.cmrl.arraylist;

/**
 * Created by Iddinesh.
 */

public class UpsBankUpdateList {

    public String getBatteryNam() {
        return batteryNam;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String batteryNam,  quantity,  status;

    public UpsBankUpdateList(String batteryName, String quantity, String status) {


            this.batteryNam = batteryName;

            this.quantity = quantity;

            this.status = status;

    }
}
