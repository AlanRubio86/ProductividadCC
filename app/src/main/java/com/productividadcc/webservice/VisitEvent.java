package com.productividadcc.webservice;

/**
 * Created by cesarrdz on 12/7/16.
 */
public class VisitEvent {
    public static final String	EVENT_TYPE_CHECKIN 		= "CheckIn";
    public static final String	EVENT_TYPE_CHECKOUT 	= "CheckOut";

    public static final String	VISIT_TYPE_REGULAR	 	= "Regular";
    public static final String	VISIT_TYPE_PROMOTION 	= "Promotion";
    public static final String	VISIT_TYPE_COLLECTION 	= "Collection";
    public static final String	VISIT_TYPE_BRANCH	 	= "Branch";
    public static final String	VISIT_TYPE_COMPETITION 	= "Competitor";
    public static final String	VISIT_TYPE_APPROVAL	 	= "Approval";
    public static final String	VISIT_TYPE_DISBURSEMENT	= "Disbursement";
    public static final String	VISIT_TYPE_TRAINING		= "Training";
    public static final String	VISIT_TYPE_UNKNOWN		= "Unknown";

    String 			phoneId;
    String			groupId;
    String			eventType;		//CheckIn | CheckOut
    String			visitType;
    String			eventTime;		//??
    VisitLocation	location;
    String			notes;


    public String getPhoneId()
    {
        return phoneId;
    }



    public String getGroupId()
    {
        return groupId;
    }



    public String getEventType()
    {
        return eventType;
    }



    public String getVisitType()
    {
        return visitType;
    }



    public String getEventTime()
    {
        return eventTime;
    }



    public VisitLocation getLocation()
    {
        return location;
    }



    public String getNotes()
    {
        return notes;
    }



    public void setPhoneId(String phoneId)
    {
        this.phoneId = phoneId;
    }



    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }



    public void setEventType(String eventType)
    {
        this.eventType = eventType;
    }



    public void setVisitType(String visitType)
    {
        this.visitType = visitType;
    }



    public void setEventTime(String eventTime)
    {
        this.eventTime = eventTime;
    }



    public void setLocation(VisitLocation location)
    {
        this.location = location;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }
}