package creations.appmaking.thsvhsapp;

/**
 *Copyright © 2017 Sriharsha Singam. All rights reserved.
 **/

public class Features {

    private String feature;




    public Features()
    {

    }

    public Features(String feature) {
        this.feature=feature;
    }

    //@JsonProperty("announce")
    public String getFeature()
    {
        return feature;
    }
}
