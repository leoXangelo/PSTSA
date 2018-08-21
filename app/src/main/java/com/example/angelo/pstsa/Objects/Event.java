package com.example.angelo.pstsa.Objects;

/**
 * Created by MastahG on 2/25/2018.
 */

public class Event {
    public int Event_ID;
    public String Event_Name;
    public String Event_Date;
    public String Event_Place;

    public Event() {
    }

    public Event(String event_Name, String event_Date, String event_Place) {
        Event_Name = event_Name;
        Event_Date = event_Date;
        Event_Place = event_Place;
    }

    public Event(int event_ID, String event_Name, String event_Date, String event_Place) {
        Event_ID = event_ID;
        Event_Name = event_Name;
        Event_Date = event_Date;
        Event_Place = event_Place;
    }

    public int getEvent_ID() {
        return Event_ID;
    }

    public void setEvent_ID(int event_ID) {
        Event_ID = event_ID;
    }

    public String getEvent_Name() {
        return Event_Name;
    }

    public void setEvent_Name(String event_Name) {
        Event_Name = event_Name;
    }

    public String getEvent_Date() {
        return Event_Date;
    }

    public void setEvent_Date(String event_Date) {
        Event_Date = event_Date;
    }

    public String getEvent_Place() {
        return Event_Place;
    }

    public void setEvent_Place(String event_Place) {
        Event_Place = event_Place;
    }
}
