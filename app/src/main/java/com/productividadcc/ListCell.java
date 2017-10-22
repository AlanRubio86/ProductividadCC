package com.productividadcc;

/**
 * Created by cesarrdz on 9/9/16.
 */
public class ListCell implements Comparable<ListCell>{

    private String name;
    private String category;
    private String id;
    private boolean isSectionHeader;

    public ListCell(String name, String category, String id)
    {
        this.name = name;
        this.category = category;
        this.id = id;
        isSectionHeader = false;
    }

    public String getName()
    {
        return name;
    }

    public String getCategory()
    {
        return category;
    }

    public String getId()
    {
        return id;
    }

    public void setToSectionHeader()
    {
        isSectionHeader = true;
    }

    public boolean isSectionHeader()
    {
        return isSectionHeader;
    }

    @Override
    public int compareTo(ListCell other) {
        return this.category.compareTo(other.category);
    }

}
