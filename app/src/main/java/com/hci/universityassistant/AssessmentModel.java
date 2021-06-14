package com.hci.universityassistant;

public class AssessmentModel
{
    private String uID, subject, title, description, dueDate;

    public AssessmentModel(String uID, String subject, String title, String description, String dueDate)
    {
        this.uID = uID;
        this.subject = subject;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDueDate()
    {
        return dueDate;
    }

    public void setDueDate(String dueDate)
    {
        this.dueDate = dueDate;
    }

    public String getuID()
    {
        return uID;
    }

    public void setuID(String uID)
    {
        this.uID = uID;
    }
}
