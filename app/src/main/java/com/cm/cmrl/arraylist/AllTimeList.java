package com.cm.cmrl.arraylist;

/**
 * Created by Iddinesh.
 */

public class AllTimeList {

    public String getEquipmentTime() {
        return equipmentTime;
    }

    public String getCompletedCount() {
        return completedCount;
    }

    private String equipmentTime;
    private String completedCount;

    public AllTimeList(String equipmentTime, String completedCount) {


        this.equipmentTime = equipmentTime;

        this.completedCount = completedCount;

    }
}
