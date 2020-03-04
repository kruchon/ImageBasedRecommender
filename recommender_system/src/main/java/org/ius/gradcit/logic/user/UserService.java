package org.ius.gradcit.logic.user;

import org.ius.gradcit.database.domain.node.Thematics;
import org.ius.gradcit.database.domain.node.User;
import org.ius.gradcit.database.domain.relationship.InterestedIn;
import org.ius.gradcit.database.domain.relationship.RecognizedIn;
import org.ius.gradcit.database.repository.*;
import org.ius.gradcit.rest.entity.UserAction;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final InterestedInRepository interestedInRepository;
    private final RecognizedInRepository recognizedInRepository;
    private final SessionFactory sessionFactory;
    private final ImageRepository imageRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       InterestedInRepository interestedInRepository,
                       ThematicsRepository thematicsRepository,
                       RecognizedInRepository recognizedInRepository,
                       SessionFactory sessionFactory, ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.sessionFactory = sessionFactory;
        this.interestedInRepository = interestedInRepository;

        this.recognizedInRepository = recognizedInRepository;
        this.imageRepository = imageRepository;
    }

    public void incInterest(String userExternalId, Thematics thematics, float weight, ActionType actionType) {
        User user = getOrSaveUser(userExternalId);
        InterestedIn interestedIn = getOrSaveInterestedIn(user, thematics);
        float oldInterestDegree = interestedIn.getInterestDegree();
        float newInterestDegree = weight * actionType.getCoef() + oldInterestDegree;
        interestedIn.setInterestDegree(newInterestDegree);
        interestedInRepository.save(interestedIn);
    }

    private InterestedIn getOrSaveInterestedIn(User user, Thematics thematics) {
        Long thematicsId = thematics.getId();
        Long userId = user.getId();
        return interestedInRepository.findByUserIdAndThematicsId(userId, thematicsId)
                .orElseGet(() -> createInterestedIn(user, thematics));
    }

    private InterestedIn createInterestedIn(User user, Thematics thematics) {
        InterestedIn interestedIn = new InterestedIn();
        interestedIn.setUser(user);
        interestedIn.setThematics(thematics);
        return interestedIn;
    }

    private User getOrSaveUser(String userExternalId) {
        return userRepository.findByExternalId(userExternalId)
                .orElseGet(() -> saveUser(userExternalId));
    }

    private User saveUser(String userExternalId) {
        User user = new User();
        user.setExternalId(userExternalId);
        userRepository.save(user);
        return user;
    }

    @Transactional
    public void incInterest(UserAction userAction) {
        String userId = userAction.getUserId();
        ActionType actionType = ActionType.valueOf(userAction.getActionType());
        String imageId = userAction.getImageId();
        List<Long> recognizedInIds = recognizedInRepository.getByImageExternalId(imageId);
        for (Long recognizedInId : recognizedInIds) {
            recognizedInRepository.findById(recognizedInId).ifPresent(recognizedIn-> {
                float probability = recognizedIn.getProbability();
                Thematics thematics = recognizedIn.getThematics();
                incInterest(userId, thematics, probability, actionType);
            });
        }
    }
}
