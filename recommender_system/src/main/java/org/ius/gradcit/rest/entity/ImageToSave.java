package org.ius.gradcit.rest.entity;


public class ImageToSave {

    private String imageid;
    private String userid;

    public ImageToSave(){}

    public ImageToSave(String imageid, String userid) {
        this.imageid = imageid;
        this.userid = userid;
    }

    public void setImageid(String imageid) {
        this.imageid = imageid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImageid() {
        return imageid;
    }

    public String getUserid() {
        return userid;
    }
}
