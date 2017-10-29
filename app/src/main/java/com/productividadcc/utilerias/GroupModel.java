package com.productividadcc.utilerias;


public class GroupModel {

    private String _date,_name;
    private String _statusId;
    private String _id;
    private String _category;
    private String _parent;
    private String _ubication;

    public GroupModel(String date,String name, String statusId, String id, String category,String parent,String ubication) {
        _date = date;
        _name=name;
        _statusId=statusId;
        _id=id;
        _category=category;
        _parent=parent;
        _ubication=ubication;

    }
    public String get_Parent() {return _parent;}

    public String get_Ubication() {return _parent;}

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

    public String getCategory() { return _category; }
}
