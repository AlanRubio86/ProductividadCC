package com.productividadcc;

import java.util.Date;

/**
 * Created by cesarrdz on 9/9/16.
 */
public class ListCell implements Comparable<ListCell>{

    private String name;
    private String statusId;
    private String id;
    private String date;
    private String category;
    private boolean isSectionHeader;
    private String parent;
    private String ubication;

    public ListCell(String name, String statusId, String id, String date, String category,String parent,String ubication  )
    {
        this.name = name;
        this.statusId = statusId;
        this.id = id;
        this.date=date;
        this.category=category;
        isSectionHeader=false;
        this.parent=parent;
        this.ubication=ubication;

    }

    public String getUbication() {return ubication;}

    public void setToSectionHeader() {
        isSectionHeader=true;
    }

    public boolean IsSectionHeader() {
        return isSectionHeader;
    }

    public String getCategory() {
        return category;
    }

    public String getParent()
    {
        return parent;
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

    public int compareTo(ListCell other) {
        return this.category.compareTo(other.category);
    }

}
