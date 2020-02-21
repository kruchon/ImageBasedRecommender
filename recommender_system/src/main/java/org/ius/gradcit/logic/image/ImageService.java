package org.ius.gradcit.logic.image;

import org.ius.gradcit.database.domain.node.Image;
import org.ius.gradcit.database.domain.node.Thematics;
import org.ius.gradcit.database.repository.ImageRepository;
import org.ius.gradcit.database.repository.ThematicsRepository;
import org.ius.gradcit.logic.recognition.Recognizer;
import org.ius.gradcit.logic.recognition.impl.RecognitionResult;
import org.ius.gradcit.logic.user.ActionType;
import org.ius.gradcit.logic.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ImageService {

    private final Recognizer recognizer;
    private final UserService userService;
    private final ImageRepository imageRepository;
    private final ThematicsRepository thematicsRepository;

    @Autowired
    public ImageService(@Qualifier("fakeRecognizerFromExcel") Recognizer recognizer,
                        UserService userService,
                        ImageRepository imageRepository,
                        ThematicsRepository thematicsRepository) {
        this.recognizer = recognizer;
        this.userService = userService;
        this.imageRepository = imageRepository;
        this.thematicsRepository = thematicsRepository;
    }

    @Transactional
    public void saveImage(String imageExternalId, String userExternalId) {
        Collection<RecognitionResult> recognitionResults = recognizer.recognize(imageExternalId);
        Image image = createImageNode(imageExternalId);
        saveImageInGraph(image, recognitionResults, userExternalId);
    }

    private void saveImageInGraph(Image image, Collection<RecognitionResult> recognitionResults, String userExternalId) {
        for (RecognitionResult recognitionResult : recognitionResults) {
            String word = recognitionResult.getWord();
            float probability = recognitionResult.getProbability();
            Optional<Thematics> thematicsOptional = thematicsRepository.findByWord(word);
            if (thematicsOptional.isPresent()) {
                Thematics thematics = thematicsOptional.get();
                image.getObjectClasses().add(thematics);
                userService.incInterest(userExternalId, word, probability, ActionType.PUBLICATION);
                imageRepository.save(image);
            }
        }
    }

    private Image createImageNode(String imageExternalId) {
        Image image = new Image();
        image.setWhenPublicated(new Date());
        image.setExternalId(imageExternalId);
        image.setObjectClasses(new HashSet<>(50));
        return imageRepository.save(image);
    }
}
