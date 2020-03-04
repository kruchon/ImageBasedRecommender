package org.ius.gradcit.logic.user;

import org.ius.gradcit.database.domain.node.User;
import org.ius.gradcit.database.repository.InterestedInRepository;
import org.ius.gradcit.database.repository.ThematicsRepository;
import org.ius.gradcit.database.repository.UserRepository;
import org.ius.gradcit.logic.recognition.Recognizer;
import org.ius.gradcit.logic.recognition.impl.RecognitionResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private InterestedInRepository interestedInRepository;

    @MockBean
    private Recognizer recognizer;

    @MockBean
    private ThematicsRepository thematicsRepository;

    private static final String USER_EXTERNAL_ID = "test_user_id";
    private static final String IMAGE_EXTERNAL_ID = "test_image_id";

    @Before
    public void setUp(){
        mockRecognizer();
        mockThematicsRepository();
    }

    @Test
    void testIncInterestCreatedRelations() {
        mockUserRepositoryReturnUser();
    }

    private void mockThematicsRepository() {

    }

    private void mockRecognizer() {
        RecognitionResult catResult = new RecognitionResult("cat", 0.9f);
        RecognitionResult dogResult = new RecognitionResult("dog", 0.6f);
        List<RecognitionResult> recognitionResults = asList(catResult, dogResult);
        when(recognizer.recognize(eq(IMAGE_EXTERNAL_ID))).thenReturn(recognitionResults);
    }

    private void mockUserRepositoryReturnUser() {
        User user = new User();
        user.setExternalId(USER_EXTERNAL_ID);
        when(userRepository.findByExternalId(eq(USER_EXTERNAL_ID))).thenReturn(Optional.of(user));
    }
}