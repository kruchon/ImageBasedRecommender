package org.ius.gradcit.logic.recognition;

import org.ius.gradcit.logic.recognition.impl.RecognitionResult;

import java.util.Collection;

public interface Recognizer {
    Collection<RecognitionResult> recognize(String imageId);
}
