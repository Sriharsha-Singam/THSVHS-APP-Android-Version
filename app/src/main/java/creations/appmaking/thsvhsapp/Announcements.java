package creations.appmaking.thsvhsapp;

/**
 *Copyright © 2017 Sriharsha Singam. All rights reserved.
 **/

import com.fasterxml.jackson.annotation.JsonProperty;

public class Announcements {

    private String announce;




    public Announcements()
    {

    }

    public Announcements(String announce) {
        this.announce=announce;
    }

    //@JsonProperty("announce")
    public String getAnnounce()
    {
        return announce;
    }
}
