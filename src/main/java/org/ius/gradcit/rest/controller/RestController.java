package org.ius.gradcit.rest.controller;

import org.ius.gradcit.logic.image.ImageService;
import org.ius.gradcit.rest.entity.ImageToSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RestController {

    private final ImageService imageService;

    @Autowired
    public RestController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("saveImage")
    public void saveImage(ImageToSave imageToSave) {
        String imageId = imageToSave.getImageId();
        String userId = imageToSave.getUserId();
        imageService.saveImage(imageId, userId);
    }
}
