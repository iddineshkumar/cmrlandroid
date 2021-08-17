package com.cm.cmrl.arraylist;

/**
 * Created by Iddinesh.
 */

public class AllList {

    public String getEquipmentId() {
        return equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public String getEquipmentTime() {
        return equipmentTime;
    }

    public String getEquipmentType() {
        return EquipmentType;
    }

    public String getCompletedCount() {
        return completedCount;
    }

    public String getPendingCount() {
        return pendingCount;
    }

    public String getUpcomingCount() {
        return upcomingCount;
    }

    private String equipmentId, equipmentName, equipmentTime, EquipmentType, completedCount,pendingCount,upcomingCount;

    public AllList(String equipmentId, String equipmentName, String equipmentTime, String EquipmentType, String completedCount, String pendingCount, String upcomingCount) {


            this.equipmentId = equipmentId;
            this.equipmentName = equipmentName;

            this.equipmentTime = equipmentTime;

            this.EquipmentType = EquipmentType;

            this.completedCount = completedCount;

            this.pendingCount = pendingCount;

            this.upcomingCount = upcomingCount;
    }
}
