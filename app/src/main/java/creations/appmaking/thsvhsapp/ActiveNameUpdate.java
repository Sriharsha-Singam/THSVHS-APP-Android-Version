package creations.appmaking.thsvhsapp;

/**
 *Copyright © 2017 Sriharsha Singam. All rights reserved.
 **/

public class ActiveNameUpdate {
    private String name , email;

    public ActiveNameUpdate()
    {

    }

    public ActiveNameUpdate(String name ,String email) {
        this.name=name;
        this.email = email;

    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

}
