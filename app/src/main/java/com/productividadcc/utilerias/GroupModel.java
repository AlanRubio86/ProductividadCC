package com.productividadcc.utilerias;


public class GroupModel {

    private String groupId,  employeeId,  typeReg,  groupNumber,  groupName,  cicle,  week,  latitude,  longitude,  refCon, statusId, date;

    public GroupModel(String _groupId, String _employeeId, String _typeReg, String _groupNumber, String _groupName, String _cicle, String _week, String _latitude, String _longitude, String _refCon, String _statusId,String _date )
    {
        this.groupId = _groupId;
        this.employeeId = _employeeId;
        this.typeReg = _typeReg;
        this.groupNumber=_groupNumber;
        this.groupName=_groupName;
        this.cicle=_cicle;
        this.week=_week;
        this.latitude=_latitude;
        this.longitude=_longitude;
        this.refCon=_refCon;
        this.statusId=_statusId;
        this.date=_date;
    }
    public String getGroupId() {return groupId;}

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
}
