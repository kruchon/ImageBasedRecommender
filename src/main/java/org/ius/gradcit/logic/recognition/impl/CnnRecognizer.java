package org.ius.gradcit.logic.recognition.impl;

import org.ius.gradcit.logic.recognition.Recognizer;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
public class CnnRecognizer implements Recognizer {

    public Collection<RecognitionResult> recognize(String imageId) {
        return Collections.emptyList();
    }

}
