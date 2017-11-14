package com.productividadcc;

public class ListCell implements Comparable<ListCell>{

    private String groupId;
    private String employeeId;
    private String typeReg;
    private String groupNumber;
    private String groupName;
    private String cicle;
    private String week;
    private String latitude;
    private String longitude;
    private String refCon;
    private String statusId;
    private String statusName;
    private String date;
    private boolean isSectionHeader;


    public ListCell(String _groupId, String _employeeId, String _typeReg, String _groupNumber, String _groupName, String _cicle, String _week, String _latitude, String _longitude, String _refCon, String _statusId,String _date ,String _statusName)
    {
        this.groupId = _groupId.trim();
        this.employeeId = _employeeId.trim();
        this.typeReg = _typeReg.trim();
        this.groupNumber=_groupNumber.trim();
        this.groupName=_groupName.trim();
        isSectionHeader=false;
        this.cicle=_cicle.trim();
        this.week=_week.trim();
        this.latitude=_latitude.trim();
        this.longitude=_longitude.trim();
        this.refCon=_refCon.trim();
        this.statusId=_statusId.trim();
        this.date=_date.trim();
        this.statusName = _statusName.trim();
    }

    public String getGroupId() {return groupId;}

    public void setToSectionHeader() {
        isSectionHeader=true;
    }

    public boolean IsSectionHeader() {
        return isSectionHeader;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getTypeReg()
    {
        return typeReg;
    }

    public String getGroupNumber()
    {
        return groupNumber;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public String getCicle()
    {
        return cicle;
    }

    public String getWeek()
    {
        return week;
    }

    public String getLatitute()
    {
        return latitude;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public String getRefCon()
    {
        return refCon;
    }

    public String getStatusId()
    {
        return statusId;
    }

    public String getDate()
    {
        return date;
    }

    public String getStatusName()
    {
        return statusName;
    }

    public int compareTo(ListCell other) {
        return this.statusId.compareTo(other.statusId);
    }

}
