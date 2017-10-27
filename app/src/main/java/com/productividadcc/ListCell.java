package com.productividadcc;

import java.util.Date;

/**
 * Created by cesarrdz on 9/9/16.
 */
public class ListCell{

    private String name;
    private String statusId;
    private String id;
    private String date;

    public ListCell(String name, String statusId, String id, String date )
    {
        this.name = name;
        this.statusId = statusId;
        this.id = id;
        this.date=date;
    }

    public String getName()
    {
        return name;
    }

    public String getStatusId()
    {
        return statusId;
    }

    public String getId()
    {
        return id;
    }

    public String getDate()
    {
        return date;
    }


}
