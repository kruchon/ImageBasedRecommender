package org.ius.gradcit.logic.image;

import org.ius.gradcit.database.domain.node.Image;
import org.ius.gradcit.database.domain.relationship.RecognizedIn;
import org.ius.gradcit.database.repository.ImageRepository;
import org.ius.gradcit.database.repository.RecognizedInRepository;
import org.ius.gradcit.database.repository.ThematicsRepository;
import org.ius.gradcit.logic.recognition.Recognizer;
import org.ius.gradcit.logic.recognition.impl.RecognitionResult;
import org.ius.gradcit.logic.user.ActionType;
import org.ius.gradcit.logic.user.UserService;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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
                        RecognizedInRepository recognizedInRepository,
                        SessionFactory sessionFactory) {
        this.recognizer = recognizer;
        this.userService = userService;
        this.imageRepository = imageRepository;
        this.thematicsRepository = thematicsRepository;
        this.recognizedInRepository = recognizedInRepository;
    }

    @Transactional
    public void saveImage(String imageExternalId, String userExternalId) {
        Image image = createImageNode(imageExternalId);
        Collection<RecognitionResult> recognitionResults = recognizer.recognize(imageExternalId);
        saveRelationships(image, recognitionResults, userExternalId);
    }

    private void saveRelationships(Image image, Collection<RecognitionResult> recognitionResults, String userExternalId) {
        Collection<RecognizedIn> recognizedInRelationships = new ArrayList<>(50);
        for (RecognitionResult recognitionResult : recognitionResults) {
            addNewRelationship(recognitionResult, recognizedInRelationships, image, userExternalId);
        }
        recognizedInRepository.saveAll(recognizedInRelationships);
    }

    private void addNewRelationship(RecognitionResult recognitionResult, Collection<RecognizedIn> recognizedInRelationships, Image image, String userExternalId) {
        String word = recognitionResult.getWord();
        float probability = recognitionResult.getProbability();
        thematicsRepository.findByWord(word).ifPresent(thematics -> {
            userService.incInterest(userExternalId, thematics, probability, ActionType.PUBLICATION);
            RecognizedIn recognizedIn = new RecognizedIn();
            recognizedIn.setProbability(probability);
            recognizedIn.setImage(image);
            recognizedIn.setThematics(thematics);
            recognizedInRelationships.add(recognizedIn);
        });
    }

    private Image createImageNode(String imageExternalId) {
        Image image = new Image();
        image.setWhenPublicated(new Date());
        image.setExternalId(imageExternalId);
        return imageRepository.save(image);
    }
}
