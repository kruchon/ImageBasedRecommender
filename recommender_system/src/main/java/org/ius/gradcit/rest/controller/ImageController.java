package org.ius.gradcit.rest.controller;

import org.ius.gradcit.logic.image.ImageService;
import org.ius.gradcit.rest.entity.ImageToSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("saveImage")
    public void saveImage(@RequestBody ImageToSave imageToSave) {
        String imageId = imageToSave.getImageid();
        String userId = imageToSave.getUserid();
        imageService.saveImage(imageId, userId);
    }
}
