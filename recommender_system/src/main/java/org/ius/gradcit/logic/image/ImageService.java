package org.ius.gradcit.logic.image;

import org.ius.gradcit.database.domain.node.Image;
import org.ius.gradcit.database.domain.node.Thematics;
import org.ius.gradcit.database.domain.relationship.RecognizedIn;
import org.ius.gradcit.database.repository.ImageRepository;
import org.ius.gradcit.database.repository.RecognizedInRepository;
import org.ius.gradcit.database.repository.ThematicsRepository;
import org.ius.gradcit.logic.recognition.Recognizer;
import org.ius.gradcit.logic.recognition.impl.RecognitionResult;
import org.ius.gradcit.logic.user.ActionType;
import org.ius.gradcit.logic.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ImageService {

    private final Recognizer recognizer;
    private final UserService userService;
    private final ImageRepository imageRepository;
    private final ThematicsRepository thematicsRepository;
    private final RecognizedInRepository recognizedInRepository;

    @Autowired
    public ImageService(@Qualifier("fakeRecognizerFromExcel") Recognizer recognizer,
                        UserService userService,
                        ImageRepository imageRepository,
                        ThematicsRepository thematicsRepository,
                        RecognizedInRepository recognizedInRepository) {
        this.recognizer = recognizer;
        this.userService = userService;
        this.imageRepository = imageRepository;
        this.thematicsRepository = thematicsRepository;
        this.recognizedInRepository = recognizedInRepository;
    }

    public void saveImage(String imageExternalId, String userExternalId) {
        Collection<RecognitionResult> recognitionResults = recognizer.recognize(imageExternalId);
        Image image = createImageNode(imageExternalId);
        saveRelationships(image, recognitionResults, userExternalId);
    }

    private void saveRelationships(Image image, Collection<RecognitionResult> recognitionResults, String userExternalId) {
        Collection<RecognizedIn> recognizedInRelationships = new ArrayList<>(50);
        for (RecognitionResult recognitionResult : recognitionResults) {
            String word = recognitionResult.getWord();
            float probability = recognitionResult.getProbability();
            Optional<Thematics> thematicsOptional = thematicsRepository.findByWord(word);
            if (thematicsOptional.isPresent()) {
                Thematics thematics = thematicsOptional.get();
                userService.incInterest(userExternalId, thematics, probability, ActionType.PUBLICATION);
                RecognizedIn recognizedIn = new RecognizedIn();
                recognizedIn.setProbability(probability);
                recognizedIn.setImage(image);
                recognizedIn.setThematics(thematics);
                recognizedInRelationships.add(recognizedIn);
            }
        }
        recognizedInRepository.saveAll(recognizedInRelationships);
    }

    private Image createImageNode(String imageExternalId) {
        Image image = new Image();
        image.setWhenPublicated(new Date());
        image.setExternalId(imageExternalId);
        return imageRepository.save(image);
    }
}
