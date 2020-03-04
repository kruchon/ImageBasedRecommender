package org.ius.gradcit.database.domain.node;

import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;

@NodeEntity(label = "Token")
public class Thematics {

    public Thematics() {}

    @Id
    @GeneratedValue
    private Long id;

    @Property
    private List<Float> embedding;

    @Index(unique = true)
    @Property
    private String word;

    public List<Float> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(List<Float> embedding) {
        this.embedding = embedding;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
