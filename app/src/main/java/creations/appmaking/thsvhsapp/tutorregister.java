package creations.appmaking.thsvhsapp;

/**
 *Copyright © 2017 Sriharsha Singam. All rights reserved.
 **/

public class tutorregister {
    private String course,coursestaken;

    public tutorregister(){

    }
    public tutorregister(String course,String coursestaken)
    {

        this.course=course;
        this.coursestaken=coursestaken;

    }

    public String getCourse()
    {
        return course;
    }

    public String getCoursetaken() {
        return coursestaken;
    }

}
