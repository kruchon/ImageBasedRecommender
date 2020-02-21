package org.ius.gradcit.logic.recognition.impl;

public class RecognitionResult {

    private final String word;
    private final float probability;

    public RecognitionResult(String word, float probability) {
        this.word = word;
        this.probability = probability;
    }

    public String getWord() {
        return word;
    }

    public float getProbability() {
        return probability;
    }
}
