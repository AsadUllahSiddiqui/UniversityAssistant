package com.hci.universityassistant;

public class FlashcardModel
{
    private String uID, subject, front, back, notes;

    public FlashcardModel(String uID, String subject, String front, String back, String notes)
    {
        this.uID = uID;
        this.subject = subject;
        this.front = front;
        this.back = back;
        this.notes = notes;
    }

    public String getuID()
    {
        return uID;
    }

    public void setuID(String uID)
    {
        this.uID = uID;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getFront()
    {
        return front;
    }

    public void setFront(String front)
    {
        this.front = front;
    }

    public String getBack()
    {
        return back;
    }

    public void setBack(String back)
    {
        this.back = back;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }
}
