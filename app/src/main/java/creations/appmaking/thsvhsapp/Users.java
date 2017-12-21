package creations.appmaking.thsvhsapp;

/**
 *Copyright © 2017 Sriharsha Singam. All rights reserved.
 **/

public class Users {

    private String announce, social, email, profileImage;

    public Users()
    {

    }

    public Users(String announce,String social,String email, String profileImage) {
        this.announce=announce;
        this.social = social;
        this.email = email;
        this.profileImage = profileImage;
    }

    public String getAnnounce()
    {
        return announce;
    }

    public String getSocial()
    {
        return social;
    }

    public String getEmail()
    {
        return email;
    }
    public String getProfileImage(){
        return profileImage;
    }

}
