package org.ius.gradcit.logic.user;

import org.ius.gradcit.database.domain.node.Thematics;
import org.ius.gradcit.database.domain.node.User;
import org.ius.gradcit.database.domain.relationship.InterestedIn;
import org.ius.gradcit.database.repository.InterestedInRepository;
import org.ius.gradcit.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final InterestedInRepository interestedInRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       InterestedInRepository interestedInRepository) {
        this.userRepository = userRepository;
        this.interestedInRepository = interestedInRepository;
    }

    public void incInterest(String userExternalId, Thematics thematics, float weight, ActionType actionType) {
        Optional<User> optionalUser = userRepository.findByExternalId(userExternalId);
        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            user = new User();
            user.setExternalId(userExternalId);
            userRepository.save(user);
        }
        InterestedIn interestedIn = new InterestedIn();
        interestedIn.setUser(user);
        interestedIn.setThematics(thematics);
        float oldInterestDegree = interestedIn.getInterestDegree();
        float newInterestDegree = weight * actionType.getCoef() + oldInterestDegree;
        interestedIn.setInterestDegree(newInterestDegree);
        interestedInRepository.save(interestedIn);
    }
}
