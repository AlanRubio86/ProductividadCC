package com.productividadcc.utilerias;


public class GroupModel {

    private String _date,_name;
    private String _statusId;
    private String _id;
    private String category;

    public GroupModel(String date,String name, String statusId, String id, String category) {
        _date = date;
        _name=name;
        _statusId=statusId;
        _id=id;
        this.category=category;

    }

    public String get_date ()
    {
        return _date;
    }

    public String get_name()
    {
        return _name;
    }

    public String get_statusId()
    {
        return _statusId;
    }

    public String get_Id()
    {
        return _id;
    }

    public String getCategory() { return category; }
}
