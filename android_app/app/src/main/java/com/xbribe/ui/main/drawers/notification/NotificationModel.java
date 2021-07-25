package com.xbribe.ui.main.drawers.notification;

public class NotificationModel
{
    String title;
    String message ;

    public NotificationModel(String title, String message)
    {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
