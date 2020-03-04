package org.ius.gradcit.rest.controller;

import org.ius.gradcit.logic.image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TestingController {

    private final ImageService imageService;

    @Autowired
    public TestingController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("saveImagesFromExcel")
    public void saveImagesFromExcel() {
        new Thread(() -> {
            List<String> imageIds = null;
            try {
                imageIds = Files.walk(Paths.get("C:\\gradcit\\recommender\\testdata\\dataset"))
                        .skip(1)
                        .map(s -> s.toString().replace("C:\\gradcit\\recommender\\testdata\\dataset\\", "")
                                .replaceAll("\\.png", ""))
                        .collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (String imageId : imageIds) {
                imageService.saveImage(imageId, "testUser");
                System.out.println(imageId);
            }
        }).start();
    }
}
