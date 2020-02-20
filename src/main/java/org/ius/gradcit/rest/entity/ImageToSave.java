package org.ius.gradcit.rest.entity;

public class ImageToSave {

    private final String imageId;
    private final String userId;

    public ImageToSave(String imageId, String userId) {
        this.imageId = imageId;
        this.userId = userId;
    }

    public String getImageId() {
        return imageId;
    }

    public String getUserId() {
        return userId;
    }
}
