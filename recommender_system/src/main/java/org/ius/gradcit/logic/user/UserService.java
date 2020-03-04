package org.ius.gradcit.logic.user;

import org.ius.gradcit.database.domain.node.Thematics;
import org.ius.gradcit.database.domain.node.User;
import org.ius.gradcit.database.domain.relationship.InterestedIn;
import org.ius.gradcit.database.repository.InterestedInRepository;
import org.ius.gradcit.database.repository.RecognizedInRepository;
import org.ius.gradcit.database.repository.UserRepository;
import org.ius.gradcit.rest.entity.UserAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final InterestedInRepository interestedInRepository;
    private final RecognizedInRepository recognizedInRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       InterestedInRepository interestedInRepository,
                       RecognizedInRepository recognizedInRepository) {
        this.userRepository = userRepository;
        this.interestedInRepository = interestedInRepository;
        this.recognizedInRepository = recognizedInRepository;
    }

    public void incInterest(String userExternalId, Thematics thematics, float weight, ActionType actionType) {
        User user = getOrSaveUser(userExternalId);
        InterestedIn interestedIn = getOrCreateInterestedIn(user, thematics);
        float oldInterestDegree = interestedIn.getInterestDegree();
        float newInterestDegree = weight * actionType.getCoef() + oldInterestDegree;
        interestedIn.setInterestDegree(newInterestDegree);
        interestedInRepository.save(interestedIn);
    }

    private InterestedIn getOrCreateInterestedIn(User user, Thematics thematics) {
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
            recognizedInRepository.findById(recognizedInId).ifPresent(recognizedIn -> {
                float probability = recognizedIn.getProbability();
                Thematics thematics = recognizedIn.getThematics();
                incInterest(userId, thematics, probability, actionType);
            });
        }
    }
}
