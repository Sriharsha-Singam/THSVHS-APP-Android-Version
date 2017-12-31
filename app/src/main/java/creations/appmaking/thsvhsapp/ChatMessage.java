package creations.appmaking.thsvhsapp;

/**
 *Copyright © 2017 Sriharsha Singam. All rights reserved.
 **/



public class ChatMessage {


    private String name,message, photo, video;
    private String date;
    public ChatMessage(){

    }
    public ChatMessage(String name,String message, String photo, String video,String date)
    {

        this.name=name;
        this.message=message;
        this.photo=photo;
        this.video=video;
        this.date=date;
    }

    public String getName()
    {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getPhoto() {
        return photo;
    }
    public String getVideo() {
        return video;
    }
    public String getDate(){ return date;}
}
